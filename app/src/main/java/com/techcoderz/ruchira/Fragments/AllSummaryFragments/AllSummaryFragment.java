package com.techcoderz.ruchira.Fragments.AllSummaryFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.Utils.ViewUtils;

/**
 * Created by priom on 9/19/16.
 */
public class AllSummaryFragment extends RuchiraFragment {
    Fragment toLaunchFragment = null;
    private RelativeLayout editProfileLayout,sendFeedbackLayout,aboutLayout;
    private ViewPager mViewPager;
    Activity activity;
    Context context;
    private SmartTabLayout viewPagerTab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_summary, container, false);
        /*setupActionBar();
        initialize(rootView);
        action();*/
        FragmentStatePagerItemAdapter adapter=new FragmentStatePagerItemAdapter(
                ((AppCompatActivity)context).getSupportFragmentManager(),
                FragmentPagerItems.with(context)
                        .add("Today Sale", TodaysTotalSaleFragment.class)
                        .add("Monthly sale", MonthlyTotalSaleFragment.class)
                        .add("Yearly sale", YearlyTotalSaleFragment.class)
                        .create()
        );

        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);

        viewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(mViewPager);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;

    }
    /*private void setupActionBar() {
        mFragmentContext.getSupportActionBar().show();
    }

    private void initialize(View rootView) {
        editProfileLayout = (RelativeLayout)rootView.findViewById(R.id.editProfileLayout);
        sendFeedbackLayout = (RelativeLayout)rootView.findViewById(R.id.sendFeedbackLayout);
        aboutLayout = (RelativeLayout)rootView.findViewById(R.id.aboutLayout);
    }

    private void action(){
        mFragmentContext.getSupportActionBar().setTitle("All Summary");

        editProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTodaysTotalSaleFragment();
            }
        });

        sendFeedbackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMonthlyTotalSaleFragment();
            }
        });

        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openYearlyTotalSaleFragment();
            }
        });
    }

    private void openMonthlyTotalSaleFragment() {
        toLaunchFragment = new MonthlyTotalSaleFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(mFragmentContext, toLaunchFragment);
            toLaunchFragment = null;
        }
    }

    private void openTodaysTotalSaleFragment() {
        toLaunchFragment = new TodaysTotalSaleFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(mFragmentContext, toLaunchFragment);
            toLaunchFragment = null;
        }
    }

    private void openYearlyTotalSaleFragment() {
        toLaunchFragment = new YearlyTotalSaleFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(mFragmentContext, toLaunchFragment);
            toLaunchFragment = null;
        }
    }*/

}