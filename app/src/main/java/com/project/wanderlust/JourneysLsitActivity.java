package com.project.wanderlust;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class JourneysLsitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journeys_lsit);

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("profilePictures",MODE_PRIVATE);
        file = new File(file, FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() + ".jpg");
        final Bitmap bitmap = SharedFunctions.decodeBitmapFromFile(file, 100, 100);
        //((CircleImageView) findViewById(R.id.userPhoto)).setImageBitmap(bitmap);
        final Context context = this;

        FirebaseDatabase.getInstance().getReference("Journeys").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                        ArrayList<JourneyMini> journeys = new ArrayList<>();
                        SimpleDateFormat format = new SimpleDateFormat(CreateJourneyActivity.DATE_FORMAT);
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            try {
                                Date date = format.parse(ds.getKey());
                                String title = ds.child(CreateJourneyActivity.TITLE).getValue(String.class);
                                File file = wrapper.getDir(ds.getKey(), MODE_PRIVATE);
                                Uri photo = null;
                                if (file.isDirectory()) {
                                    String[] images = file.list();
                                    if(images.length > 0)
                                        photo = Uri.parse(new File(file, images[0]).getAbsolutePath());
                                }
                                journeys.add(new JourneyMini(title, date, "Faisal Town", "Lahore", photo));
                            } catch (ParseException e) {}
                        }
                        JourneyLsitAdapter adapter = new JourneyLsitAdapter(context, journeys, bitmap, "Farhan");
                        ListView view = findViewById(R.id.journeysList);
                        view.setAdapter(adapter);
                    }

                    @Override public void onCancelled(DatabaseError databaseError) { }
                });
    }

    public void createJourney(View view) {
        startActivity(new Intent(this, CreateJourneyActivity.class));
    }
}
