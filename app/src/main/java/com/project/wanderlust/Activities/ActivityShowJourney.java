package com.project.wanderlust.Activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.wanderlust.Adapters.AdapterShowImages;
import com.project.wanderlust.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ActivityShowJourney extends ActionBarMenu implements  TextToSpeech.OnInitListener {

    final ArrayList<Uri> photos = new ArrayList<>();
    private AdapterShowImages   adapter;


    TextView titleView;
    TextView descriptionView;
    TextView addressView;

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
        addressView = findViewById(R.id.address);

        final TextView dateView = findViewById(R.id.date1);
        final GridView photoGrid = findViewById(R.id.photo1);

        adapter = new AdapterShowImages(getApplicationContext(), photos);
        photoGrid.setAdapter(adapter);

        new showImages(timestamp).execute();

        //Get that particular journey
        FirebaseDatabase.getInstance().getReference("Journeys").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child(timestamp)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String dateString = dataSnapshot.getKey();
                        String title = dataSnapshot.child(ActivityCreateJourney.TITLE).getValue(String.class);
                        String description = dataSnapshot.child(ActivityCreateJourney.DESCRIPTION).getValue(String.class);
                        String address = dataSnapshot.child("address").getValue(String.class);

                        titleView.setText(title);
                        if(description == null || description.equals(""))
                            descriptionView.setVisibility(View.GONE);
                        else
                            descriptionView.setText(description);

                        addressView.setText(address);
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
    }


    //-----------ASYNC TASK TO SAVE IMAGES------------------//
    class showImages extends AsyncTask<Void, Integer, Void>
    {
        String timestamp;

        showImages(String t)
        {
            this.timestamp = t;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            //get photos from local storage and show

            ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
            File file = wrapper.getDir(timestamp, MODE_PRIVATE);
            if (file.isDirectory()) {
                String[] images = file.list();
                if (images.length != 0) {
                    for (String image : images) {
                        photos.add(Uri.parse(new File(file, image).getAbsolutePath()));
                    }

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            //adapter notify
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Pics Loaded Successfully.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            Handler handler2 = new Handler(Looper.getMainLooper());
            handler2.post(new Runnable() {
                public void run() {
                    if (photos.size() == 0) {
                        TextView t1 = findViewById(R.id.description3);
                        GridView v1 = findViewById(R.id.photo1);

                        t1.setVisibility(View.GONE);
                        v1.setVisibility(View.GONE);
                    }
                }
            });

            return null;
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

        float s = (float) 0.8;

        tts.setSpeechRate(s);
        tts.speak("The title of journey is ", TextToSpeech.QUEUE_FLUSH, null);
        tts.speak(title, TextToSpeech.QUEUE_ADD, null);

        tts.speak("Description ", TextToSpeech.QUEUE_ADD, null);
        if (description == "")
        {
            tts.speak("The journey has no description", TextToSpeech.QUEUE_ADD, null);
        }
        else
            tts.speak(description, TextToSpeech.QUEUE_ADD, null);
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
