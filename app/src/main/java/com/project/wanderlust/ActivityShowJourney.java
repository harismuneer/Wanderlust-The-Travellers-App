package com.project.wanderlust;

import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Locale;

public class ActivityShowJourney extends ActionBarMenu implements  TextToSpeech.OnInitListener {
    private AdapterShowImages adapter;

    TextView titleView;
    TextView descriptionView;
    private TextToSpeech tts;
    FloatingActionButton myFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_journey);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //------------TEXT TO SPEECH-------------//
        tts = new TextToSpeech(this, this);

        myFab = (FloatingActionButton) findViewById(R.id.speakjourney);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                speakJourney();
            }
        });
        //-----------------------------------------//

        Intent intent = getIntent();
        String timestamp = (String) intent.getSerializableExtra("timestamp");

        titleView = findViewById(R.id.title1);
        descriptionView = findViewById(R.id.description1);

        final TextView dateView = findViewById(R.id.date1);
        final GridView photoGrid = findViewById(R.id.photo1);

        //Get that particular journey
        FirebaseDatabase.getInstance().getReference("Journeys").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child(timestamp)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String dateString = dataSnapshot.getKey();
                        String title = dataSnapshot.child(ActivityCreateJourney.TITLE).getValue(String.class);
                        String description = dataSnapshot.child(ActivityCreateJourney.DESCRIPTION).getValue(String.class);
                        titleView.setText(title);
                        if(description.equals(""))
                            descriptionView.setVisibility(View.GONE);
                        else
                            descriptionView.setText(description);

                        try {
                            Date date = new SimpleDateFormat(ActivityCreateJourney.DATE_FORMAT).parse(dateString);

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

        //get photos from local storage and show
        final ArrayList<Uri> photos = new ArrayList<>();
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir(timestamp,MODE_PRIVATE);
        if (file.isDirectory()) {
            String[] images = file.list();
            for(String image : images) {
                photos.add(Uri.parse(new File(file, image).getAbsolutePath()));
            }

            adapter = new AdapterShowImages(this, photos);
            photoGrid.setAdapter(adapter);
        }
    }

    //---------------------------------------------------------------------------------//
    //-----------------------------TEXT TO SPEECH CODE---------------------------------//
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Toast.makeText(this,"This Language is not supported",Toast.LENGTH_SHORT).show();
                myFab.setEnabled(false);
            }

            else
            {
                myFab.setEnabled(true);
            }

        }
        else
        {
            Toast.makeText(this,"Text to Speech Initialization Error",Toast.LENGTH_SHORT).show();
            myFab.setEnabled(false);
        }
    }


    public void speakJourney ()
    {
        String title = titleView.getText().toString();
        String description = descriptionView.getText().toString();

        tts.speak("The title of journey is ", TextToSpeech.QUEUE_FLUSH, null);
        tts.speak(title, TextToSpeech.QUEUE_FLUSH, null);

        tts.speak("Description ", TextToSpeech.QUEUE_FLUSH, null);
        if (description == "")
        {
            tts.speak("The journey has no description", TextToSpeech.QUEUE_FLUSH, null);
        }
        else
            tts.speak(description, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


}
