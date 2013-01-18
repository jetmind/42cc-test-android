package com.cc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.view.View;
import android.widget.EditText;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class EditActivityTest {

    private EditActivity activity;
    private EditText editName;
    private EditText editSurname;
    private EditText editBirth;
    private EditText editContacts;
    private EditText editBio;

    @Before
    public void setUp() throws Exception {
        activity = new EditActivity();

        // create test user with contacts
        User user = new User();
        user.setName("Darth");
        user.setSurname("Vader");
        user.setBirth("03/29/3113");
        user.setBio("Multiline\nbio");
        DatabaseHandler db = new DatabaseHandler(activity);
        db.addUser(user);
        db.addContact(new Contact(user, "Email", "darth@sith.org"));
        db.addContact(new Contact(user, "Skype", "darth.vader"));

        activity.onCreate(null);

        editName = (EditText) activity.findViewById(R.id.editName);
        editSurname = (EditText) activity.findViewById(R.id.editSurname);
        editBirth = (EditText) activity.findViewById(R.id.editBirth);
        editContacts = (EditText) activity.findViewById(R.id.editContacts);
        editBio = (EditText) activity.findViewById(R.id.editBio);
    }

    @Test
    public void shouldSeeEditWidgets() {
        assertThat(editName.getVisibility(), equalTo(View.VISIBLE));
        assertThat(editSurname.getVisibility(), equalTo(View.VISIBLE));
        assertThat(editBirth.getVisibility(), equalTo(View.VISIBLE));
        assertThat(editBio.getVisibility(), equalTo(View.VISIBLE));
        assertThat(editContacts.getVisibility(), equalTo(View.VISIBLE));
    }

    @Test
    public void shouldSeeCurrentName() {
        assertThat(editName.getText().toString(), equalTo("Darth"));
    }

    @Test
    public void shouldSeeCurrentSurname() {
        assertThat(editSurname.getText().toString(), equalTo("Vader"));
    }

    @Test
    public void shouldSeeCurrentBirth() {
        assertThat(editBirth.getText().toString(), equalTo("03/29/3113"));
    }

    @Test
    public void shouldSeeCurrentBio() {
        assertThat(editBio.getText().toString(), equalTo("Multiline\nbio"));
    }

    @Test
    public void shouldSeeCurrentContacts() {
        String expected = "Email: darth@sith.org";
        expected += "\nSkype: darth.vader";
        assertThat(editContacts.getText().toString(), equalTo(expected));
    }

}
