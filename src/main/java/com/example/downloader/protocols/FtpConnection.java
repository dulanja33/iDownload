package com.example.downloader.protocols;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FtpConnection implements Connection {

    private static Logger log = LoggerFactory.getLogger(HttpConnection.class);
    private String URL;
    private String user;
    private String pass;
    private Long contentLength;
    private String fileName;

    public FtpConnection(String url, String user, String pass) {
        this.URL = url;
        this.user = user;
        this.pass = pass;
        getConnectionData();
    }


    private void getConnectionData() {
        FTPClient ftpClient = new FTPClient();
        try {
            java.net.URL url = new URL(this.URL);

            ftpClient.connect(url.getHost());
            ftpClient.login(this.user, this.pass);
            String filePath = url.getFile();
            this.fileName = filePath.substring(1).replaceAll("/", "_");
            ftpClient.sendCommand("SIZE", filePath);
            String[] results = ftpClient.getReplyString().split(" ");
            if(results.length > 1){
                String result = results[1].replaceAll("\r\n", "");
                this.contentLength = Long.parseLong(result);
            }

            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException e) {
            log.error("ftp connection failed, {}", e.getMessage());
        }

    }


    @Override
    public InputStream getInputStream(long start, long end) {
        FTPClient ftpClient = new FTPClient();
        try {
            java.net.URL url = new URL(this.URL);
            ftpClient.connect(url.getHost());
            ftpClient.login(this.user, this.pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String filePath = url.getFile();
            return ftpClient.retrieveFileStream(filePath);

        } catch (IOException e) {
            log.error("ftp connection failed, {}", e.getMessage());
        }

        return null;
    }

    @Override
    public boolean isResumable() {
        return false;
    }

    @Override
    public boolean isSupportRange() {
        return false;
    }

    @Override
    public long getContentLength() {
        return contentLength;
    }

    @Override
    public void setUrl(String url) {

    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setUserNamePassword(String user, String pass) {

    }
}
