package com.example.provaH2.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

@Entity
public class Account {
    @Id
   // private String id;
    private String fullName;
    @Column(unique = true)
    private String email;
    private String password;

    public Account(){}

    public Account(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
/*
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
*/
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
              //  "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
