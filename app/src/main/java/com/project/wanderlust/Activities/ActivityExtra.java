package com.project.wanderlust.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.project.wanderlust.R;


public class ActivityExtra extends ActionBarMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    public void invite(View view) {
            //making invite request
            Intent intent = new AppInviteInvitation.IntentBuilder("Check the amazing Wanderlust App")
                    .setMessage("Hey! that's a new cool journey app. Download it from link below!")
                    .setDeepLink(Uri.parse("https://pygz5.app.goo.gl/wanderlust"))
                    .setCallToActionText("Share")
                    .build();
            startActivityForResult(intent, 2);
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == -1)
            {
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "sent");
                ActivityRegisterPhoneNumber.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,
                        payload);

                Toast.makeText(this, R.string.invitationSeccessfull, Toast.LENGTH_SHORT).show();
            }
            else {
                // Sending failed or it was canceled, show failure message to the user
                // ...

                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "not sent");
                ActivityRegisterPhoneNumber.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,
                        payload);

                Toast.makeText(this, R.string.error_send_invite, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
