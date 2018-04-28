package com.project.wanderlust;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

        Intent intent = getIntent();
        String phone = (String) intent.getSerializableExtra("phoneNumber");

        FirebaseDatabase.getInstance().getReference("users").child(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        ((TextView) findViewById(R.id.name)).setText(name);
                    }
                    @Override public void onCancelled(DatabaseError databaseError) { }
                });

        FirebaseDatabase.getInstance().getReference("Journeys").child(phone)
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
        file = new File(file, phone + ".jpg");
        if(file.exists()) {
            final Bitmap bitmap = SharedFunctions.decodeBitmapFromFile(file, 500, 500);
            ((ImageView) findViewById(R.id.photo)).setImageBitmap(bitmap);
        }
        ((TextView) findViewById(R.id.friends)).setText(Integer.toString(ContactsActivity.contactslist.size()));
    }
}
