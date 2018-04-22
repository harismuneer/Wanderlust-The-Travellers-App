package com.project.wanderlust;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShowJourney extends AppCompatActivity {
    private ShowImagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_journey);

        Intent intent = getIntent();
        String timestamp = (String) intent.getSerializableExtra("timestamp");
        timestamp = "2018-04-22 11:18:44";

        final TextView titleView = findViewById(R.id.title);
        final TextView descriptionView = findViewById(R.id.description);
        final TextView dateView = findViewById(R.id.date);
        final GridView photoGrid = findViewById(R.id.photo);

        FirebaseDatabase.getInstance().getReference("Journeys").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child(timestamp)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String dateString = dataSnapshot.getKey();
                        String title = dataSnapshot.child(CreateJourneyActivity.TITLE).getValue(String.class);
                        String description = dataSnapshot.child(CreateJourneyActivity.DESCRIPTION).getValue(String.class);
                        titleView.setText(title);
                        if(description.equals(""))
                            descriptionView.setVisibility(View.GONE);
                        else
                            descriptionView.setText(description);

                        try {
                            Date date = new SimpleDateFormat(CreateJourneyActivity.DATE_FORMAT).parse(dateString);

                            //format the date in which required
                            String date1 = new SimpleDateFormat("dd-MM-yyyy").format(date);
                            dateView.setText(date1);
                        }
                        catch (ParseException p) {}
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        final ArrayList<Uri> photos = new ArrayList<>();
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir(timestamp,MODE_PRIVATE);
        if (file.isDirectory()) {
            String[] images = file.list();
            for(String image : images) {
                photos.add(Uri.parse(new File(file, image).getAbsolutePath()));
            }

            adapter = new ShowImagesAdapter(this, photos);
            photoGrid.setAdapter(adapter);
        }
    }
}
