package com.example.securefiletransfer.file_share.model;

public class UserModel {
    String email, id, username, image, phone;

    public UserModel(String email, String id, String username, String image, String phone) {
        this.email = email;
        this.id = id;
        this.username = username;
        this.image = image;
        this.phone = phone;
    }

    public UserModel() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
