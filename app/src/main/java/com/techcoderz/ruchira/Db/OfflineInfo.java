package com.techcoderz.ruchira.Db;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techcoderz.ruchira.ModelClasses.JsonModel.AppUser;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Arif on 8/4/2017.
 */

public class OfflineInfo {
    SharedPreferences sharedpreferences;
    Context context;
    public OfflineInfo(Context context){
        if(context==null){
            System.out.println("Context is null....");
        }
        this.context=context;
        sharedpreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    public void setUserInfo(String userInfo){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("userInfo",userInfo);
        editor.commit();
        System.out.println("Successfully save user info....");
    }
    public AppUser getUserInfo(){
        Gson gson=new Gson();
        AppUser appUser=gson.fromJson(sharedpreferences.getString("userInfo",""),AppUser.class);
        return appUser;
    }





    public void clearAll(){
        sharedpreferences.edit().clear().apply();
    }
}
