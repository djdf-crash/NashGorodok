package ua.km.nashgorodok.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import ua.km.nashgorodok.database.dao.FeedDao;
import ua.km.nashgorodok.database.models.Feed;
import ua.km.nashgorodok.utils.DateTypeConverter;


@Database(entities = {Feed.class}, version = 4, exportSchema = false)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract FeedDao userDao();
}
