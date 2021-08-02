package com.tunebrains.planner5dtest.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class CacheDb extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String CACHE_TABLE = "cache";
    public static final String COLUMN_MODE = "_mode";
    public static final String COLUMN_TIME = "_time";
    public static final String COLUMN_DATA = "_data";
    private static final String CREATE_CACHE_DB = "create table " + CACHE_TABLE + "("
            + COLUMN_MODE + " text primary key,"
            + COLUMN_DATA + " text,"
            + COLUMN_TIME + " int"

            + ")";

    public CacheDb(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory, VERSION);
    }

    public CacheDb(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, VERSION, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public CacheDb(@Nullable Context context, @Nullable String name, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, VERSION, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CACHE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
