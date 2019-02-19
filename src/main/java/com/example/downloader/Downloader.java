package com.example.downloader;


import com.example.downloader.Utils.DownloaderUtil;
import com.example.downloader.dto.ArgsDataParseDTO;
import com.example.downloader.protocols.Connection;
import com.example.downloader.protocols.ConnectionFactory;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

public class Downloader implements ProgressCallBack {

    private static Logger log = LoggerFactory.getLogger(Downloader.class);
    private static long MB = 1024 * 1024;
    private ProgressBar progressBar;
    private Connection connection;
    private CountDownLatch downloadCountLatch;
    private int connectionCount = 1;
    private boolean userConCountSet = false;
    private String localPath;


    public void init(ArgsDataParseDTO dto) {
        this.localPath = dto.getLocalPath();
        this.connection = ConnectionFactory.getConnection(dto);
        if (dto.getNumOfConnections() != -1) {
            this.connectionCount = dto.getNumOfConnections();
            this.userConCountSet = true;
        }
        this.decideConnectionCount();
        this.progressBar = new ProgressBarBuilder()
                .setTaskName("Downloading")
                .setUnit("MB", MB)
                .setInitialMax(connection.getContentLength())
                .showSpeed().build();
        this.downloadCountLatch = new CountDownLatch(connectionCount);

    }

    public void download() {
        FileOutputStream fos;
        ReadableByteChannel rbc;
        System.out.println("Created " + connectionCount + " connections ....");
        try {

            long portion = connection.getContentLength() / connectionCount;
            long offset = 0;

            File[] partialFiles = new File[connectionCount];
            Path base = Paths.get(this.localPath);
            Path file = Paths.get(connection.getFileName());
            Path resolve = base.resolve(file);

            for (int i = 1; i <= connectionCount; i++) {
                if (i != connectionCount) {
                    rbc = new CallbackByteChannel(connection, this, i, offset, offset + portion);
                } else {
                    rbc = new CallbackByteChannel(connection, this, i, offset, connection.getContentLength());
                }

                String partialFileName = resolve.toString() + ".temp" + i;
                fos = new FileOutputStream(partialFileName);
                new PartialDownloader(rbc, fos, downloadCountLatch).partialDownload();
                offset += portion;
                partialFiles[i - 1] = new File(partialFileName);
            }

            downloadCountLatch.await();
            progressBar.close();
            System.out.println("Download completed...");
            System.out.println("Merging partial files .....");
            DownloaderUtil.mergePartialFiles(resolve.toFile(), partialFiles);
            System.out.println("Merging partial files completed ...");
            System.out.println("Deleting partial files ....");
            DownloaderUtil.deleteParialFiles(partialFiles);
            System.out.println("Deleting partial files completed ....");


        } catch (Exception e) {
            log.error("download failed {}", e.getMessage());
        }
    }


    private void decideConnectionCount() {
        if (!this.userConCountSet && connection.isSupportRange()) {
            long contentLength = connection.getContentLength();
            if (contentLength < 4 * MB) {
                connectionCount = 1;
            } else if (contentLength < 10 * MB) {
                connectionCount = 2;
            } else if (contentLength < 80 * MB) {
                connectionCount = 4;
            } else if (contentLength < 200 * MB) {
                connectionCount = 8;
            } else {
                connectionCount = 12;
            }
        }
    }

    @Override
    public void callback(CallbackByteChannel rbc) {
        progressBar.stepTo(rbc.getFullReadSoFar());
    }
}
