package com.project.wanderlust;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends FragmentActivity  {

    private TextView mTextMessage;
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
                case R.id.navigation_journeys:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_friends:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case R.id.Profile:
                //start profile activity here
                startActivity(new Intent(this, SetProfileDataActivity.class));
                return true;

            case R.id.InviteLink:
                //start invite link activity here
                startActivity(new Intent(this, ExtraActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //when user presses the back arrow in action bar, he is taken to previous activity
    @Override
    public boolean onNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        bottombar = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottombar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FPagerAdapter(getSupportFragmentManager()));

        //When swipe occurs
        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position)
                    {
                        if (position == 0)
                            bottombar.setSelectedItemId(R.id.navigation_notes);
                        else if (position == 1)
                            bottombar.setSelectedItemId(R.id.navigation_journeys);
                        if (position == 2)
                            bottombar.setSelectedItemId(R.id.navigation_friends);
                    }
                });

    }


    //Fragment Pager Adapter
    public class FPagerAdapter extends FragmentPagerAdapter
    {

        Fragment[] fragmentslist = {null,null,null};
        final int PAGE_COUNT = 3;

        public FPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override public int getCount() { return PAGE_COUNT; }

        @Override public Fragment getItem(int position)
        {
            if ((position == 0) && (fragmentslist[position] == null))
            {
                //fragmentslist[position] = new ShowNotesFragment();
                fragmentslist[position] =  new JourneysListFragment();  //testing
            }
            else if ((position == 1) && (fragmentslist[position] == null))
            {
                fragmentslist[position] =  new JourneysListFragment();
            }
            else if ((position == 2) && (fragmentslist[position] == null))
            {
                fragmentslist[position] = new ContactsFragment();
            }

            return fragmentslist[position];
        }
    }
}



