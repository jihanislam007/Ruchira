package com.techcoderz.ruchira.Utils;

import android.content.Context;

/**
 * Created by Minhazur on 3/22/15.
 */
public class LoggedInUser {
    private static final String TAG = "LoggedInUser";
    private Context context;
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
}
