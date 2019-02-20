package com.example.idownload;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.CountDownLatch;

public class PartialDownloader implements Runnable {
    private static Logger log = LoggerFactory.getLogger(PartialDownloader.class);

    private ReadableByteChannel callbackByteChannel;
    private FileOutputStream fileOutputStream;
    private CountDownLatch countDownLatch;


    public PartialDownloader(ReadableByteChannel callbackByteChannel, FileOutputStream fileOutputStream, CountDownLatch countDownLatch) {
        this.callbackByteChannel = callbackByteChannel;
        this.fileOutputStream = fileOutputStream;
        this.countDownLatch = countDownLatch;
    }

    public void partialDownload() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            fileOutputStream.getChannel().transferFrom(callbackByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            log.error("Partial Download failed, {}", e.getMessage());
        } finally {
            countDownLatch.countDown();
        }
    }
}
