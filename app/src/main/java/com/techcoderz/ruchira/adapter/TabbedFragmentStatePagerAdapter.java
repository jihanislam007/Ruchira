package com.techcoderz.ruchira.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.techcoderz.ruchira.fragment.NotOrderedFragment;
import com.techcoderz.ruchira.fragment.OrderedFragment;
import com.techcoderz.ruchira.fragment.YetToVisitFragment;

/**
 * Created by apollo on 11/8/2015.
 */
public class TabbedFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    private String tabTitles[] = new String[]{"Yet To Visit", "Ordered", "Not Ordered"};
    private Context context;
    Fragment toLaunchFragment = null;

    public TabbedFragmentStatePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        notifyDataSetChanged();
        getItem(0);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        notifyDataSetChanged();
        switch (position) {
            case 0:
                return YetToVisitFragment.newInstance();
            case 1:
                return OrderedFragment.newInstance();
            case 2:
                return NotOrderedFragment.newInstance();
            default:
                return YetToVisitFragment.newInstance();

        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}