package com.project.wanderlust.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.project.wanderlust.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


public class ActivityTLogin extends ActionBarMenu {

    private TwitterLoginButton twitterLoginButton;
    private TwitterSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String consumerKey = "yHLRJX0scOPs3KzEjpG24ncwV";
        String consumerSecret = "TGurwmekln1LoUU1Bcza7P3cridGYCdSF0aTQIzVNLlANt6ACU";


        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(consumerKey, consumerSecret))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();

        //finally initialize twitter with created configs
        Twitter.initialize(config);


        setContentView(R.layout.activity_t_login);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);




        if (getTwitterSession() != null)
        {
            Intent main = new Intent(this,ActivityTPage.class);
            startActivity(main);
        }
        else {

            twitterLoginButton = findViewById(R.id.tlogin);
            twitterLoginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    session = result.data;
                    Intent main = new Intent(ActivityTLogin.this, ActivityTPage.class);
                    startActivity(main);

                }

                @Override
                public void failure(TwitterException exception) {
                    // Do something on failure
                }
            });
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }


    private TwitterSession getTwitterSession()
    {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        return session;
    }
}




