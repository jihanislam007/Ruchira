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

    private boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");

            if (ipAddr.equals("")) {
                return false;

            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
