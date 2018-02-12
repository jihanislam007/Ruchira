package com.techcoderz.ruchira.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.squareup.picasso.Picasso;
import com.techcoderz.ruchira.Db.OfflineInfo;
import com.techcoderz.ruchira.Fragments.AllSummaryFragments.AllSummaryFragment;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.DashBoardFragments.DashBoardFragment;
import com.techcoderz.ruchira.Fragments.OrderFragments.OrderFragment;
import com.techcoderz.ruchira.Fragments.AllOutletFragments.OutletsFragment;
import com.techcoderz.ruchira.Fragments.ProductPriceFragments.ProductPriceFragment;
import com.techcoderz.ruchira.Fragments.PromotionFragments.ProductPromotionFragment;
import com.techcoderz.ruchira.Fragments.OtherFragments.ProfileFragment;
import com.techcoderz.ruchira.Fragments.TodaysStatusFragment.TodayStatusFragment;
import com.techcoderz.ruchira.ServerInfo.ServerInfo;
import com.techcoderz.ruchira.Utils.AppConfig;
import com.techcoderz.ruchira.Utils.FragmentCallbacks;
import com.techcoderz.ruchira.Utils.LoggedInUser;
import com.techcoderz.ruchira.Utils.NetworkUtils;
import com.techcoderz.ruchira.Utils.TaskUtils;
import com.techcoderz.ruchira.Utils.UserPreferences;
import com.techcoderz.ruchira.Utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class MainActivity extends RuchiraActivity implements
        NavigationView.OnNavigationItemSelectedListener, FragmentCallbacks {
    private Toolbar toolbar;
    private ActionBar mActionbar;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int mSelectedId;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Fragment fragmentToLaunch;
    private String fragmentName;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private boolean isKilledBySystem = false;
    private int drawerItemToOpen;
    private CoordinatorLayout coordinatorLayout;
    private static String TAG = "MainActivity";

    private CircleImageView profile_image;
    private TextView profile_name_txt, company_name_txt;
    private boolean doubleBackToExitPressedOnce = false;

    OfflineInfo offlineInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        offlineInfo=new OfflineInfo(this);

        setToolbar();
        /*if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/
        //Utills(savedInstanceState);
        initialize();
        action();

        //fetchDataFromServer();
    }

    private void initialize() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mDrawer = (NavigationView) findViewById(R.id.main_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        View rootView = mDrawer.inflateHeaderView(R.layout.nav_header_main);

        profile_image = (CircleImageView) rootView.findViewById(R.id.imageView);
        profile_name_txt = (TextView) rootView.findViewById(R.id.profile_name_txt);
        company_name_txt = (TextView) rootView.findViewById(R.id.company_name_txt);
    }

    private void action() {
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorStatusBar));
        }
        // Drawer Task
        drawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        mDrawer.setNavigationItemSelectedListener(MainActivity.this);
        drawerBackPressHandling();

        Bundle savedInstanceState = getIntent().getExtras();
        if (savedInstanceState == null) {
            mSelectedId = savedInstanceState == null ? R.id.nav_dash_board : savedInstanceState.getInt("SELECTED_ID");
        }
        if (mSelectedId == 0) {
            DashBoardFragment homeFragment = new DashBoardFragment();
            ViewUtils.launchFragmentWithoutKeepingInBackStack(this, homeFragment);
        } else {
            itemSelection(mSelectedId);
        }

        if (UserPreferences.getToken(this) != null) {
            profile_name_txt.setText(UserPreferences.getDisplayName(this));
            Picasso.with(this).load(UserPreferences.getProfilePicLogin(this)).into(profile_image);
            company_name_txt.setText(UserPreferences.getCompanyName(this));
        } else {
            profile_name_txt.setText("Guest");
        }
    }

    private void drawerBackPressHandling() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    mActionbar.setDisplayHomeAsUpEnabled(true);
                    mActionbar.setHomeButtonEnabled(true);
                    mActionbar.setDisplayShowHomeEnabled(true);
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int count = getSupportFragmentManager().getBackStackEntryCount();
                            if (count > 0) {
                                getSupportFragmentManager().popBackStack();
                            } else {
                                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                                mActionbar.setDisplayHomeAsUpEnabled(false);
                                drawerToggle.setDrawerIndicatorEnabled(true);
                                drawerToggle.syncState();
                                mDrawerLayout.openDrawer(Gravity.LEFT);
                            }
                        }
                    });
                } else {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    mActionbar.setDisplayHomeAsUpEnabled(false);
                    drawerToggle.setDrawerIndicatorEnabled(true);
                    drawerToggle.syncState();
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                                mDrawerLayout.closeDrawer(Gravity.LEFT);
                            } else mDrawerLayout.openDrawer(GravityCompat.START);
                        }
                    });
                }
            }
        });
    }

    private void itemSelection(int mSelectedId) {
        fragmentToLaunch = null;
        drawerItemToOpen = mSelectedId;
        switch (mSelectedId) {
            case R.id.nav_dash_board:
                DashBoardFragment dashBoardFragment = new DashBoardFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, dashBoardFragment);
                updateToolBar("DashBoard");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_order:
                OrderFragment orderFragment = new OrderFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, orderFragment);
                updateToolBar("Order");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_todays_status:
                TodayStatusFragment todayStatusFragment = new TodayStatusFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, todayStatusFragment);
                updateToolBar("Today\'s Status");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_all_summary:
                AllSummaryFragment allSummaryFragment = new AllSummaryFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, allSummaryFragment);
                updateToolBar("All Summary");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_outlets:
                OutletsFragment outletsFragment = new OutletsFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, outletsFragment);
                updateToolBar("Outlets");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_promotions:
                ProductPromotionFragment productPromotionFragment = new ProductPromotionFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, productPromotionFragment);
                updateToolBar("Task");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_product_price:
                ProductPriceFragment productPriceFragment = new ProductPriceFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, productPriceFragment);
                updateToolBar("Product & Price");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_profile:
                ProfileFragment profileFragment = new ProfileFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, profileFragment);
                updateToolBar("Profile");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_setting:
                mTitle = "Logout";
                signOff(UserPreferences.getToken(MainActivity.this));
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
        }

        if (fragmentToLaunch != null) {
            ViewUtils.openNavigationDrawerItems(this, fragmentToLaunch);
        }
    }

    private void updateToolBar(String notifications) {
        mActionbar.setTitle(notifications);
    }

    private void signOff(final String userToken) {
        if (NetworkUtils.hasInternetConnection(this)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Log out?");
            alertDialogBuilder.setMessage("All cached data will be removed from this device," +
                    " and will be restored when you log in again.");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    TaskUtils.clearUserInfo(MainActivity.this);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();
        itemSelection(mSelectedId);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("SELECTED_ID", mSelectedId);
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (doubleBackToExitPressedOnce) {
                System.exit(0);
            }
            this.doubleBackToExitPressedOnce = true;
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else mDrawerLayout.openDrawer(GravityCompat.START);
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Utills(Bundle savedInstanceState) {
        TaskUtils.showCurrentDeviceResolutionType(this);
        LoggedInUser.getInstance().init(this);
        mTitle = mDrawerTitle = getTitle();
        if (savedInstanceState != null) {
            isKilledBySystem = savedInstanceState.getBoolean("isKilledBySystem");
            logDebug("MainPage: onCreate " + isKilledBySystem);
        }
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_activity_main);
        setSupportActionBar(toolbar);
        mActionbar = getSupportActionBar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isKilledBySystem", true);
        logDebug("onSaveInstanceState");
    }

    @Override
    public void onFragmentSelected(String fragmentName) {
        logDebug("fragment selected " + fragmentName);
        this.fragmentName = fragmentName;
    }

    @Override
    public void onProfileUpdated() {
    }

    private void fetchDataFromServer() {
        String tag_string_req = "req_logout";
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        final ProgressDialog finalProgressDialog = progressDialog;


    }

    public void handleResult(String result) {
        Log.d(TAG, result.toString());
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                Toast.makeText(this, "Logout Successful", Toast.LENGTH_LONG).show();
                ViewUtils.startLoginActivity(MainActivity.this);
                finish();
                return;
            } else {
                ViewUtils.alertUser(MainActivity.this, "Email address or password entered is incorrect. Please try again.");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}