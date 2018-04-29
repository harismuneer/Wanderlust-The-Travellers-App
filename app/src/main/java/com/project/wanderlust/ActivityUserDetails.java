package com.project.wanderlust;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class ActivityUserDetails extends ActionBarMenu {

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Google Admob
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
        catch (Exception e)
        {
            Crashlytics.logException(e);
        }

        Intent intent = getIntent();
        String phone = (String) intent.getSerializableExtra("phoneNumber");

        //Get Name
        FirebaseDatabase.getInstance().getReference("Users").child(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        ((TextView) findViewById(R.id.name)).setText(name);
                    }
                    @Override public void onCancelled(DatabaseError databaseError) { }
                });

        //Get Check-Ins Count
        FirebaseDatabase.getInstance().getReference("Journeys").child(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long checkIns = dataSnapshot.getChildrenCount();
                        ((TextView) findViewById(R.id.check)).setText(Long.toString(checkIns));
                    }

                    @Override public void onCancelled(DatabaseError databaseError) { }
                });

        //Get Profile Picture
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("profilePictures",MODE_PRIVATE);
        file = new File(file, phone + ".jpg");
        if(file.exists()) {
            final Bitmap bitmap = SharedFunctions.decodeBitmapFromFile(file, 500, 500);
            ((ImageView) findViewById(R.id.photo)).setImageBitmap(bitmap);
        }

        //Set Friends Count
        ((TextView) findViewById(R.id.friends)).setText(Integer.toString(FragmentContactsList.contactslist.size()));
    }


    //----------------------ADMOB----------------------------//

    //For Admob
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
