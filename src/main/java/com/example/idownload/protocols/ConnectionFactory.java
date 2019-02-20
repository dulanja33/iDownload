package com.example.idownload.protocols;


import com.example.idownload.dto.ArgsDataParseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionFactory {

    private static Logger log = LoggerFactory.getLogger(ConnectionFactory.class);


    public static Connection getConnection(ArgsDataParseDTO dto) {
        Connection connection = null;

        try {
            URL connectionUrl = new URL(dto.getUrl());
            String protocol = connectionUrl.getProtocol();

            switch (protocol.toUpperCase()) {
                case "HTTP":
                    connection = new HttpConnection(dto.getUrl());
                    break;
                case "HTTPS":
                    connection = new HttpConnection(dto.getUrl());
                    break;
                case "FTP":
                    connection = new FtpConnection(dto.getUrl(), dto.getUsername(), dto.getPassword());
                    break;
                case "SFTP":
                    connection = new FtpConnection(dto.getUrl(), dto.getUsername(), dto.getPassword());
                    break;
                default:
                    break;
            }

        } catch (MalformedURLException e) {
            log.error("url is not valid", e.getMessage());
        }

        return connection;

    }
}
