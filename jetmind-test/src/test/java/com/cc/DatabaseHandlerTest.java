package com.cc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

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
    }

    @Test
    public void shouldPopulateDbOnCreate() {
        User user = db.getUser(1);
        assertThat(user.getName(), equalTo("Igor"));
        assertThat(user.getSurname(), equalTo("Bondarenko"));
        assertThat(user.getBio(), equalTo("Working as a Python developer at 42 Coffee Cups"));
        List<Contact> contacts = user.getContacts();
        assertNotNull(contacts);
        assertThat(contacts.isEmpty(), is(false));
        Contact c = contacts.get(0);
        assertThat(c.getLabel(), equalTo("Email"));
        assertThat(c.getValue(), equalTo("jetmind2@gmail.com"));
        assertThat(c.getUser(), equalTo(user));
        c = contacts.get(1);
        assertThat(c.getLabel(), equalTo("Skype"));
        assertThat(c.getValue(), equalTo("jetmind"));
        assertThat(c.getUser(), equalTo(user));
    }

    @Test
    public void test_addUser() {
        User user = new User();
        user.setName("Darth");
        user.setSurname("Vader");
        String bio = "Born on Tatooine.\n" +
                "Was a jedi, known as Anakin Skywalker.\n" +
                "Has a son: Luke";
        user.setBio(bio);
        db.addUser(user);
        User userFromDb = db.getUser(2);
        assertThat(userFromDb.getName(), equalTo("Darth"));
        assertThat(userFromDb.getSurname(), equalTo("Vader"));
        assertThat(userFromDb.getBio(), equalTo(bio));
        assertNull(userFromDb.getBirth());
        assertThat(userFromDb.getContacts().isEmpty(), is(true));
    }

    @Test
    public void test_addContact() {
        User user = db.getUser(1);
        Contact c = new Contact(user, "Phone", "555-555-555");
        db.addContact(c);
        Contact contactFromDb = db.getUserContacts(user).get(2);
        assertThat(contactFromDb.getLabel(), equalTo("Phone"));
        assertThat(contactFromDb.getValue(), equalTo("555-555-555"));
    }

    @Test
    public void test_getUserContacts() {
        User user = new User();
        user.setName("Darth");
        user.setSurname("Vader");
        user.setBio("Born on Tatooine.\n" +
                "Was a jedi, known as Anakin Skywalker.\n" +
                "Has a son: Luke");
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
