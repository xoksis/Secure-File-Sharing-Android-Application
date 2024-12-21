package com.example.securefiletransfer.file_share.model;

public class ReadwriteUserDetails {
    public String Email,Name,PhoneNo;
    public ReadwriteUserDetails(){

    };
    public ReadwriteUserDetails(String email, String username, String password, String phoneno, String dob, String gender) {
        Name = username;
        Email = email;
        PhoneNo = phoneno;
    }
}
