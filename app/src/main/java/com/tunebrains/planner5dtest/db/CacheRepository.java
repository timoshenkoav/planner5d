package com.tunebrains.planner5dtest.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CacheRepository {
    private final CacheDb cacheDb;
    private final long ttl;

    public CacheRepository(CacheDb cacheDb, long ttl) {
        this.cacheDb = cacheDb;
        this.ttl = ttl;
    }

    synchronized public void store(String mode, String data) {
        final SQLiteDatabase db = cacheDb.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(CacheDb.COLUMN_DATA, data);
        values.put(CacheDb.COLUMN_MODE, mode);
        values.put(CacheDb.COLUMN_TIME, System.currentTimeMillis());
        db.insertWithOnConflict(CacheDb.CACHE_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    synchronized public String load(String mode) {
        final SQLiteDatabase db = cacheDb.getReadableDatabase();
        Cursor c = null;
        String data = null;
        try {
            c = db.query(CacheDb.CACHE_TABLE, null, String.format("%s=? and %s > ?", CacheDb.COLUMN_MODE, CacheDb.COLUMN_TIME), new String[]{
                    mode,
                    String.valueOf(System.currentTimeMillis() - ttl)
            }, null, null, null);
            if (c.moveToFirst()) {
                data = c.getString(c.getColumnIndex(CacheDb.COLUMN_DATA));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
        return data;
    }
}
