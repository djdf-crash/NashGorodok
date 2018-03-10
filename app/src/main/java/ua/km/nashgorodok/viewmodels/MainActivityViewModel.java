package ua.km.nashgorodok.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ua.km.nashgorodok.database.DatabaseUtil;
import ua.km.nashgorodok.database.models.Feed;
import ua.km.nashgorodok.services.RefreshFeedIntentService;

public class MainActivityViewModel extends AndroidViewModel {

    private LiveData<List<Feed>> listLiveData;
    private MutableLiveData<Boolean> refreshing;
    private MutableLiveData<Integer> itemMenuSelected;

    public static String UPADTE_FEEDS = "UPDATE_FEEDS";
    public static String DELETE_ALL_FEEDS = "DELETE_ALL_FEEDS";

    private Application app;

    public MainActivityViewModel(Application app) {
        super(app);
        this.app = app;
        listLiveData = DatabaseUtil.getFeedDao(app).getAll();
    }

    public LiveData<List<Feed>> getListLiveData() {
        return listLiveData;
    }

    public LiveData<List<Feed>> getListLiveData(String category) {
        listLiveData = DatabaseUtil.getFeedDao(app).getListFeedsByCategory(category);
        return listLiveData;
    }

    public MutableLiveData<Integer> getItemMenuSelected() {

        if (itemMenuSelected == null){
            itemMenuSelected = new MutableLiveData<>();
        }

        return itemMenuSelected;
    }

    public MutableLiveData<Boolean> getRefreshing() {
        if (refreshing == null) {
            refreshing = new MutableLiveData<>();
        }
        return refreshing;
    }

    public void refreshFeeds(String url){
        Intent refreshService = new Intent(app.getApplicationContext(), RefreshFeedIntentService.class);
        refreshService.setAction(RefreshFeedIntentService.ACTION_REFRESH_IN_ACTIVITY);
        refreshService.putExtra(RefreshFeedIntentService.URL_PARSE, url);
        app.startService(refreshService);
        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean result = intent
                        .getBooleanExtra(RefreshFeedIntentService.EXTRA_KEY_OUT,false);
                refreshing.setValue(result);
            }
        };

        IntentFilter intentFilter = new IntentFilter(
                RefreshFeedIntentService.ACTION_REFRESH_INTENT_SERVICE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        app.registerReceiver(broadcastReceiver, intentFilter);

    }

    public void updateFeedsAfterChangeCategory(List<Feed> feeds){
        new AsyncUpdateDb(app.getApplicationContext(), feeds).execute(UPADTE_FEEDS);
    }

    public void clearDb(){
        new AsyncUpdateDb(app.getApplicationContext(), Collections.<Feed>emptyList()).execute(DELETE_ALL_FEEDS);
    }

    public static class AsyncUpdateDb extends AsyncTask<String, Void, Void> {

        private Context ctx;
        private List<Feed> feeds;

        public AsyncUpdateDb(Context ctx, List<Feed> feeds) {
            this.ctx = ctx;
            this.feeds = feeds;
        }

        @Override
        protected Void doInBackground(String... voids) {
            if (TextUtils.equals(UPADTE_FEEDS, voids[0])) {
                DatabaseUtil.getFeedDao(ctx).updateAllFeeds(feeds);
            }else if (TextUtils.equals(DELETE_ALL_FEEDS, voids[0])){
                DatabaseUtil.getFeedDao(ctx).clearTable();
            }
            return null;
        }

    }

}
