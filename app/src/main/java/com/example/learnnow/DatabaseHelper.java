package com.example.learnnow;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LearnNow.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "users";
    public static final String TABLE_COURSES = "courses";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FULLNAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";

    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_COURSE_NAME = "course_name";
    public static final String COLUMN_INSTRUCTOR_NAME = "instructor_name";
    public static final String COLUMN_IMAGE_BLOB = "image_blob";

    public static final String TABLE_REQUESTS = "requests";
    public static final String COLUMN_REQUEST_ID = "request_id";
    public static final String COLUMN_REQUEST_STUDENT = "student_fullname";
    public static final String COLUMN_REQUEST_COURSE = "course_name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "Creating tables...");

        String createTableQuery = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FULLNAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT)";
        db.execSQL(createTableQuery);
        Log.d("DatabaseHelper", "Users table created.");

        String createTableQuery2 = "CREATE TABLE " + TABLE_COURSES + " (" +
                COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COURSE_NAME + " TEXT, " +
                COLUMN_INSTRUCTOR_NAME + " TEXT, " +
                COLUMN_IMAGE_BLOB + " BLOB)";
        db.execSQL(createTableQuery2);
        Log.d("DatabaseHelper", "Courses table created.");

        String createRequestTableQuery = "CREATE TABLE " + TABLE_REQUESTS + " (" +
                COLUMN_REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REQUEST_STUDENT + " TEXT, " +
                COLUMN_REQUEST_COURSE + " TEXT)";
        db.execSQL(createRequestTableQuery);
        Log.d("DatabaseHelper", "Requests table created.");
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);
        onCreate(db);
    }

    public boolean insertUser(String fullName, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAME, fullName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
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

        ContentValues contentValues = new ContentValues();
        contentValues.put("full_name", newFullName);

        int rowsAffected = db.update("users", contentValues, "id = ?", new String[]{String.valueOf(userId)});
        db.close();

        return rowsAffected > 0;
    }

    public boolean insertRequest(String studentFullName, String courseName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REQUEST_STUDENT, studentFullName);
        values.put(COLUMN_REQUEST_COURSE, courseName);
        long result = db.insert(TABLE_REQUESTS, null, values);
        db.close();
        return result != -1;
    }

    public List<RequestModel> getAllRequests() {
        List<RequestModel> requestList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REQUESTS, null);
        if (cursor.moveToFirst()) {
            do {
                int requestId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ID));
                String studentFullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_STUDENT));
                String courseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_COURSE));
                requestList.add(new RequestModel(requestId, studentFullName, courseName));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return requestList;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkEmailPasswordRole(String email, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=? AND " + COLUMN_ROLE + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password, role});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }



    public boolean insertCourse(String courseName, String instructorName, Bitmap image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_NAME, courseName);
        values.put(COLUMN_INSTRUCTOR_NAME, instructorName);
        values.put(COLUMN_IMAGE_BLOB, convertBitmapToByteArray(image));

        long result = db.insert(TABLE_COURSES, null, values);
        db.close();

        return result != -1;
    }



    public List<CourseModel> getAllCourses() {
        List<CourseModel> courseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COURSES, null);

        if (cursor.moveToFirst()) {
            do {
                int courseId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID));
                String courseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_NAME));
                String instructorName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTOR_NAME));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_BLOB));
                Bitmap image = convertByteArrayToBitmap(imageBytes);

                courseList.add(new CourseModel(courseId, courseName, instructorName, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return courseList;
    }

    private Bitmap convertByteArrayToBitmap(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public boolean deleteCourse(int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_COURSES, COLUMN_COURSE_ID + "=?", new String[]{String.valueOf(courseId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean updateCourse(int courseId, String courseName, String instructorName, Bitmap image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COURSE_NAME, courseName);
        contentValues.put(COLUMN_INSTRUCTOR_NAME, instructorName);
        contentValues.put(COLUMN_IMAGE_BLOB, convertBitmapToByteArray(image));

        int rowsAffected = db.update(TABLE_COURSES, contentValues, COLUMN_COURSE_ID + "=?", new String[]{String.valueOf(courseId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean checkCourseExists(String courseName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSE_NAME + "=?", new String[]{courseName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


}
