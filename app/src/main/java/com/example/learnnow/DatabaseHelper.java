package com.example.learnnow;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LearnNow.db";
    private static final int DATABASE_VERSION = 2; // Increment version

    // Table Name
    private static final String TABLE_USERS = "users";

    // Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FULLNAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role"; // New column for role

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table with new "role" column
        String createTableQuery = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FULLNAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT)"; // Add the role column here
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Create the table again with the new schema
        onCreate(db);
    }

    // Insert User with Role
    public boolean insertUser(String fullName, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAME, fullName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role); // Store the role

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1; // If result is -1, insertion failed
    }

    public List<UserModel> getAllUsers() {
        List<UserModel> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULLNAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE));

                userList.add(new UserModel(id, fullName, email, role));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

    public UserModel getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + "=?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULLNAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE));
            cursor.close();
            return new UserModel(id, fullName, email, role);
        }
        cursor.close();
        return null;
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USERS, COLUMN_ID + "=?", new String[]{String.valueOf(userId)}) > 0;
    }

    public boolean updateUserFullName(int userId, String newFullName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAME, newFullName);

        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
        return rowsAffected > 0;
    }


    // Check if the email already exists in the database
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    // Check if email and password are valid
    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Check if email, password and role match
    public boolean checkEmailPasswordRole(String email, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=? AND " + COLUMN_ROLE + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password, role});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

}
