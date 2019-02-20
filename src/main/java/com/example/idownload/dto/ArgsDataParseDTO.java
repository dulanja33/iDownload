package com.example.idownload.dto;


public class ArgsDataParseDTO {
    private String url = "";
    private String localPath = "";
    private String username = "";
    private String password = "";
    private int numOfConnections = -1;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNumOfConnections() {
        return numOfConnections;
    }

    public void setNumOfConnections(int numOfConnections) {
        this.numOfConnections = numOfConnections;
    }
}
