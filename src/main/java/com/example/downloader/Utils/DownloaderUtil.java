package com.example.downloader.Utils;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.*;

public class DownloaderUtil {
    private DownloaderUtil() {
    }

    public static void mergePartialFiles(File destination, File[] sources) throws IOException {
        try (OutputStream output = createAppendableStream(destination)) {
            for (File source : sources) {
                appendFile(output, source);
            }
        }
    }

    public static void deleteParialFiles(File[] files) {
        for (File file : files) {
            FileUtils.deleteQuietly(file);
        }
    }

    public static String extractFileName(CloseableHttpResponse response, String url) {
        String fileName = "";
        Header disposition = response.getFirstHeader("Content-Disposition");
        if (disposition != null) {
            String value = disposition.getValue();
            int index = value.indexOf("filename=");
            if (index > 0) {
                fileName = value.substring(index + 10, value.length() - 1);
            }
        } else {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        }

        return fileName;
    }

    private static BufferedOutputStream createAppendableStream(File destination) throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(destination, true));
    }

    private static void appendFile(OutputStream output, File source) throws IOException {
        try (InputStream input = new BufferedInputStream(new FileInputStream(source))) {
            IOUtils.copy(input, output);
        }
    }


}
