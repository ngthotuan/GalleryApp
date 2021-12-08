package com.example.galleryapp.database;

public class Config {
    public static final String DATABASE_NAME = "gallery_db";

    // Image Table
    public static final String TABLE_IMAGE = "imageTable";
    public static final String IMAGE_ID = "_id";
    public static final String IMAGE_NAME = "name";
    public static final String IMAGE_PATH = "path";
    public static final String IMAGE_SIZE = "size";
    public static final String IMAGE_TYPE = "type";
    public static final String IMAGE_URI = "uri";
    public static final String IMAGE_CREATED_DATE = "created_date";
    public static final String IMAGE_MODIFIED_DATE = "modified_date";
    public static final String IMAGE_FAVOURITE = "favourite";

    // Album Table
    public static final String TABLE_ALBUM = "albumTable";
    public static final String ALBUM_ID = "_id";
    public static final String ALBUM_NAME = "name";

    // Link Table
    public static final String TABLE_LINK = "linkTable";
    public static final String ALBUM_ID_FK = "album_id";
    public static final String IMAGE_ID_FK = "image_id";
    public static final String IMAGE_SUB_CONSTRAINT = "image_sub_constraint";

    // For general purpose key-value pair data
    public static final String TITLE = "title";
    public static final String CREATE_IMAGE = "create_image";
    public static final String UPDATE_IMAGE = "update_image";
    public static final String CREATE_ALBUM = "create_album";
    public static final String UPDATE_ALBUM = "update_album";

}
