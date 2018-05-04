package com.project.wanderlust.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.wanderlust.R;

public class ActivityShareOptions extends ActionBarMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_options);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }


    public void facebook(View view)
    {
        Intent main = new Intent(this,ActivityFBLogin.class);
        startActivity(main);
    }


    public void twitter(View view)
    {
        Intent main = new Intent(this,ActivityTLogin.class);
        startActivity(main);
    }

}
