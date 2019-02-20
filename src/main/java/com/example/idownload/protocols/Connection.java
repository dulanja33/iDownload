package com.example.idownload.protocols;


import java.io.InputStream;

public interface Connection {
    InputStream getInputStream(long start, long end);

    boolean isResumable();

    boolean isSupportRange();

    long getContentLength();

    void setUrl(String url);

    String getFileName();

    void setUserNamePassword(String user, String pass);

}
