package com.techcoderz.ruchira.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.utills.NetworkUtils;
import com.techcoderz.ruchira.utills.TaskUtils;
import com.techcoderz.ruchira.utills.UserPreferences;
import com.techcoderz.ruchira.utills.ViewUtils;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class SettingFragment extends RuchiraFragment {

    private static String TAG = "SettingsFragment";
    RelativeLayout logoutLayout, aboutLayout, mainFragmentContainer, editProfileLayout;
    Fragment toLaunchFragment = null;
    private View divider5;

    public SettingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        this.activityCallbacks.onFragmentSelected(ViewUtils.FcaFragments.SETTING_FRAGMENT);
        setupActionBar();

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        initialize(rootView);
        action();

        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(mFragmentContext, toLaunchFragment);
        }
        return rootView;
    }

    private void initialize(View rootView) {
        logoutLayout = (RelativeLayout) rootView.findViewById(R.id.logoutLayout);
        aboutLayout = (RelativeLayout) rootView.findViewById(R.id.aboutLayout);
        editProfileLayout = (RelativeLayout) rootView.findViewById(R.id.editProfileLayout);
        mainFragmentContainer = (RelativeLayout) rootView.findViewById(R.id.settingFragmentLayout);
        divider5 = (View) rootView.findViewById(R.id.divider5);
    }

    private void action() {
        logoutLayout.setVisibility(View.VISIBLE);
        divider5.setVisibility(View.VISIBLE);

        if (UserPreferences.getToken(mFragmentContext) == null) {
            logoutLayout.setVisibility(View.GONE);
            divider5.setVisibility(View.GONE);
        }

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
                    signOff();
                }
//                else {
//                    int duration = 4000;
//                    SnackBarMaker.snackWithCustomTiming(mainFragmentContainer
//                            , getString(R.string.message_no_interent2), duration, mFragmentContext);
//                }

            }
        });
    }


    private void setupActionBar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("Settings");
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            mFragmentContext.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void signOff() {
        if (NetworkUtils.hasInternetConnection(getContext())) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mFragmentContext);
            alertDialogBuilder.setTitle("Log out?");
            alertDialogBuilder.setMessage("All cached data will be removed from this device, and will be restored when you log in again.");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    TaskUtils.clearUserInfo(mFragmentContext);
                    ViewUtils.startLoginActivity(mFragmentContext);
                }
            });
            alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}