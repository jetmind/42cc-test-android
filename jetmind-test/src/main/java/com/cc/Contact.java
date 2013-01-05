package com.cc;

public class Contact {

    private String label;
    private String value;
    private User user;

    public Contact(User user) {
        super();
        this.user = user;
    }

    public Contact(String label, String value, User user) {
        super();
        this.label = label;
        this.value = value;
        this.user = user;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
