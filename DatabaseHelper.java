package com.anish.collegeapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "collegeApp.db";
    private static final int DATABASE_VERSION = 2; // Incremented version for schema changes

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_COURSES = "courses";

    // User Table Columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_DEPARTMENT = "department";
    private static final String KEY_USER_STUDENT_ID = "student_id";
    private static final String KEY_USER_PHONE = "phone";
    private static final String KEY_USER_COURSE = "course";
    private static final String KEY_USER_SEMESTER = "semester";
    private static final String KEY_USER_CGPA = "cgpa";

    // Course Table Columns
    private static final String KEY_COURSE_ID = "id";
    private static final String KEY_COURSE_NAME = "name";
    private static final String KEY_COURSE_CODE = "code";
    private static final String KEY_COURSE_CREDITS = "credits";

    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_USER_NAME + " TEXT," +
                KEY_USER_EMAIL + " TEXT UNIQUE," +
                KEY_USER_PASSWORD + " TEXT," +
                KEY_USER_DEPARTMENT + " TEXT," +
                KEY_USER_STUDENT_ID + " TEXT," +
                KEY_USER_PHONE + " TEXT," +
                KEY_USER_COURSE + " TEXT," +
                KEY_USER_SEMESTER + " INTEGER," +
                KEY_USER_CGPA + " REAL" +
                ")";

        String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES +
                "(" +
                KEY_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_COURSE_NAME + " TEXT," +
                KEY_COURSE_CODE + " TEXT," +
                KEY_COURSE_CREDITS + " INTEGER" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_COURSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Migration from version 1 to 2 - add new columns
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + KEY_USER_PHONE + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + KEY_USER_COURSE + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + KEY_USER_SEMESTER + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + KEY_USER_CGPA + " REAL DEFAULT 0.0");
        }
    }

    // User CRUD Operations
    public long addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getName());
        values.put(KEY_USER_EMAIL, user.getEmail());
        values.put(KEY_USER_PASSWORD, user.getPassword());
        values.put(KEY_USER_DEPARTMENT, user.getDepartment());
        values.put(KEY_USER_STUDENT_ID, user.getStudentId());
        values.put(KEY_USER_PHONE, user.getPhone());

        return db.insert(TABLE_USERS, null, values);
    }

    public User getUser(String email) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{
                        KEY_USER_ID,
                        KEY_USER_NAME,
                        KEY_USER_EMAIL,
                        KEY_USER_PASSWORD,
                        KEY_USER_DEPARTMENT,
                        KEY_USER_STUDENT_ID,
                        KEY_USER_PHONE,
                        KEY_USER_COURSE,
                        KEY_USER_SEMESTER,
                        KEY_USER_CGPA
                },
                KEY_USER_EMAIL + " = ?",
                new String[]{email},
                null, null, null);

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") User user = new User(
                    cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD)),
                    cursor.getString(cursor.getColumnIndex(KEY_USER_DEPARTMENT)),
                    cursor.getString(cursor.getColumnIndex(KEY_USER_STUDENT_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_USER_PHONE)));
            cursor.close();
            return user;
        }
        return null;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getName());
        values.put(KEY_USER_PASSWORD, user.getPassword());
        values.put(KEY_USER_DEPARTMENT, user.getDepartment());
        values.put(KEY_USER_STUDENT_ID, user.getStudentId());
        values.put(KEY_USER_PHONE, user.getPhone());

        return db.update(TABLE_USERS, values, KEY_USER_EMAIL + " = ?",
                new String[]{user.getEmail()});
    }

    // Course CRUD Operations would go here
    // ...
}