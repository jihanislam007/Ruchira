package com.techcoderz.ruchira.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.fragment.AllSummaryFragment;
import com.techcoderz.ruchira.fragment.DashBoardFragment;
import com.techcoderz.ruchira.fragment.OrderFragment;
import com.techcoderz.ruchira.fragment.OutletsFragment;
import com.techcoderz.ruchira.fragment.ProductPriceFragment;
import com.techcoderz.ruchira.fragment.ProductPromotionFragment;
import com.techcoderz.ruchira.fragment.SettingFragment;
import com.techcoderz.ruchira.fragment.ShopProfileFragment;
import com.techcoderz.ruchira.fragment.TodayStatusFragment;
import com.techcoderz.ruchira.utills.FragmentCallbacks;
import com.techcoderz.ruchira.utills.LoggedInUser;
import com.techcoderz.ruchira.utills.NetworkUtils;
import com.techcoderz.ruchira.utills.RuchiraKeys;
import com.techcoderz.ruchira.utills.TaskUtils;
import com.techcoderz.ruchira.utills.UserPreferences;
import com.techcoderz.ruchira.utills.ViewUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class MainActivity2 extends RuchiraActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentCallbacks {
    private Toolbar toolbar;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    public FragmentManager.OnBackStackChangedListener mOnBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            logDebug("on back stack change listener called");
            updateDrawerToggle();
        }
    };
    private int mSelectedId;
    private int backPressedCount;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView drawerUserName;
    private TextView userEmail;
    private CircleImageView drawerProfilePic;
    private ImageView drawerCoverPhoto;
    private Fragment fragmentToLaunch;
    private String fragmentName;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private boolean isKilledBySystem = false;
    private int drawerItemToOpen;
    private CoordinatorLayout coordinatorLayout;
    private ProgressDialog progressDialog;

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Utills(savedInstanceState);

        initialize();
        action(savedInstanceState);
    }

    private void initialize() {

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mDrawer = (NavigationView) findViewById(R.id.main_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

//        HomeFragment homeFragment = new HomeFragment();
//        ViewUtils.launchFragmentWithoutKeepingInBackStack(this, homeFragment);

    }

    private void action(Bundle savedInstanceState) {
        if (!NetworkUtils.hasInternetConnection(this)) {
        }
        drawerRefresh();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

//        toolbar.setTitleTextColor(Color.parseColor("#519c3f"));
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        savedInstanceState = getIntent().getExtras();


        Log.d("savedInstanceState", savedInstanceState + "");

        if (savedInstanceState == null) {
            mSelectedId = savedInstanceState == null ? R.id.nav_dash_board : savedInstanceState.getInt("SELECTED_ID");
        }
        Log.e(TAG, mSelectedId + "");
        itemSelection(mSelectedId);


    }

    private void drawerRefresh() {

        collapsingToolbarLayout.setTitle("Collapsing");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mTitle = getSupportActionBar().getTitle();
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
                if (UserPreferences.getIsProfilePictureOrAvatarChanged(getApplicationContext())) {
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
    }

    private void itemSelection(int mSelectedId) {
        fragmentToLaunch = null;
        drawerItemToOpen = mSelectedId;
        switch (mSelectedId) {
            case R.id.nav_dash_board:
                Log.e(TAG, mSelectedId + "");
                DashBoardFragment dashBoardFragment = new DashBoardFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, dashBoardFragment);
                updateToolBar("DashBoard");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_order:
                Log.e(TAG, mSelectedId + "");
                OrderFragment orderFragment = new OrderFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, orderFragment);
                updateToolBar("Order");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_todays_status:
                Log.e(TAG, mSelectedId + "");
                TodayStatusFragment todayStatusFragment = new TodayStatusFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, todayStatusFragment);
                updateToolBar("Todays Status");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_all_summary:
                Log.e(TAG, mSelectedId + "");
                AllSummaryFragment allSummaryFragment = new AllSummaryFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, allSummaryFragment);
                updateToolBar("All Summary");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_outlets:
                Log.e(TAG, mSelectedId + "");
                OutletsFragment outletsFragment = new OutletsFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, outletsFragment);
                updateToolBar("Outlets");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_promotions:
                Log.e(TAG, mSelectedId + "");
                ProductPromotionFragment productPromotionFragment = new ProductPromotionFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, productPromotionFragment);
                updateToolBar("Task");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_product_price:
                Log.e(TAG, mSelectedId + "");
                ProductPriceFragment productPriceFragment = new ProductPriceFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, productPriceFragment);
                updateToolBar("Product & Price");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
//
            case R.id.nav_profile:
                Log.e(TAG, mSelectedId + "");
                ShopProfileFragment shopProfileFragment = new ShopProfileFragment();
                ViewUtils.launchFragmentWithoutKeepingInBackStack(this, shopProfileFragment);
                updateToolBar("Shope Profile");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_setting:
                Log.e(TAG, mSelectedId + "");
                fragmentToLaunch = new SettingFragment();
                setTitle("Settings");
                updateToolBar("Settings");
                mTitle = "Settings";
                TaskUtils.saveNavigationDrawerSelectedItem(this, RuchiraKeys.DRAWER_ITEMS.SETTINGS);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
        }

        if (fragmentToLaunch != null) {
            ViewUtils.openNavigationDrawerItems(this, fragmentToLaunch);
        }
    }

    private void updateToolBar(String notifications) {
        getSupportActionBar().setTitle(notifications);
//        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
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
    protected void onPause() {
        super.onPause();
        getSupportFragmentManager().removeOnBackStackChangedListener(mOnBackStackChangedListener);
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportFragmentManager().addOnBackStackChangedListener(mOnBackStackChangedListener);
    }

    @Override
    public void onBackPressed() {
        try {
            int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackEntryCount < 1) {
                backPressedCount++;
                handleAppFinish();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    private void handleAppFinish() {
        if (backPressedCount == 2) {
            Toast.makeText(this, "Press again to exit the app", Toast.LENGTH_LONG).show();
        }
        if (backPressedCount > 2) {
            //super.onCreate(null);

            FragmentManager fm = this.getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.replaceExtras(new Bundle());
            //intent.setAction("");
            //intent.setData(null);
            intent.putExtra("EXIT", true);// ***Change Here***

            startActivity(intent);


            //finish();
            System.exit(0);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
//        getSupportActionBar().setIcon(R.drawable.logo);
//        toolbar.setLogo(R.drawable.logo);
//        toolbar.setNavigationIcon(R.drawable.logo);

    }

    public void updateDrawerToggle() {
        if (drawerToggle == null) {
            return;
        }
        boolean isRoot = getSupportFragmentManager().getBackStackEntryCount() == 0;
        logDebug("is root " + true + " back stack items " + getSupportFragmentManager().getBackStackEntryCount());
        drawerToggle.setDrawerIndicatorEnabled(isRoot);
        getSupportActionBar().setDisplayShowHomeEnabled(!isRoot);
        getSupportActionBar().setDisplayHomeAsUpEnabled(!isRoot);
        getSupportActionBar().setHomeButtonEnabled(!isRoot);
        if (isRoot) {
            drawerToggle.syncState();
        }
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
//        final String coverPicturePath = ApraiseKeys.SERVER_IMAGE_URL + UserPreferences.getCoverPicLogin(this);
//        ViewUtils.loadImageWithcashing(this, coverPicturePath, drawerCoverPhoto);
//        final String profilePicPath = ApraiseKeys.SERVER_IMAGE_URL + UserPreferences.getProfilePicLogin(this);
//        ViewUtils.loadImageWithcashing(this, profilePicPath, drawerProfilePic);
    }
}