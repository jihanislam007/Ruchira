package com.techcoderz.ruchira.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.techcoderz.ruchira.R;


/**
 * Created by Shahriar Workspace on 4/21/2015.
 */
public class UserPreferences {
    final static String TAG = "UserPreferences";
    public static void saveEmail(Context context, String email) {
        Log.d(TAG, "email " + email);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.USER_EMAIL, email);
        editor.apply();
    }

    public static void savePassword(Context context, String password) {
        Log.d(TAG, "password " + password);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.USER_PASSWORD, password);
        editor.apply();
    }
    public static void saveCompanyId(Context context, String companyID) {
        Log.d(TAG, "companyID : " + companyID);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.USER_COMPANY_ID, companyID);
        editor.apply();
    }

    public static String getEmail(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.USER_EMAIL, null);
    }


    public static String getPassword(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.USER_PASSWORD, null);
    }

    public static String getCompanyId(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.USER_COMPANY_ID, null);
    }

    public static void saveToken(Context context, String atoken) {
        Log.d(TAG, "atoken " + atoken);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.ATOKEN, atoken);
        editor.apply();
    }

    public static void saveId(Context context, String id) {
        Log.d(TAG, "atoken " + id);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.ID, id);
        editor.apply();
    }

    public static void saveCompanyName(Context context, String companyName) {
        Log.d(TAG, "companyName " + companyName);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.COMPANY_NAME, companyName);
        editor.apply();
    }

    public static void saveDisplayName(Context context, String displayName) {
        Log.d(TAG, "displayName " + displayName);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.DISPLAYNAME, displayName);
        editor.apply();
    }

    public static void saveProfilePicLogin(Context context, String profilePicLoginImageToken) {
        Log.d(TAG, "ProfilePicLogin : " + profilePicLoginImageToken);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.USER_PROFILE_PIC_LOGIN, profilePicLoginImageToken);
        editor.apply();
    }


    public static String getProfilePicLogin(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.USER_PROFILE_PIC_LOGIN, null);
    }


    public static String getDisplayName(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.DISPLAYNAME, null);
    }

    public static String getCompanyName(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.COMPANY_NAME, null);
    }

    public static String getToken(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.ATOKEN, null);
    }

    public static String getUserId(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.ID, null);
    }


    public static void clearUserInfo(Context context) {
        Log.d(TAG, "clearUserInfo ok");
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.remove(RuchiraKeys.ATOKEN).apply();
        editor.remove(RuchiraKeys.DISPLAYNAME).apply();
        editor.remove(RuchiraKeys.USER_COVER_PIC_LOGIN).apply();
        editor.remove(RuchiraKeys.USER_PROFILE_PIC_LOGIN).apply();
        editor.remove(RuchiraKeys.COMPANY_NAME).apply();

        editor.remove(RuchiraKeys.RECEIVED_EVALUATION_ID).apply();
        editor.remove(RuchiraKeys.SENT_EVALUATION_ID).apply();
        editor.remove(RuchiraKeys.IS_NOTIFICATION_ENABLE).apply();
        editor.remove(RuchiraKeys.QR_CODE).apply();
        editor.remove(RuchiraKeys.ANNUAL_XP).apply();
        editor.remove(RuchiraKeys.TOTAL_XP).apply();
        editor.remove(RuchiraKeys.IsProfilePictureOrAvatarChanged).apply();

        /*Shouldn't clear app installation information that's why setting again*/
        setAppVersion(context, context.getResources().getString(R.string.app_version));
    }

    public static void clearOrderID(Context context) {
        Log.d(TAG, "clearUserInfo ok");
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.remove(RuchiraKeys.USER_ORDER_ID).apply();
    }

    public static void setAppVersion(Context context, String key) {
        SharedPreferences userInfo = getSharedPreferences(context);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(key, "AppVersion");
        editor.commit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(RuchiraKeys.USER_INFO, Context.MODE_PRIVATE);
    }

    public static class LoggedInUserInfo {
        public static void setEmail(Context context, String email) {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            sharedPreferences.edit().putString(RuchiraKeys.USER_EMAIL, email).commit();
        }

        public static String getEmail(Context context) {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            return sharedPreferences.getString(RuchiraKeys.USER_EMAIL, "");
        }

        public static void setName(Context context, String name) {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            sharedPreferences.edit().putString(RuchiraKeys.USER_NAME, name).commit();
        }

        public static String getName(Context context) {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            return sharedPreferences.getString(RuchiraKeys.USER_NAME, "");
        }

    }

}