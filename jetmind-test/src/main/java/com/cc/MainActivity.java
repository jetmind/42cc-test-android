package com.cc;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity {

    private static String TAG = "jetmind-test";
    private static long USER_ID = 1;
    private LoginFragment loginFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            loginFragment = new LoginFragment();
            getSupportFragmentManager()
            .beginTransaction()
            .add(android.R.id.content, loginFragment)
            .commit();
        } else {
            loginFragment = (LoginFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }

        //        DatabaseHandler db = new DatabaseHandler(this);
        //        User user = db.getUser(USER_ID);
        //
        //        if (user != null) {
        //            TextView textFio = (TextView) findViewById(R.id.textFio);
        //            textFio.setText(user.getName() + " " + user.getSurname());
        //            TextView textBirth = (TextView) findViewById(R.id.textBirth);
        //            textBirth.setText(
        //                    new SimpleDateFormat(
        //                            "MMMM dd, yyyy", Locale.getDefault()).format(user.getBirth()));
        //            TextView textBio = (TextView) findViewById(R.id.textBio);
        //            textBio.setText(user.getBio());
        //            StringBuilder sb = new StringBuilder();
        //            if (user.getContacts() != null) {
        //                for (Contact c : user.getContacts()) {
        //                    sb.append(c.getLabel() + ": " + c.getValue() + "\n");
        //                }
        //            }
        //            sb.deleteCharAt(sb.length() - 1);
        //            TextView textContacts = (TextView) findViewById(R.id.textContacts);
        //            textContacts.setText(sb.toString());
        //        }
    }

}

