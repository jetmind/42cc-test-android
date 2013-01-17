package com.cc;

public class Contact {

    private long id;
    private String label;
    private String value;
    private User user;

    public Contact(User user) {
        super();
        this.user = user;
    }

    public Contact(User user, String label, String value) {
        super();
        this.label = label;
        this.value = value;
        this.user = user;
        this.id = user.getId();
    }

    public Contact(User user, String label, String value, long id) {
        super();
        this.label = label;
        this.value = value;
        this.user = user;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
