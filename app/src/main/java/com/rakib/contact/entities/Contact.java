package com.rakib.contact.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity
public class Contact implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String number;
    private String email;
    private String photo;

    public Contact(String name, String number, String email, String photo) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.photo = photo;
    }
    @Ignore
    public Contact(long id, String name, String number, String email, String photo) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.email = email;
        this.photo = photo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
