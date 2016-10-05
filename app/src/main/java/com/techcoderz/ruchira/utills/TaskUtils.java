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
import com.techcoderz.ruchira.model.Beat;
import com.techcoderz.ruchira.model.Outlet;
import com.techcoderz.ruchira.model.OutletRemainning;
import com.techcoderz.ruchira.model.Target;
import com.techcoderz.ruchira.model.TodayOrder;
import com.techcoderz.ruchira.model.TodaySale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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


    public static List<TodaySale> setTodayTotalSale(String json) {
        ArrayList<TodaySale> TodayTotalSaleList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONObject jsonObject2 = jsonObject.getJSONObject("todaySale");

//            for (int i = 0; i < jsonArray.length(); i++) {
            TodaySale todaySale = new TodaySale();
            todaySale.setYesterdeay(jsonObject2.getString("yesterday"));
            todaySale.setMonthAccumulation(jsonObject2.getString("monthAccumulation"));
            todaySale.setMonthDailyAvg(jsonObject2.getString("monthDailyAvg"));
            todaySale.setMonthWeeklyAvg(jsonObject2.getString("monthWeeklyAvg"));
            todaySale.setTodaySale(jsonObject2.getString("todaSale"));
            TodayTotalSaleList.add(todaySale);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return TodayTotalSaleList;
    }

    public static List<TodayOrder> setTodayOrder(String json) {
        ArrayList<TodayOrder> TodayTotalOrderList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject2 = jsonObject.getJSONObject("todayOrder");

//            for (int i = 0; i < jsonObject2.length(); i++) {
            TodayOrder todayOrder = new TodayOrder();
            todayOrder.setYesterdeay(jsonObject2.getString("yesterday"));
            todayOrder.setMonthAccumulation(jsonObject2.getString("monthAccumulation"));
            todayOrder.setMonthDailyAvg(jsonObject2.getString("monthDailyAvg"));
            todayOrder.setMonthWeeklyAvg(jsonObject2.getString("monthWeeklyAvg"));
            todayOrder.setTodayOrder(jsonObject2.getString("todayOrder"));
            TodayTotalOrderList.add(todayOrder);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return TodayTotalOrderList;
    }

    public static List<Target> setTarget(String json) {
        ArrayList<Target> TodayTotalOrderList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject2 = jsonObject.getJSONObject("target");

//            for (int i = 0; i < jsonObject2.length(); i++) {
            Target todayOrder = new Target();
            todayOrder.setThisMonth(jsonObject2.getString("thisMonth"));
            todayOrder.setCurrentAchive(jsonObject2.getString("currentAchieve"));
            todayOrder.setRemainningTarget(jsonObject2.getString("remainingTarget"));
            todayOrder.setAvgTargetVisit(jsonObject2.getString("avgTargetVisit"));
            TodayTotalOrderList.add(todayOrder);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return TodayTotalOrderList;
    }


    public static List<Beat> setBeat(String json) {
        ArrayList<Beat> bannerImageList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("beat");

            for (int i = 0; i < jsonArray.length(); i++) {
                Beat bannerImage = new Beat();
                bannerImage.setId(jsonArray.getJSONObject(i).getString("id"));
                bannerImage.setTitle(jsonArray.getJSONObject(i).getString("title"));
                bannerImageList.add(bannerImage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bannerImageList;
    }

    public static List<Outlet> setOutlet(String json) {
        ArrayList<Outlet> outletList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("outlet");

            for (int i = 0; i < jsonArray.length(); i++) {
                Outlet outlet = new Outlet();
                outlet.setOid(jsonArray.getJSONObject(i).getString("id"));
                outlet.setTitle(jsonArray.getJSONObject(i).getString("title"));
                outlet.setGroup(jsonArray.getJSONObject(i).getString("group"));
                outletList.add(outlet);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return outletList;
    }

    public static List<OutletRemainning> setOutletRemainning(String json) {
        ArrayList<OutletRemainning> TodayTotalOrderList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject2 = jsonObject.getJSONObject("outletRemaining");

//            for (int i = 0; i < jsonObject2.length(); i++) {
            OutletRemainning todayOrder = new OutletRemainning();
            todayOrder.setTotalOutlet(jsonObject2.getString("totalOutlet"));
            todayOrder.setOutletVisited(jsonObject2.getString("outletVisited"));
            todayOrder.setOutletRemained(jsonObject2.getString("outletRemained"));
            TodayTotalOrderList.add(todayOrder);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return TodayTotalOrderList;
    }


}
