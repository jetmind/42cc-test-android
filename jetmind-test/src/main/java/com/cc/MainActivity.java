package com.cc;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static String TAG = "jetmind-test";

    private static long USER_ID = 1;

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        DatabaseHandler db = new DatabaseHandler(this);
        User user = db.getUser(USER_ID);

        TextView textFio = (TextView) findViewById(R.id.textFio);
        textFio.setText(user.getName() + " " + user.getSurname());
        TextView textBirth = (TextView) findViewById(R.id.textBirth);
        textBirth.setText(
                new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(user.getBirth()));
        TextView textBio = (TextView) findViewById(R.id.textBio);
        textBio.setText(user.getBio());
        StringBuilder sb = new StringBuilder();
        if (user.getContacts() != null) {
            for (Contact c : user.getContacts()) {
                sb.append(c.getLabel() + ": " + c.getValue());
            }
        }
    }

}

