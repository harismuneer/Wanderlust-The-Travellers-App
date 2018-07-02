package com.project.wanderlust.Activities;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.project.wanderlust.R;


public class AcitivitySplashScreen extends AppCompatActivity
{

    private int SPLASH_TIME_OUT = 8000;
    public  LinearLayout linear;
    public  FirebaseRemoteConfig config;
    public  long cacheExpiration;
    public String adID = "ca-app-pub-3963432354728475~4835237630";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
                Toast.makeText(getApplicationContext(), R.string.splashScreen,Toast.LENGTH_SHORT).show();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Handler h = new Handler();

        h.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                // Start your app main activity
                Intent i = new Intent(AcitivitySplashScreen.this, ActivityRegisterPhoneNumber.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
