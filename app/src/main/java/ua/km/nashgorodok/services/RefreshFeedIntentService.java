package ua.km.nashgorodok.services;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import ua.km.nashgorodok.database.DatabaseUtil;
import ua.km.nashgorodok.database.models.Feed;


public class RefreshFeedIntentService extends IntentService {

    private static final String TAG = RefreshFeedIntentService.class.getSimpleName();
    public static final String ACTION_REFRESH_INTENT_SERVICE = "ua.km.nashgorodok.service.RESPONSE";
    public static final String EXTRA_KEY_OUT = "EXTRA_OUT";
    public static final String ACTION_REFRESH_IN_ACTIVITY = "EXTRA_OUT";
    public static final String URL_PARSE = "URL_PARSE";


    public RefreshFeedIntentService() {
        super("RefreshFeedIntentService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Start onHandleIntent");


        if (intent != null){

            String url = intent.getStringExtra(URL_PARSE);
            if (TextUtils.isEmpty(url)){
                return;
            }

            //DatabaseUtil.getFeedDao(getApplicationContext()).clearTable();

            List<Feed>feedList = FeedProvider.parse(getApplicationContext(), url);

//            DatabaseUtil.getFeedDao(getApplicationContext()).insertAll(feedList);

            if (intent.getAction().equals(ACTION_REFRESH_IN_ACTIVITY)){
                Intent responseIntent = new Intent();
                responseIntent.setAction(ACTION_REFRESH_INTENT_SERVICE);
                responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
                responseIntent.putExtra(EXTRA_KEY_OUT, false);
                sendBroadcast(responseIntent);
            }
        }
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "Start onDestroy");
        super.onDestroy();
    }
}
