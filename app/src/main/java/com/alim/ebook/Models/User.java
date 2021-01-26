package com.alim.ebook.Models;

public class User {
    private String user;
    private String email;
    public User() {
    }

    public String getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public User setUser(String user){
        this.user = user;
        return this;
    }

    public User setEmail(String email){
        this.email = email;
        return this;
    }

}
