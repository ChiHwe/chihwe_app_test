package com.example.chihwe_app_test.database.modal;

public class News {
    public static final String TABLE_NAME = "news";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NEWS = "news";
    public static final String COLUMN_NEWS_CONTENT = "content";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String news;
    private String timestamp;
    private String contents;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NEWS + " TEXT,"
                    + COLUMN_NEWS_CONTENT + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public News() {
    }

    public News(int id, String news,String contents, String timestamp) {
        this.id = id;
        this.news = news;
        this.contents = contents;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getNews() {
        return news;
    }

    public String getNews_content() {
        return contents;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}