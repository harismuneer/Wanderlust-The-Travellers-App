package com.project.wanderlust;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExtraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);
    }

    public void invite(View view) {
        //requesting for invite link
        FirebaseDatabase.getInstance().getReference("InviteLink").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String link = "";
                try {
                    //reading invite link
                    link = dataSnapshot.getValue(String.class);
                } catch (Exception ex) {}

                //making invite request
                Intent intent = new AppInviteInvitation.IntentBuilder("Hey check this app")
                        .setMessage("Hey that's a new cool journey app. " + link)
                        .setCallToActionText("Share")
                        .build();
                startActivityForResult(intent, 2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }
}
