package com.example.downloader.protocols;


import com.example.downloader.Utils.DownloaderUtil;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class HttpConnection implements Connection {

    private static Logger log = LoggerFactory.getLogger(HttpConnection.class);

    private String URL;
    private boolean supportRange;
    private long contentLength;
    private String fileName;

    HttpConnection(String url) {
        this.URL = url;
        this.getConnectionData();
    }

    private void getConnectionData() {
        HttpHead httpGet = new HttpHead(this.URL);
        try (
                CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
                CloseableHttpResponse response = closeableHttpClient.execute(httpGet)
        ) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {

                Header acceptRangesHeader = response.getFirstHeader("Accept-Ranges");
                Header contentLengthHeader = response.getFirstHeader("Content-Length");
                supportRange = acceptRangesHeader != null && !acceptRangesHeader.getValue().equals("none");
                contentLength = contentLengthHeader != null ? Long.parseLong(contentLengthHeader.getValue()) : 0;

                fileName = DownloaderUtil.extractFileName(response, this.URL);
            } else {
                log.info("Connection failed for this URL {}", this.URL);
            }
        } catch (IOException e) {
            log.error("Connection error {}", e.getMessage());
        }
    }

    private InputStream getConnectionInputStream(long start, long end) {
        HttpGet httpGet = new HttpGet(this.URL);
        if (supportRange) {
            httpGet.addHeader("Range", "bytes=" + start + "-" + (end - 1));
        }
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        try {
            CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
            return response.getEntity().getContent();
        } catch (IOException e) {
            log.error("Connection error", e.getMessage());
        }
        return null;
    }

    @Override
    public InputStream getInputStream(long start, long end) {
        return getConnectionInputStream(start, end);
    }

    @Override
    public boolean isResumable() {
        return supportRange;
    }

    @Override
    public boolean isSupportRange() {
        return supportRange;
    }

    @Override
    public long getContentLength() {
        return contentLength;
    }

    @Override
    public void setUrl(String url) {
        this.URL = url;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public void setUserNamePassword(String user, String pass) {
      //nothing to implement here
    }
}
