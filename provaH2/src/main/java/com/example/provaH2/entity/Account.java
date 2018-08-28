package com.example.provaH2.entity;


import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Entity
public class Account {
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @Id @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String fullname;
    @Column(unique = true)
    private String email;
    private String password;
    @Lob
    private byte[] image;

    public Account(){}

    public Account(String fullName, String email, String password) {
        this.fullname = fullName;
        this.email = email;
        this.password = password;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullname;
    }

    public void setFullName(String fullName) {
        this.fullname = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", fullName='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
