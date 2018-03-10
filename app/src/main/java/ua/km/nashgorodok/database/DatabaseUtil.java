package ua.km.nashgorodok.database;


import android.arch.persistence.room.Room;
import android.content.Context;

import ua.km.nashgorodok.database.dao.FeedDao;

public class DatabaseUtil {

    private static AppDatabase db;

    public static FeedDao getFeedDao(Context ctx){
        if (db == null) {
            db = Room.databaseBuilder(ctx, AppDatabase.class, DatabaseConstants.NAME_DATABASE)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return db.userDao();
    }

}
