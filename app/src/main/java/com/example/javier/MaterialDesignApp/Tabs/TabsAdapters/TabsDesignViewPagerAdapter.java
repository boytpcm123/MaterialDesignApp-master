package com.example.javier.MaterialDesignApp.Tabs.TabsAdapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.javier.MaterialDesignApp.Tabs.TabsViews.TabItemNews;


public class TabsDesignViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence titles[];
    int tabNumber;

    // Constructor
    public TabsDesignViewPagerAdapter (FragmentManager fragmentManager, CharSequence titles[], int tabNumber) {
        super(fragmentManager);

        this.titles = titles;
        this.tabNumber = tabNumber;

    }

    // Return Fragment for each position
    @Override
    public Fragment getItem(int position) {

        //Create fragment
        TabItemNews tabItemNews= new TabItemNews();
        //Create bundle to send value
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        //Set value
        tabItemNews.setArguments(bundle);

        return tabItemNews;

    }

    // Return tab titles for each position

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    // Return tab number.
    @Override
    public int getCount() {
        return tabNumber;
    }
}