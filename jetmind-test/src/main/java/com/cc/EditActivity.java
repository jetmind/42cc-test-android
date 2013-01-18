package com.cc;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class EditActivity extends Activity {

    private class PopulateFieldsFromDb extends AsyncTask<Void, Void, User> {
        @Override
        protected User doInBackground(Void... params) {
            Log.i(TAG, "Fetching data from db");
            DatabaseHandler db = new DatabaseHandler(EditActivity.this);
            User user = db.getUser(USER_ID);
            db.close();
            return user;
        }
        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                name.setText(user.getName());
                surname.setText(user.getSurname());
                birth.setText(user.getBirthDb());
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

    private static final String TAG = "EditActivity";
    private static final long USER_ID = 1;

    private EditText name;
    private EditText surname;
    private EditText birth;
    private EditText bio;
    private EditText contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        name = (EditText) findViewById(R.id.editName);
        surname = (EditText) findViewById(R.id.editSurname);
        birth = (EditText) findViewById(R.id.editBirth);
        bio = (EditText) findViewById(R.id.editBio);
        contacts = (EditText) findViewById(R.id.editContacts);

        new PopulateFieldsFromDb().execute();
    }

    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        super.onCreateOptionsMenu(menu);
    //        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
    //        return true;
    //    }

}
