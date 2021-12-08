package com.example.galleryapp.database;

import static com.example.galleryapp.database.Config.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static DatabaseHelper databaseHelper;

    private static final String DATABASE_NAME = Config.DATABASE_NAME;
    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper() {
        super(mContext.context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance() {
        if (databaseHelper == null){
            synchronized (DatabaseHelper.class) {
                if (databaseHelper == null) {
                    databaseHelper = new DatabaseHelper();
                }
            }
        }

        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table SQL
        String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_IMAGE + "("
                + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IMAGE_PATH + " TEXT NOT NULL, "
                + IMAGE_NAME + " TEXT, "
                + IMAGE_SIZE + " INTEGER NOT NULL, "
                + IMAGE_TYPE + " TEXT NOT NULL, "
                + IMAGE_URI + " TEXT, "
                + IMAGE_CREATED_DATE + " INTEGER, "
                + IMAGE_MODIFIED_DATE + " INTEGER, "
                + IMAGE_FAVOURITE + "INTEGER NOT NULL"
                + ")";

        String CREATE_ALBUM_TABLE = "CREATE TABLE " + TABLE_ALBUM + "("
                + ALBUM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ALBUM_NAME + " TEXT NOT NULL"
                + ")";

        String CREATE_LINK_TABLE = "CREATE TABLE " + TABLE_LINK + "("
                + IMAGE_ID_FK + " INTEGER NOT NULL, "
                + ALBUM_ID_FK + " INTEGER NOT NULL, "
                + "FOREIGN KEY (" + IMAGE_ID_FK + ") REFERENCES " + TABLE_IMAGE + "(" + IMAGE_ID + ") ON UPDATE CASCADE ON DELETE CASCADE, "
                + "FOREIGN KEY (" + ALBUM_ID_FK + ") REFERENCES " + TABLE_ALBUM + "(" + ALBUM_ID + ") ON UPDATE CASCADE ON DELETE CASCADE, "
                + "CONSTRAINT " + IMAGE_SUB_CONSTRAINT + " UNIQUE (" + IMAGE_ID_FK + "," + ALBUM_ID_FK + ")"
                + ")";

        db.execSQL(CREATE_IMAGE_TABLE);
        db.execSQL(CREATE_ALBUM_TABLE);
        db.execSQL(CREATE_LINK_TABLE);

        Log.d(TAG, "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINK);

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
