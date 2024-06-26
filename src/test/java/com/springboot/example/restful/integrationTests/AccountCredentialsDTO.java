package com.springboot.example.restful.integrationTests;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AccountCredentialsDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public AccountCredentialsDTO() {}

    public AccountCredentialsDTO(String username, String password) {
        this.username = username;
        this.password = password;
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

}
