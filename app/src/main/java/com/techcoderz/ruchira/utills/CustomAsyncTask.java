package com.techcoderz.ruchira.utills;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Shahriar Workspace on 11/8/2015.
 */
public class CustomAsyncTask <Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    ProgressDialog progressDialog = null;
    private final String TAG = CustomAsyncTask.class.getName();

    private Context mContext;

    public Context getmContext() {
        return mContext;
    }

    public CustomAsyncTask(Context context) {
        this.mContext = context;
        if (!NetworkUtils.hasInternetConnection(context)) {
            cancel(true);
        }
    }

    public CustomAsyncTask(Context context, String message) {
        this.mContext = context;
        if (NetworkUtils.hasInternetConnection(context)) {
            progressDialog = ProgressDialog.show(context, null, message, true);
            progressDialog.setCancelable(false);
        } else {
            this.cancel(true);
        }
    }

    public CustomAsyncTask(Exception anyError) {
        Log.d(TAG, " Error");
    }

    public CustomAsyncTask(Context context, String title, String message) {
        this.mContext = context;
        if (NetworkUtils.hasInternetConnection(context)) {
            progressDialog = ProgressDialog.show(context, title, message, true); //FIXME : crashes
            progressDialog.setCancelable(false);
        } else {
            this.cancel(true);
        }
    }

    @Override
    protected Result doInBackground(Params... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Result result) {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        super.onPostExecute(result);
    }
}