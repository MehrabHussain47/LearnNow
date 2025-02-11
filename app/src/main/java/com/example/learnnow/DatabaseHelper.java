package com.example.learnnow;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LearnNow.db";
    private static final int DATABASE_VERSION = 2; // Increment version

    // Table Name
    private static final String TABLE_USERS = "users";
    public static final String TABLE_COURSES = "courses";

    // Columns for user table
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FULLNAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role"; // New column for role

    //Column for course table
    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_COURSE_NAME = "course_name";
    public static final String COLUMN_INSTRUCTOR_NAME = "instructor_name";
    public static final String COLUMN_IMAGE_PATH = "image_path";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "Creating tables...");
        // Users Table
        String createTableQuery = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FULLNAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT)";
        db.execSQL(createTableQuery);
        Log.d("DatabaseHelper", "Users table created.");

        // Courses Table
        String createTableQuery2 = "CREATE TABLE " + TABLE_COURSES + " (" +
                COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COURSE_NAME + " TEXT, " +
                COLUMN_INSTRUCTOR_NAME + " TEXT, " +
                COLUMN_IMAGE_PATH + " TEXT)";
        db.execSQL(createTableQuery2);
        Log.d("DatabaseHelper", "Courses table created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
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

        // Prepare the update statement
        ContentValues contentValues = new ContentValues();
        contentValues.put("full_name", newFullName); // The column name and new value

        // Execute the update
        int rowsAffected = db.update("users", contentValues, "id = ?", new String[]{String.valueOf(userId)});
        db.close();

        return rowsAffected > 0; // Returns true if at least one row was updated
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

    public void insertCourse(String courseName, String instructorName, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_NAME, courseName);
        values.put(COLUMN_INSTRUCTOR_NAME, instructorName);
        values.put(COLUMN_IMAGE_PATH, imagePath);

        long result = db.insert(TABLE_COURSES, null, values);
        db.close();
    }

    // Get all courses from the courses table
    public List<CourseModel> getAllCourses() {
        List<CourseModel> courseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COURSES, null);

        if (cursor.moveToFirst()) {
            do {
                int courseId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID));
                String courseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_NAME));
                String instructorName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTOR_NAME));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH));

                courseList.add(new CourseModel(courseId, courseName, instructorName, imagePath));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return courseList;
    }

    // Get a course by its ID
    public CourseModel getCourseById(int courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSE_ID + "=?", new String[]{String.valueOf(courseId)});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID));
            String courseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_NAME));
            String instructorName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTOR_NAME));
            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH));
            cursor.close();
            return new CourseModel(id, courseName, instructorName, imagePath);
        }
        cursor.close();
        return null;
    }

    // Delete a course by its ID
    public boolean deleteCourse(int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_COURSES, COLUMN_COURSE_ID + "=?", new String[]{String.valueOf(courseId)});
        db.close();
        return rowsAffected > 0;
    }

    // Update course details by its ID
    public boolean updateCourse(int courseId, String courseName, String instructorName, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COURSE_NAME, courseName);
        contentValues.put(COLUMN_INSTRUCTOR_NAME, instructorName);
        contentValues.put(COLUMN_IMAGE_PATH, imagePath);

        int rowsAffected = db.update(TABLE_COURSES, contentValues, COLUMN_COURSE_ID + "=?", new String[]{String.valueOf(courseId)});
        db.close();
        return rowsAffected > 0;
    }

    // Check if a course exists by its name
    public boolean checkCourseExists(String courseName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSE_NAME + "=?", new String[]{courseName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


}
