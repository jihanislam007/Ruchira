package com.techcoderz.ruchira.utills;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.techcoderz.ruchira.R;

import java.net.InetAddress;

/**
 * Created by Saad Workspace on 11/8/2015.
 */
public class NetworkUtils {
    public static Boolean hasInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            return true;

        } else {
            if (context != null) {
                TaskUtils.alertUser(context, context.getResources().getString(R.string.message_no_interent));
            }
            return false;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }


}
