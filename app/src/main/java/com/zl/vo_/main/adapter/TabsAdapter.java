package com.zl.vo_.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/10/1.
 */

public class TabsAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> tabTitle;

    public TabsAdapter(FragmentManager fm, List<Fragment> fragments, List<String> tabTitle) {
        super(fm);
        this.fragments=fragments;
        this.tabTitle=tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle.get(position);
    }
}
