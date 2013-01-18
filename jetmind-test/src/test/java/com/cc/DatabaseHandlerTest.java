package com.cc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DatabaseHandlerTest {

    private DatabaseHandler db;

    @Before
    public void setUp() {
        db = new DatabaseHandler(new MainActivity());
        User user = new User();
        user.setName("Test");
        user.setSurname("User");
        user.setBio("Test bio");
        db.addUser(user);
    }

    @Test
    public void test_addUser() throws ParseException {
        User user = new User();
        user.setName("Darth");
        user.setSurname("Vader");
        String bio = "Born on Tatooine.\n" +
                "Was a jedi, known as Anakin Skywalker.\n" +
                "Has a son: Luke";
        user.setBio(bio);
        user.setBirth("01/01/2222");
        db.addUser(user);
        User userFromDb = db.getUser(2);
        assertThat(userFromDb.getName(), equalTo("Darth"));
        assertThat(userFromDb.getSurname(), equalTo("Vader"));
        assertThat(userFromDb.getBio(), equalTo(bio));
        assertThat(userFromDb.getBirthDisplay(), equalTo("January 01, 2222"));
        assertThat(userFromDb.getContacts().isEmpty(), is(true));
    }

    @Test
    public void test_addContact() {
        User user = db.getUser(1);
        Contact c = new Contact(user, "Phone", "555-555-555");
        db.addContact(c);
        Contact contactFromDb = db.getUserContacts(user).get(0);
        assertThat(contactFromDb.getLabel(), equalTo("Phone"));
        assertThat(contactFromDb.getValue(), equalTo("555-555-555"));
    }

    @Test
    public void test_getUserContacts() {
        User user = new User();
        user.setName("Darth");
        user.setSurname("Vader");
        db.addUser(user);
        db.addContact(new Contact(user, "Email", "darth.vader@deathstar.org"));
        db.addContact(new Contact(user, "Skype", "darth.vader"));
        List<Contact> contacts = db.getUserContacts(user);
        Contact c = contacts.get(0);
        assertThat(c.getLabel(), equalTo("Email"));
        assertThat(c.getValue(), equalTo("darth.vader@deathstar.org"));
        c = contacts.get(1);
        assertThat(c.getLabel(), equalTo("Skype"));
        assertThat(c.getValue(), equalTo("darth.vader"));
    }

}
