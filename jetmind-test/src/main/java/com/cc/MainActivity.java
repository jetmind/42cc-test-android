package com.cc;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class MainActivity extends FragmentActivity {

    private class FetchFromDb extends AsyncTask<Void, Void, User> {
        @Override
        protected User doInBackground(Void... params) {
            Log.i(TAG, "Fetching data from db");
            DatabaseHandler db = new DatabaseHandler(MainActivity.this);
            return db.getUser(USER_ID);
        }
        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                fio.setText(user.getName() + " " + user.getSurname());
                birth.setText(new SimpleDateFormat(
                        "MMMM dd, yyyy", Locale.getDefault()).format(user.getBirth()));
                bio.setText(user.getBio());
                StringBuilder sb = new StringBuilder();
                if (user.getContacts() != null) {
                    for (Contact c : user.getContacts()) {
                        sb.append(c.getLabel() + ": " + c.getValue() + "\n");
                    }
                }
                sb.deleteCharAt(sb.length() - 1);
                contacts.setText(sb.toString());
            }
            super.onPostExecute(user);
        }
    }

    private class FetchFromFacebook extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "Fetching data from facebook...");
            Response r = Request.executeAndWait(new Request(Session.getActiveSession(), "me"));
            GraphUser graphUser = r.getGraphObjectAs(GraphUser.class);

            Log.i(TAG, "Saving data to db");
            User user = new User();
            user.setName(graphUser.getFirstName());
            user.setSurname(graphUser.getLastName());
            //            user.setBirth(graphUser.getBirthday());
            user.setBio((String) graphUser.getProperty("bio"));
            Contact contact = new Contact(user, "Email", (String) graphUser.getProperty("email"));
            Contact contact2 = new Contact(user, "Birthday", graphUser.getBirthday());

            DatabaseHandler db = new DatabaseHandler(MainActivity.this);
            db.addUser(user);
            db.addContact(contact);
            db.addContact(contact2);
            db.close();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            new FetchFromDb().execute();
            super.onPostExecute(result);
        }
    }

    private static String TAG = "jetmind-test";
    private static long USER_ID = 1;

    private static final List<String> PERMISSIONS = Arrays.asList(
            "email", "user_birthday", "user_about_me");
    private Session.StatusCallback sessionStatusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private TextView fio;
    private TextView birth;
    private TextView bio;
    private TextView contacts;
    private ImageView photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fio = (TextView) findViewById(R.id.textFio);
        birth = (TextView) findViewById(R.id.textBirth);
        bio = (TextView) findViewById(R.id.textBio);
        contacts = (TextView) findViewById(R.id.textContacts);
        photo = (ImageView) findViewById(R.id.imagePhoto);
    }

    @Override
    public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        if (session == null || session.isClosed()) {
            Session.setActiveSession(new Session(MainActivity.this));
        }
        session = Session.getActiveSession();

        if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || session.isOpened()){
            new FetchFromDb().execute();
        } else {
            OpenRequest openRequest = new OpenRequest(MainActivity.this);
            openRequest.setPermissions(PERMISSIONS);
            openRequest.setCallback(sessionStatusCallback);
            session.openForRead(openRequest);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            new FetchFromFacebook().execute();
        }
    }
}

