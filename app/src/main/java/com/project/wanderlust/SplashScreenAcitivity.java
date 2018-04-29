package com.project.wanderlust;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashScreenAcitivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    public  LinearLayout linear;
    public  FirebaseRemoteConfig config;
    public  long cacheExpiration;
    public static String adID = "ca-app-pub-3963432354728475~4835237630";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(this, adID);


        config = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        config.setConfigSettings(configSettings);

        config.setDefaults(R.xml.config_default);

        cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (config.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        config.fetch(cacheExpiration).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                config.activateFetched();
                String splashdisplay = config.getString("splash");
                Drawable splash =  getResources().getDrawable(getResources()
                        .getIdentifier(splashdisplay, "drawable", getPackageName()));
                linear.setBackground(splash);
            }
        });

        //get default value
        String splashdisplay = config.getString("splash");

        setContentView(R.layout.activity_splash_screen);
        linear =  (LinearLayout)findViewById(R.id.splashlinear);

        Drawable splash =  getResources().getDrawable(getResources()
                .getIdentifier(splashdisplay, "drawable", getPackageName()));
        linear.setBackground(splash);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // Start your app main activity
                Intent i = new Intent(SplashScreenAcitivity.this, RegisterPhoneNumberActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
