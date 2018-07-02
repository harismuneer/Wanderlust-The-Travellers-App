package com.project.wanderlust.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.wanderlust.R;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

import retrofit2.Call;

public class ActivityTPage extends ActionBarMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tpage);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        fetchTwitterImage();
    }


    public void PostOnTwitter(View view)
    {
        try
        {
            EditText post = findViewById(R.id.post);
            String p = post.getText().toString();

            final Intent intent = new ComposerActivity.Builder(this)
                    .session(TwitterCore.getInstance().getSessionManager().getActiveSession())
                    .text(p)
                    .hashtags("#wanderlust_best_app")
                    .createIntent();
            startActivity(intent);
        }

        catch (TwitterException te)
        {
            te.printStackTrace();
            Toast.makeText(this, te.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void fetchTwitterImage()
    {
        //check if user is already authenticated or not
        if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null)
        {

            //fetch twitter image with other information if user is already authenticated

            //initialize twitter api client
            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

            //pass includeEmail : true if you want to fetch Email as well
            Call<User> call = twitterApiClient.getAccountService().verifyCredentials(true, false, false);
            call.enqueue(new Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    User user = result.data;
                    TextView name = findViewById(R.id.nameAndSurname4);
                    name.setText("User Name: " + user.name + "\nScreen Name : " + user.screenName);

                    String imageProfileUrl = user.profileImageUrl;

                    imageProfileUrl = imageProfileUrl.replace("_normal", "");

                    ///load image using Picasso
                    Picasso.with(ActivityTPage.this)
                            .load(imageProfileUrl)
                            .placeholder(R.mipmap.ic_launcher_round)
                            .into((ImageView)findViewById(R.id.profileImage4));
                }

                @Override
                public void failure(TwitterException exception) {
                    Toast.makeText(ActivityTPage.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            {
            //if user is not authenticated first ask user to do authentication
            Toast.makeText(this, "First to Twitter auth to Verify Credentials.", Toast.LENGTH_SHORT).show();
        }
    }
}
