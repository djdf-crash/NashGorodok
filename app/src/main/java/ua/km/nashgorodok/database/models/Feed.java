package ua.km.nashgorodok.database.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import ua.km.nashgorodok.database.DatabaseConstants;

@Entity(tableName = DatabaseConstants.FEED_TABLE)
public class Feed {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DatabaseConstants.FEED_ID)
    private int id;

    @ColumnInfo(name = DatabaseConstants.FEED_TITLE)
    private String title;

    @ColumnInfo(name = DatabaseConstants.FEED_CATEGORY)
    private String category;

    @ColumnInfo(name = DatabaseConstants.FEED_BODY)
    private String body;

    @ColumnInfo(name = DatabaseConstants.FEED_POST_URL)
    private String postUrl;

    @ColumnInfo(name = DatabaseConstants.FEED_POST_AUTHOR)
    private String author;

    @ColumnInfo(name = DatabaseConstants.FEED_THUMBNAIL_URL)
    private String thumbnailUrl;

    @ColumnInfo(name = DatabaseConstants.FEED_PUB_DATE)
    private String pubDate;

    @ColumnInfo(name = DatabaseConstants.FEED_IS_NEW)
    private boolean isNew;

    public Feed(String title, String body, String author, String postUrl, String thumbnailUrl, String pubDate, String category, boolean isNew) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.postUrl = postUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.pubDate = pubDate;
        this.category = category;
        this.isNew = isNew;
    }

    public int getId() {
        return id;
    }

    public void setId(int uid) {
        this.id = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean aNew) {
        isNew = aNew;
    }
}
