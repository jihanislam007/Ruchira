package com.techcoderz.ruchira.utills;

import android.content.Context;

/**
 * Created by Minhazur on 3/22/15.
 */
public class LoggedInUser {
    private static final String TAG = "LoggedInUser";
    private Context context;
    private boolean isInfoLoaded;
    private static LoggedInUser loggedInUser;

    private LoggedInUser() {
    }

    public static LoggedInUser getInstance() {
        if (loggedInUser == null) {
            loggedInUser = new LoggedInUser();
        }
        return loggedInUser;
    }

    public void init(Context context) {
        this.context = context;
    }

    public boolean isInfoLoaded() {
        return isInfoLoaded;
    }

    public void setInfoLoaded(boolean infoLoaded) {
        this.isInfoLoaded = infoLoaded;
    }

    public void setUserInfo() {}

    public String getName() {
        return UserPreferences.LoggedInUserInfo.getName(context);
    }

    public void setName(String name) {
        UserPreferences.LoggedInUserInfo.setName(context, name);
    }


    public String getEmail() {
        return UserPreferences.LoggedInUserInfo.getEmail(context);
    }

    public void setEmail(String email) {
        UserPreferences.LoggedInUserInfo.setEmail(context, email);
    }


//    public void setResult(GetUserProfileResponse userProfileResponse) {
//        setEmail(userProfileResponse.getEmail());
//        setName(userProfileResponse.getNickname());
//
//        String photoSection = userProfileResponse.getPhotosection();
//        String profilePictureToken = photoSection == null ? "" : photoSection;
//
//        UserPreferences.LoggedInUserInfo.setProfilePictureToken(context, profilePictureToken);
//
//        String photoSection2 = userProfileResponse.getPhotosection1();
//        String coverPictureToken = photoSection == null ? "" : photoSection2;
//        UserPreferences.LoggedInUserInfo.setCoverPictureToken(context, coverPictureToken);
//
//        setInfoLoaded(true);
//    }
//
//    public GetUserProfileResponse getUserProfile(Context context) {
//        return UserPreferences.LoggedInUserInfo.getUserProfile(context);
//    }
}
