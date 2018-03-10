package ua.km.nashgorodok.database.dao;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ua.km.nashgorodok.database.models.Feed;

@Dao
public interface FeedDao {

    @Query("SELECT * FROM feeds")
    LiveData<List<Feed>> getAll();

    @Query("SELECT * FROM feeds WHERE post_url = :postUrl")
    Feed getFeedByPostUrl(String postUrl);

    @Query("SELECT * FROM feeds WHERE category IN (:category)")
    LiveData<List<Feed>> getListFeedsByCategory(String category);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Feed> feeds);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertFeed(Feed feed);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFeed(Feed feed);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAllFeeds(List<Feed> feeds);

    @Delete
    void delete(Feed feed);

    @Query("DELETE FROM feeds")
    public void clearTable();

}
