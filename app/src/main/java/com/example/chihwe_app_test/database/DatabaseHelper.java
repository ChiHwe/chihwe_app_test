package com.example.chihwe_app_test.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.chihwe_app_test.database.modal.News;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "news_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(News.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + News.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertNews(String news) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(News.COLUMN_NEWS, news);

        // insert row
        long id = db.insert(News.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public String getnews(String news_title) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        String return_data = null;
        Cursor cursor = db.query(News.TABLE_NAME,
                new String[]{News.COLUMN_ID, News.COLUMN_NEWS, News.COLUMN_TIMESTAMP},
                News.COLUMN_NEWS + "=?",
                new String[]{news_title}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

            if (cursor.getCount()> 0)

                return_data = cursor.getString(cursor.getColumnIndex(News.COLUMN_NEWS));

            /*
            News news = new News(
                    cursor.getInt(cursor.getColumnIndex(News.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(News.COLUMN_NEWS)),
                    cursor.getString(cursor.getColumnIndex(News.COLUMN_TIMESTAMP)));
*/
            // close the db connection
            cursor.close();

            return return_data;

    }

    public List<News> getAllNews() {
        List<News> news_list = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + News.TABLE_NAME + " ORDER BY " +
                News.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                News news = new News();
                news.setId(cursor.getInt(cursor.getColumnIndex(News.COLUMN_ID)));
                news.setNews(cursor.getString(cursor.getColumnIndex(News.COLUMN_NEWS)));
                news.setTimestamp(cursor.getString(cursor.getColumnIndex(News.COLUMN_TIMESTAMP)));

                news_list.add(news);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        return news_list;
    }

    public int getNewscount() {
        String countQuery = "SELECT  * FROM " + News.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateNews(News news) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(News.COLUMN_NEWS, news.getNews());

        // updating row
        return db.update(News.TABLE_NAME, values, News.COLUMN_ID + " = ?",
                new String[]{String.valueOf(news.getId())});
    }

    public void deleteNews(News news) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(News.TABLE_NAME, News.COLUMN_ID + " = ?",
                new String[]{String.valueOf(news.getId())});
        db.close();
    }
}
