package com.project.wanderlust;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class UserDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        ((TextView) findViewById(R.id.name)).setText(name);
                    }
                    @Override public void onCancelled(DatabaseError databaseError) { }
                });

        FirebaseDatabase.getInstance().getReference("Journeys").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long checkIns = dataSnapshot.getChildrenCount();
                        ((TextView) findViewById(R.id.check)).setText(Long.toString(checkIns));
                    }

                    @Override public void onCancelled(DatabaseError databaseError) { }
                });

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("profilePictures",MODE_PRIVATE);
        file = new File(file, FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() + ".jpg");
        final Bitmap bitmap = SharedFunctions.decodeBitmapFromFile(file, 100, 100);
        ((ImageView) findViewById(R.id.photo)).setImageBitmap(bitmap);

        ((TextView) findViewById(R.id.friends)).setText(Integer.toString(Contacts.contactslist.size()));
    }
}
