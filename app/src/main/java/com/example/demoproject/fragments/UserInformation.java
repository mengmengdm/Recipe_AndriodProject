package com.example.demoproject.fragments;

import java.util.HashMap;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;



public class UserInformation {
    private int id;
    private String userName;
    private String emailAddress;
    private String hashedPassword;


    public UserInformation(String userName, String emailAddress, String password){
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.hashedPassword= hashPassword(password);
    }

    public String hashPassword(String password){
        //Generate Salt
        String salt = BCrypt.gensalt(10);
        //Return hashedPassword
        return BCrypt.hashpw(password,salt);
    }

    public Map<String,String> getPostParameters()
    {
        Map<String,String> params = new HashMap<>();
        params.put("username",userName);
        params.put("emailaddress",emailAddress);
        params.put("password",hashedPassword);
        return params;
    }
}
