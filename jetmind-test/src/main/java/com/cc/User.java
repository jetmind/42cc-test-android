package com.cc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class User {

    private long id;
    private String name;
    private String surname;
    private Date birth;
    private String bio;
    private List<Contact> contacts;

    private SimpleDateFormat displayFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
    private SimpleDateFormat dbFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirth() {
        return birth;
    }

    public String getBirthDisplay() {
        if (birth != null) {
            return displayFormat.format(birth);
        }
        return null;
    }

    public String getBirthDb() {
        if (birth != null) {
            return dbFormat.format(birth);
        }
        return null;
    }

    public void setBirth(String birth) throws ParseException {
        this.birth = dbFormat.parse(birth);
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

}
