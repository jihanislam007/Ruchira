package com.techcoderz.ruchira.utills;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.techcoderz.ruchira.R;


/**
 * Created by Shahriar Workspace on 4/21/2015.
 */
public class UserPreferences {
    final static String TAG = "UserPreferences";

    public static void saveName(Context context, String name) {
        Log.d(TAG, "name " + name);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.DISPLAYNAME, name);
        editor.apply();
    }

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

    public static void saveOrderId(Context context, String orderId) {
        Log.d(TAG, "orderId : " + orderId);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.USER_ORDER_ID, orderId);
        editor.apply();
    }

    public static void saveCompanyId(Context context, String companyID) {
        Log.d(TAG, "companyID : " + companyID);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.USER_COMPANY_ID, companyID);
        editor.apply();
    }

    public static void saveContactNumber(Context context, String contactNumber) {
        Log.d(TAG, "contactNumber " + contactNumber);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.USER_CONTACT_NUMBER, contactNumber);
        editor.apply();
    }

    public static void saveReligious(Context context, String religious) {
        Log.d(TAG, "religious " + religious);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.USER_RELIGIOUS, religious);
        editor.apply();
    }

    public static void saveGender(Context context, String gender) {
        Log.d(TAG, "gender " + gender);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.USER_GENDER, gender);
        editor.apply();
    }

    public static void saveAlarmMinute(Context context, String minute) {
        Log.d(TAG, "minute " + minute);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.USER_ALARM_MINUTE, minute);
        editor.apply();
    }

    public static void saveAlarmHour(Context context, String hour) {
        Log.d(TAG, "hour " + hour);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.USER_ALARM_HOUR, hour);
        editor.apply();
    }

    public static void saveDob(Context context, String dob) {
        Log.d(TAG, "dob " + dob);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.USER_DOB, dob);
        editor.apply();
    }

    public static String getName(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.USER_NAME, null);
    }

    public static String getOrderId(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.USER_ORDER_ID, null);
    }

    public static String getEmail(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.USER_EMAIL, null);
    }


    public static String getReligious(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.USER_RELIGIOUS, null);
    }

    public static String getDob(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.USER_DOB, null);
    }

    public static String getPassword(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.USER_PASSWORD, null);
    }

    public static String getCompanyId(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.USER_COMPANY_ID, null);
    }

    public static String getGender(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.USER_GENDER, null);
    }


    public static void saveIsProfilePictureOrAvatarChanged(Context context, boolean isChanged) {
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putBoolean(RuchiraKeys.IsProfilePictureOrAvatarChanged, isChanged);
        editor.apply();
    }

    public static boolean getIsProfilePictureOrAvatarChanged(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getBoolean(RuchiraKeys.IsProfilePictureOrAvatarChanged, true);
    }

    public static void saveReceivedEvaluationId(Context context, String receivedEvaluationId) {
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.RECEIVED_EVALUATION_ID, receivedEvaluationId);
        editor.apply();
    }

    public static String getReceivedEvaluationId(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.RECEIVED_EVALUATION_ID, "0");
    }

    public static void saveSentEvaluationId(Context context, String sentEvaluationId) {
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.SENT_EVALUATION_ID, sentEvaluationId);
        editor.apply();
    }

    public static String getSentEvaluationId(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.SENT_EVALUATION_ID, "0");
    }

    public static void saveNotificationToggle(Context context, boolean isChecked) {
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putBoolean(RuchiraKeys.IS_NOTIFICATION_ENABLE, isChecked);
        editor.apply();
    }

    public static boolean getNotificationToggle(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getBoolean(RuchiraKeys.IS_NOTIFICATION_ENABLE, true);
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

    public static void saveShopeProfileId(Context context, String ShopeProfileId) {
        Log.d(TAG, "ShopeProfileId " + ShopeProfileId);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.SHOPE_PROFILE_ID, ShopeProfileId);
        editor.apply();
    }

    public static void saveDisplayName(Context context, String displayName) {
        Log.d(TAG, "displayName " + displayName);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.DISPLAYNAME, displayName);
        editor.apply();
    }

    public static void saveLaid(Context context, String alaid) {
        Log.d(TAG, "alaid : " + alaid);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.LAID, alaid);
        editor.apply();
    }


    public static void saveProfilePicLogin(Context context, String profilePicLoginImageToken) {
        Log.d(TAG, "ProfilePicLogin : " + profilePicLoginImageToken);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.USER_PROFILE_PIC_LOGIN, profilePicLoginImageToken);
        editor.apply();
    }

    public static void saveCoverPicLogin(Context context, String coverPicLoginImageToken) {
        Log.d(TAG, "CoverPicLogin : " + coverPicLoginImageToken);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.USER_COVER_PIC_LOGIN, coverPicLoginImageToken);
        editor.apply();
    }


    public static void saveTotalXp(Context context, String totalXp) {
        Log.d(TAG, "totalXp " + totalXp);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.TOTAL_XP, totalXp);
        editor.commit();
    }

    public static void saveAnnualXp(Context context, String annualXp) {
        Log.d(TAG, "annualXp " + annualXp);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.ANNUAL_XP, annualXp);
        editor.commit();
    }

    public static void saveGcmId(Context context, String gcmId) {
        Log.d(TAG, "saveGcmId " + gcmId);
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.GCM_ID, gcmId);
        editor.commit();
    }

    public static void saveQRCode(Context context, String qrCode) {
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(RuchiraKeys.QR_CODE, qrCode);
        editor.commit();
    }

    public static void setActiveChatSessionId(Context context, int csId) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putInt(RuchiraKeys.ACTIVE_CHAT_SESSION, csId);
        editor.commit();
    }

    public static String getShopeProfileId(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.SHOPE_PROFILE_ID, null);
    }

    public static String getCoverPicLogin(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.USER_COVER_PIC_LOGIN, null);
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

    public static String getId(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.ID, null);
    }

    public static String getLaid(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.LAID, null);
    }

    public static Boolean isTokenSet(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.contains(RuchiraKeys.ATOKEN);
    }

    public static String getTotalXp(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.TOTAL_XP, null);
    }

    public static String getAnnualXp(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.ANNUAL_XP, "0");
    }

    public static String getGcmId(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.GCM_ID, null);
    }

    public static String getQRCode(Context context) {
        SharedPreferences userInfo = getSharedPreferences(context);
        return userInfo.getString(RuchiraKeys.QR_CODE, null);
    }

    public static void clearUserPreferences(Context context) {
        Log.d(TAG, "clearUserInfo ok");
        SharedPreferences userInfo = getSharedPreferences(context);

        SharedPreferences.Editor editor = userInfo.edit();
        editor.clear();
        editor.commit();
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

    public static boolean isFreshInstallation(Context context, String key) {
        Log.d(TAG, "Fresh installation " + key);
        SharedPreferences userInfo = getSharedPreferences(context);

        if (userInfo.contains(key)) {
            return false;
        }
        return true;
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

        public static void setProfilePictureToken(Context context, String profilePictureToken) {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            sharedPreferences.edit().putString(RuchiraKeys.USER_PROFILE_PIC_LOGIN, profilePictureToken).commit();
        }

        public static String getProfilePictureToken(Context context) {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            return sharedPreferences.getString(RuchiraKeys.USER_PROFILE_PIC_LOGIN, "");

        }

        public static void setCoverPictureToken(Context context, String coverPictureToken) {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            sharedPreferences.edit().putString(RuchiraKeys.USER_COVER_PIC_LOGIN, coverPictureToken).commit();
        }

        public static String getCoverPictureToken(Context context) {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            return sharedPreferences.getString(RuchiraKeys.USER_COVER_PIC_LOGIN, "");

        }

    }

}