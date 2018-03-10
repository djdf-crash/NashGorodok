package ua.km.nashgorodok.utils;


import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import ua.km.nashgorodok.services.JobSchedulerService;


public class UtilJobScheduler {

    private static final String TAG = UtilJobScheduler.class.getSimpleName();

    public static void scheduleJob(Context context) {

        ComponentName serviceComponent = new ComponentName(context, JobSchedulerService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(TimeUnit.SECONDS.toMillis(1));
        builder.setOverrideDeadline(TimeUnit.SECONDS.toMillis(5));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);
        builder.setBackoffCriteria(TimeUnit.SECONDS.toMillis(10), JobInfo.BACKOFF_POLICY_LINEAR);

        Log.i(TAG, "scheduleJob: adding job to scheduler");

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }


}
