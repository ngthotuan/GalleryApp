package com.example.galleryapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static DatabaseHelper databaseHelper;

    private static final String DATABASE_NAME = Config.DATABASE_NAME;
    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null){
            synchronized (DatabaseHelper.class) {
                if (databaseHelper == null) {
                    databaseHelper = new DatabaseHelper(context);
                }
            }
        }

        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table SQL
        String CREATE_IMAGE_TABLE = "CREATE TABLE " + Config.TABLE_IMAGE + "("
                + Config.COLUMN_IMAGE_PATH + " TEXT PRIMARY KEY, "
                + Config.COLUMN_IMAGE_NAME + " TEXT, "
                + Config.COLUMN_IMAGE_SIZE + " INTEGER NOT NULL, "
                + Config.COLUMN_IMAGE_TYPE + " TEXT NOT NULL, "
                + Config.COLUMN_IMAGE_URI + " TEXT NOT NULL, "
                + Config.COLUMN_IMAGE_CREATED_DATE + " INTEGER, "
                + Config.COLUMN_IMAGE_MODIFIED_DATE + " INTEGER, "
                + Config.COLUMN_IMAGE_ALBUM + " TEXT, "
                + Config.COLUMN_IMAGE_FAVOURITE + "INTEGER NOT NULL"
                + ")";

        db.execSQL(CREATE_IMAGE_TABLE);
        Log.d(TAG, "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_IMAGE);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        //enable foreign key constraints like ON UPDATE CASCADE, ON DELETE CASCADE
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
}
