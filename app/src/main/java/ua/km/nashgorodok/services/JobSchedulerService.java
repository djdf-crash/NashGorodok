package ua.km.nashgorodok.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import ua.km.nashgorodok.utils.UtilJobScheduler;


public class JobSchedulerService extends JobService {

    private static final String TAG = JobSchedulerService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Log.d(TAG, "Start onStartJob");

        Intent service = new Intent(getApplicationContext(), RefreshFeedIntentService.class);
        getApplicationContext().startService(service);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Start onStopJob");
        return true;
    }
}
