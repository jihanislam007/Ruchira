package com.techcoderz.ruchira.utills;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.activity.LoginActivity;
import com.techcoderz.ruchira.activity.RuchiraActivity;

import java.io.File;


public class ViewUtils {

    public static LayoutInflater getLayoutInflater(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return layoutInflater;
    }

    public static void alertUser(Context context, String message) {
        TDialogHandler.showDialog(context, null, message, "Ok", null, null, null);
    }

    public static void setImageToImageView(Context context, ImageView imageView, String imagePath) {
        Log.i("PICASSO", "using picasso");
        Picasso.with(context)
                .load(Uri.fromFile(new File(imagePath)))
                .into(imageView);
    }

    public static void launchPopUpFragment(Context context, Fragment fragment) {
        FragmentManager supportFragmentManager = ((RuchiraActivity) context).getSupportFragmentManager();
        supportFragmentManager
                .beginTransaction()
//                .replace(R.id.containerForOverlay, fragment, fragment.getClass().getName())
                .addToBackStack(null)
                .commit();
    }


    public static void launchFragmentKeepingInBackStackAndPopLauncherFragment(Context context, Fragment fragmentToLaunch) {
        FragmentManager supportFragmentManager = ((RuchiraActivity) context).getSupportFragmentManager();
        supportFragmentManager.popBackStackImmediate();

        launchFragmentKeepingInBackStack(context, fragmentToLaunch);
    }

    public static void openNavigationDrawerItems(Context context, Fragment fragmentToLaunch) {
        FragmentManager supportFragmentManager = ((RuchiraActivity) context).getSupportFragmentManager();
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //clear all fragments from back stack
        supportFragmentManager.beginTransaction().replace(R.id.container_body, fragmentToLaunch)
                .commit();

    }

    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


    private static void launchFragmentKeepingInBackStack(Context context, Fragment fragmentToLaunch, String fragmentTag) {
        FragmentManager supportFragmentManager = ((RuchiraActivity) context).getSupportFragmentManager();
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container_body, fragmentToLaunch, fragmentTag)
                .addToBackStack(null)
                .commit();
    }

    public static void launchFragmentKeepingInBackStack(Context context, Fragment fragmentToLaunch) {
        Log.e("ViewUtil",fragmentToLaunch.getClass().getName());
        launchFragmentKeepingInBackStack(context, fragmentToLaunch, fragmentToLaunch.getClass().getName());
    }

    public static void popBackStack(Context context) {
        FragmentManager supportFragmentManager = ((RuchiraActivity) context).getSupportFragmentManager();
        supportFragmentManager.popBackStack();
    }

    public static void launchFragmentWithoutKeepingInBackStack(Context context, Fragment fragmentToLaunch) {
        launchFragmentWithoutKeepingInBackStack(context, fragmentToLaunch, fragmentToLaunch.getClass().getName());
    }

    private static void launchFragmentWithoutKeepingInBackStack(Context context, Fragment fragmentToLaunch, String fragmentTag) {
        FragmentManager supportFragmentManager = ((RuchiraActivity) context).getSupportFragmentManager();
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container_body, fragmentToLaunch, fragmentTag)
                .commit();
    }

    public static void takeImageFromGallery(Fragment fragment, String title, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        fragment.startActivityForResult(Intent.createChooser(intent, title), requestCode);
    }

    public static File takeImageFromCamera(Fragment fragment, int requestCode) {
        File newImageFile = TaskUtils.getNewImageFileForCamera();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newImageFile));
        fragment.startActivityForResult(intent, requestCode);

        return newImageFile;

    }


    public static void startLauncherActivity(Context context) {
        Intent intent = new Intent(context, LauncherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


    public static void takeImageFromGallery(Activity activityFrom, String title, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityFrom.startActivityForResult(Intent.createChooser(intent, title), requestCode);
    }

    public static File takeImageFromCamera(Activity activityFrom, int requestCode) {
        File newImageFile = TaskUtils.getNewImageFileForCamera();

        Intent intent = new Intent(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newImageFile));
        activityFrom.startActivityForResult(intent, requestCode);

        return newImageFile;
    }

//    public static void loadProfileImageUsingPicasso(Context context, String imageUrl, ImageView imageView) {
//        loadImageUsingPicasso(context, imageUrl, imageView, true);
//    }
//
//    public static void loadImageUsingPicasso(Context context, String imageUrl, ImageView imageView) {
//        loadImageUsingPicasso(context, imageUrl, imageView, false);
//    }

//    private static void loadImageUsingPicasso(Context context, String imageUrl, ImageView imageView, boolean isRounded) {
//        Transformation circleTransform = isRounded == true ? new CircleTransform() : null;
//
//        Picasso.with(context)
//                .load(imageUrl)
//                .placeholder(R.drawable.apraise_logo)
//               // .transform(circleTransform)
//                .into(imageView);
//    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isPointOutOfView(View w, float x, float y) {
        return x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom();
    }

    public static int getScreenWidth(Context context) {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        return screenWidth;

    }

    public static int getScreenHeight(Context context) {
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        return screenHeight;

    }

    // Image cache
//    public synchronized static void loadImageWithcashing(Context context,String imageURL, ImageView imageView){
//        ImageLoader imageLoader = new ImageLoader(context);
//        imageLoader.displayImage(imageURL,imageView);
//    }

    public static class FcaFragments {
        public static final String SETTING_FRAGMENT = "SETTING_FRAGMENT";
        public static final String SETTING_ABOUT_FRAGMENT = "SETTING_ABOUT_FRAGMENT";
        public static final String MY_QR_CODE_FRAGMENT = "MY_QR_CODE_FRAGMENT";

    }
    public static int getScreenWidthInPixels(Context context) {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        return screenWidth;
    }

}
