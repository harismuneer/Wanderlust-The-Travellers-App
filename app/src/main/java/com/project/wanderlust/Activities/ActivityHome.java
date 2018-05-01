package com.project.wanderlust.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.project.wanderlust.Fragments.FragmentContactsList;
import com.project.wanderlust.Fragments.FragmentJourneysList;
import com.project.wanderlust.Fragments.FragmentMap;
import com.project.wanderlust.Fragments.FragmentNotesList;
import com.project.wanderlust.R;


public class ActivityHome extends ActionBarMenu {

    private ViewPager viewPager;
    private BottomNavigationView bottombar;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_notes:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_journeysMap:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_journeys:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_friends:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottombar = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottombar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FPagerAdapter(getSupportFragmentManager()));

        viewPager.setOffscreenPageLimit(3);

        //When swipe occurs
        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position)
                    {
                        if (position == 0)
                            bottombar.setSelectedItemId(R.id.navigation_notes);
                        else if (position == 1)
                            bottombar.setSelectedItemId(R.id.navigation_journeysMap);
                        else if (position == 2)
                            bottombar.setSelectedItemId(R.id.navigation_journeys);
                        else if (position == 3)
                            bottombar.setSelectedItemId(R.id.navigation_friends);
                    }
                });

    }


    //Fragment Pager Adapter
    public class FPagerAdapter extends FragmentPagerAdapter
    {
        final int PAGE_COUNT = 4;

        public FPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override public int getCount() { return PAGE_COUNT; }

        @Override public Fragment getItem(int position)
        {

            if ((position == 0))
            {
                return new FragmentNotesList();
            }
            else if ((position == 1))
            {
                return new FragmentMap();
            }
            else if ((position == 2))
            {
                return new FragmentJourneysList();

            }
            else if ((position == 3))
            {
                return new FragmentContactsList();
            }

            return null;
        }
    }
}



