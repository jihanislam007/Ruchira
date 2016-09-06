package com.techcoderz.ruchira.utills;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;

import com.techcoderz.ruchira.R;

import java.io.File;
import java.util.UUID;

/**
 * Created by Shahriar Workspace on 9/6/2016.
 */
public class TaskUtils {

    final static String TAG = "TaskUtils";
    private static boolean gpsAlertShown = false;

    public static boolean isEmpty(String string) {
        if (string == null) {
            return true;
        }
        if (string.isEmpty()) {
            return true;
        }
        if (string.equals("null")) {
            return true;
        }
        return false;
    }


    public static boolean isNotEmpty(String string) {
        return !TaskUtils.isEmpty(string);
    }


    public static void alertUser(Context context, String message) {
//        TDialogHandler.showDialog(context, null, message);
    }

    public static boolean isFromLocalStorage(String filePath) { ///storage/emulated/0/FcDrawing1454406644872.jpg

        if (filePath == null)
            return false;

        return filePath.contains("storage");
    }

    public static String getAppDirBase() {
        String dirBase = Environment.getExternalStorageDirectory() + File.separator + "Apraise";
        File file = new File(dirBase);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dirBase;
    }

    public static File getNewImageFileForCamera() {
        File root = new File(getAppDirBase() + File.separator + "Camera");
        root.mkdirs();

        File imagePath = new File(root, UUID.randomUUID().toString() + ".jpg");
        Log.d(TAG, "Camera file opened: " + imagePath.getAbsolutePath());

        return imagePath;
    }

    public static void showGpsTurnOnAlert(final Context context) {
        if (gpsAlertShown) {
            return;
        }

        final DialogButtonListener positiveButtonListener = new DialogButtonListener() {
            @Override
            public void onDialogButtonClick() {
                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };
        final String messageToShow = context.getResources().getString(R.string.dialog_turnon_gps);
        TDialogHandler.showDialog(context, null, messageToShow, "Ok", positiveButtonListener, null, null);
        gpsAlertShown = true;
    }

    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void saveNavigationDrawerSelectedItem(Context context, RuchiraKeys.DRAWER_ITEMS drawerItems) {
        Log.d(TAG, "save current page " + drawerItems.name());
        SharedPreferences userInfo = context.getSharedPreferences(
                RuchiraKeys.USER_INFO, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putInt(RuchiraKeys.PAGE_NAME, drawerItems.ordinal());
        editor.commit();
    }

    public static void showCurrentDeviceResolutionType(Context context) {

        String deviceScreenResType = "";
        switch (context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                deviceScreenResType = "Large";
                break;

            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                deviceScreenResType = "XLARGE";
                break;

            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                deviceScreenResType = "NORMAL";
                break;

            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                deviceScreenResType = "SMALL";
                break;
        }
        Log.d("Screen Resolution ", deviceScreenResType);
    }


}
