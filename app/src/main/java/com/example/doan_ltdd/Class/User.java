package com.example.doan_ltdd.Class;

public class User {

    public String userId;
    public String urlImage;
    public String username;
    public String name;
    public String email;
    public String phone;
    public String password;
    public String address;

    public User() {
    }

    public User(String userId, String urlImage, String username, String name, String email, String phone, String password, String address) {
        this.userId = userId;
        this.urlImage = urlImage;
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
    }
}
