package com.project.wanderlust.Activities;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.wanderlust.DataClasses.Contact;
import com.project.wanderlust.Fragments.FragmentContactsList;
import com.project.wanderlust.Others.SharedFunctions;
import com.project.wanderlust.R;

import java.io.File;
import java.util.ArrayList;

public class AcitivitySplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 5000;
    public  LinearLayout linear;
    public  FirebaseRemoteConfig config;
    public  long cacheExpiration;
    public static String adID = "ca-app-pub-3963432354728475~4835237630";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the status bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        MobileAds.initialize(this, adID);

        config = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        config.setConfigSettings(configSettings);

        config.setDefaults(R.xml.config_default);

        cacheExpiration = 3600;
        if (config.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        setContentView(R.layout.activity_splash_screen);
        linear =  (LinearLayout)findViewById(R.id.splashlinear);

        config.fetch(cacheExpiration).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                config.activateFetched();
                String splashdisplay = config.getString("splash");
                Drawable splash =  getResources().getDrawable(getResources()
                        .getIdentifier(splashdisplay, "drawable", getPackageName()));
                linear.setBackground(splash);
                Toast.makeText(getApplicationContext(),"Splash Screen changed according to a new update :)",Toast.LENGTH_SHORT).show();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        // Start your app main activity
        Intent i = new Intent(AcitivitySplashScreen.this, ActivityRegisterPhoneNumber.class);
        startActivity(i);
        finish();
    }
}
