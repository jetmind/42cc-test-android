package com.cc;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandler";

    // db info
    private static final String DB_NAME = "userinfo";
    private static final int DB_VERSION = 1;
    private static final String TABLE_USER = "user";
    private static final String TABLE_CONTACT = "contact";

    // tables info
    private static final String USER_ID = "id";
    private static final String USER_NAME = "name";
    private static final String USER_SURNAME = "surname";
    private static final String USER_BIRTH = "birth";
    private static final String USER_BIO = "bio";

    private static final String CONTACT_ID = "id";
    private static final String CONTACT_USER_ID = "user_id";
    private static final String CONTACT_LABEL = "label";
    private static final String CONTACT_VALUE = "value";

    // create sql
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "(" +
            USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            USER_NAME + " TEXT," +
            USER_SURNAME + " TEXT," +
            USER_BIRTH + " TEXT," +
            USER_BIO + " TEXT" + ")";

    private static final String CREATE_TABLE_CONTACT = "CREATE TABLE " + TABLE_CONTACT + "(" +
            CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            CONTACT_USER_ID + " INTEGER," +
            CONTACT_LABEL + " TEXT," +
            CONTACT_VALUE + " TEXT," +
            "FOREIGN KEY(" + CONTACT_USER_ID + ")" +
            " REFERENCES " + TABLE_USER + "(" + USER_ID + ")" + ")";

    // drop sql
    private static final String DROP_TABLE_CONTACT = "DROP TABLE IF EXISTS " + TABLE_CONTACT;
    private static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS " + TABLE_USER;

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating db " + DB_NAME);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,
                "Updating db from version " + oldVersion + " to version " + newVersion +
                ". All existing data will be destroyed.");
        db.execSQL(DROP_TABLE_CONTACT);
        db.execSQL(DROP_TABLE_USER);
        onCreate(db);
    }

    public void addUser(User user) {
        addUser(user, null);
    }

    protected void addUser(User user, SQLiteDatabase db0) {
        SQLiteDatabase db = db0;
        if (db == null) {
            db = this.getWritableDatabase();
        }
        ContentValues values = new ContentValues();
        values.put(USER_NAME, user.getName());
        values.put(USER_SURNAME, user.getSurname());
        values.put(USER_BIO, user.getBio());
        values.put(USER_BIRTH, user.getBirthDb());
        long id = db.insert(TABLE_USER, null, values);
        user.setId(id);
    }

    public void addContact(Contact contact) {
        addContact(contact, null);
    }

    protected void addContact(Contact contact, SQLiteDatabase db0) {
        SQLiteDatabase db = db0;
        if (db == null) {
            db = this.getWritableDatabase();
        }
        ContentValues values = new ContentValues();
        values.put(CONTACT_LABEL, contact.getLabel());
        values.put(CONTACT_VALUE, contact.getValue());
        values.put(CONTACT_USER_ID, contact.getUser().getId());
        long id = db.insert(TABLE_CONTACT, null, values);
        contact.setId(id);
    }

    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER,
                new String[] {USER_NAME, USER_SURNAME, USER_BIRTH, USER_BIO},
                USER_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setId(id);
            user.setName(cursor.getString(0));
            user.setSurname(cursor.getString(1));
            if (cursor.getString(2) != null) {
                try {
                    user.setBirth(cursor.getString(2));
                } catch (ParseException e) {
                    Log.d(TAG, "Can't parse date " + cursor.getString(2));
                    return null;
                }
            }

            user.setBio(cursor.getString(3));
            user.setContacts(this.getUserContacts(user));
            return user;
        }
        return null;
    }

    public List<Contact> getUserContacts(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACT,
                new String[] {CONTACT_ID, CONTACT_LABEL, CONTACT_VALUE},
                CONTACT_USER_ID + "=?",
                new String[] {String.valueOf(user.getId())}, null, null, null, null);
        List<Contact> contacts = new LinkedList<Contact>();
        while (cursor != null && cursor.moveToNext()) {
            long cid = cursor.getLong(0);
            String label = cursor.getString(1);
            String value = cursor.getString(2);
            contacts.add(new Contact(user, label, value, cid));
        }
        return contacts;
    }

    public void clean() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT, null, null);
        db.delete(TABLE_USER, null, null);
    }

}
