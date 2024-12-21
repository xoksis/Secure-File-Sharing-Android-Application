package com.example.securefiletransfer.file_share.model;

public class FileShared {
    String fileUrl, id, username, sender, filename, key;

    public FileShared() {
    }

    public FileShared(String fileUrl, String id, String username, String sender, String filename, String key) {
        this.fileUrl = fileUrl;
        this.id = id;
        this.username = username;
        this.sender = sender;
        this.filename = filename;
        this.key = key;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
