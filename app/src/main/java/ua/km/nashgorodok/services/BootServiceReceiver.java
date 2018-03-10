package ua.km.nashgorodok.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ua.km.nashgorodok.utils.UtilJobScheduler;


public class BootServiceReceiver extends BroadcastReceiver {

    private static final String TAG = BootServiceReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Start OnReceive");
        UtilJobScheduler.scheduleJob(context);
    }
}
