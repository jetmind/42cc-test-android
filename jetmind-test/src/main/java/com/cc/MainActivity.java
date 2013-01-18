package com.cc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class MainActivity extends SherlockActivity {

    private class FetchFromDb extends AsyncTask<Void, Void, User> {
        @Override
        protected User doInBackground(Void... params) {
            Log.i(TAG, "Fetching data from db");
            DatabaseHandler db = new DatabaseHandler(MainActivity.this);
            User user = db.getUser(USER_ID);
            db.close();
            return user;
        }
        @Override
        protected void onPostExecute(User user) {
            Log.i(TAG, "Fetching user data from local storage");
            if (user != null) {
                fio.setText(user.getName() + " " + user.getSurname());
                birth.setText(user.getBirthDisplay());
                bio.setText(user.getBio());
                StringBuilder sb = new StringBuilder();
                if (user.getContacts() != null) {
                    for (Contact c : user.getContacts()) {
                        sb.append(c.getLabel() + ": " + c.getValue() + "\n");
                    }
                }
                sb.deleteCharAt(sb.length() - 1);
                contacts.setText(sb.toString());

                InputStream in = null;
                try {
                    in = openFileInput(PHOTO_FILENAME);
                    photo.setImageBitmap(BitmapFactory.decodeStream(in));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Can't find profile picture in the local storage");
                } finally {
                    try {
                        if (in != null) { in.close(); }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
            // fetch profile picture
            URL avaUrl = null;
            InputStream in = null;
            OutputStream out = null;
            try {
                avaUrl = new URL("http://graph.facebook.com/" + graphUser.getId() + "/picture?type=normal");
                in = avaUrl.openConnection().getInputStream();
                out = openFileOutput(PHOTO_FILENAME, Context.MODE_PRIVATE);
                byte[] buffer = new byte[4096];
                while (in.read(buffer) >= 0) {
                    out.write(buffer);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d(TAG, "Bad profile picture url");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Can't upload profile picture");
            } finally {
                try {
                    if (in != null) { in.close(); }
                    if (out != null) { out.close(); }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Log.i(TAG, "Saving data to db");
            User user = new User();
            user.setId(USER_ID);
            user.setName(graphUser.getFirstName());
            user.setSurname(graphUser.getLastName());
            user.setBio(graphUser.getProperty("bio").toString());
            try {
                user.setBirth(graphUser.getBirthday());
            } catch (java.text.ParseException e) {
                e.printStackTrace();
                Log.i(TAG, "Can't parse date");
            }
            Contact contact = new Contact(user, "Email", graphUser.getProperty("email").toString());

            DatabaseHandler db = new DatabaseHandler(MainActivity.this);
            db.clean();
            db.addUser(user);
            db.addContact(contact);
            db.close();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            new FetchFromDb().execute();
            super.onPostExecute(result);
        }
    }

    private static final String TAG = "jetmind-test";
    private static final long USER_ID = 1;
    private static final String PHOTO_FILENAME = "profile_photo.jpg";

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, EditActivity.class));
        return super.onOptionsItemSelected(item);
    }
}

