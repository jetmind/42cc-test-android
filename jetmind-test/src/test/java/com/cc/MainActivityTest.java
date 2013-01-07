package com.cc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private MainActivity activity;
    private TextView textFio;
    private TextView textBirth;
    private TextView textContacts;
    private TextView textBio;
    private ImageView imagePhoto;

    @Before
    public void setUp() throws Exception {
        activity = new MainActivity();
        activity.onCreate(null);
        textFio = (TextView) activity.findViewById(R.id.textFio);
        textBirth = (TextView) activity.findViewById(R.id.textBirth);
        textContacts = (TextView) activity.findViewById(R.id.textContacts);
        textBio = (TextView) activity.findViewById(R.id.textBio);
        imagePhoto = (ImageView) activity.findViewById(R.id.imagePhoto);
    }

    @Test
    public void widgetsAreVisible() {
        assertThat(textFio.getVisibility(), equalTo(View.VISIBLE));
        assertThat(textBirth.getVisibility(), equalTo(View.VISIBLE));
        assertThat(textContacts.getVisibility(), equalTo(View.VISIBLE));
        assertThat(textBio.getVisibility(), equalTo(View.VISIBLE));
        assertThat(imagePhoto.getVisibility(), equalTo(View.VISIBLE));
    }

    @Test
    public void shouldShowUserInfo() {
        assertThat(textFio.getText().toString(), equalTo("Igor Bondarenko"));
        assertThat(textBirth.getText().toString(), equalTo("March 29, 1988"));
        String expected = "Email: jetmind2@gmail.com\nSkype: jetmind";
        assertThat(textContacts.getText().toString(), equalToIgnoringWhiteSpace(expected));
        expected = "Working as a Python developer at 42 Coffee Cups";
        assertThat(textBio.getText().toString(), equalToIgnoringWhiteSpace(expected));
    }

}