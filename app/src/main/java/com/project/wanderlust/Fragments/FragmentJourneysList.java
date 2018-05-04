package com.project.wanderlust.Fragments;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.wanderlust.Activities.ActivityCreateJourney;
import com.project.wanderlust.Activities.ActivitySelectLocation;
import com.project.wanderlust.Activities.ActivityShowJourney;
import com.project.wanderlust.Adapters.AdapterJourneyList;
import com.project.wanderlust.DataClasses.JourneyMini;
import com.project.wanderlust.R;
import com.project.wanderlust.Others.SharedFunctions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class FragmentJourneysList extends Fragment implements RecyclerView.OnItemTouchListener
{
    GestureDetector gestureDetector;
    RecyclerView rv;
    public static ArrayList<JourneyMini> journeys;
    public static AdapterJourneyList adapter = new AdapterJourneyList(null, null, null, R.layout.journey_cell);
    static Bitmap profilePhoto;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_journeys_list, container, false);
        setRetainInstance(true);

        return rootView;
    }

    @Override
    public  void onActivityCreated(Bundle b)
    {
        super.onActivityCreated(b);


        //-----------RECYCLER VIEW CODE-----------------//
        rv = getView().findViewById(R.id.journeysList);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.addOnItemTouchListener(this);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null)
                {
                    JourneyMini j = journeys.get(rv.getChildAdapterPosition(child));

                    Intent intent = new Intent(getContext(), ActivityShowJourney.class);
                    intent.putExtra("timestamp", new SimpleDateFormat(ActivityCreateJourney.DATE_FORMAT).format(j.date));
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    startActivity(intent);
                }

                return true;
            }
        }
        );
        //---------------------------------------------//

        final Context context = getContext();

        //get profile picture to show with every journey
        ContextWrapper wrapper = new ContextWrapper(context);
        File file = wrapper.getDir("profilePictures", MODE_PRIVATE);
        file = new File(file, FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() + ".jpg");
        profilePhoto = SharedFunctions.decodeBitmapFromFile(file, 100, 100);

        // FAB Listener
        FloatingActionButton button = (FloatingActionButton) getView().findViewById(R.id.createjourney);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ActivitySelectLocation.class);
                i.putExtra("callActivity", "journey");
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        new LoadJourneys().execute();
    }

    //-----------------GESTURE DETECTOR METHODS---------------------//
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
    {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e)
    {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }



    //-----------ASYNC TASK TO Load Journeys------------------//
    class LoadJourneys extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            try{

                //Get Journeys From Firebase
                FirebaseDatabase.getInstance().getReference("Journeys").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ContextWrapper wrapper = new ContextWrapper(getContext());
                                journeys = new ArrayList<>();
                                SimpleDateFormat format = new SimpleDateFormat(ActivityCreateJourney.DATE_FORMAT);
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    try {
                                        Date date = format.parse(ds.getKey());
                                        String title = ds.child(ActivityCreateJourney.TITLE).getValue(String.class);
                                        String address = ds.child("address").getValue(String.class);
                                        File file = wrapper.getDir(ds.getKey(), MODE_PRIVATE);
                                        Uri photo = null;
                                        Bitmap photo1 = null;

                                        String desc = ds.child("description").getValue(String.class);

                                        //if that journey has a photo then choose one photo from it.
                                        if (file.isDirectory()) {
                                            String[] images = file.list();
                                            if (images.length > 0)
                                            {
                                                File file1 = new File(file, "0.jpg");
                                                photo1 = SharedFunctions.decodeBitmapFromFile(file1, 500, 500);
                                            }
                                        }

                                        JourneyMini j1 = new JourneyMini(title, date, address, photo1, desc);

                                        //Location of Journey par kaam karna abhi
                                        journeys.add(j1);


                                        String lon = ds.child("longitude").getValue(String.class);
                                        String lat = ds.child("latitude").getValue(String.class);

                                        try {
                                            //Place the corresponding marker too
                                             j1.marker = FragmentMap.mMap.addMarker(new MarkerOptions()
                                                     .title(title)
                                                    .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)))
                                                    .snippet(new SimpleDateFormat(ActivityCreateJourney.DATE_FORMAT).format(date) + "\n" + desc));
                                             j1.marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                        }
                                        catch (Exception e)
                                        {

                                        }

                                    } catch (ParseException e) {
                                    }
                                }

                                Collections.reverse(journeys);

                                String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

                                //Get Name
                                FirebaseDatabase.getInstance().getReference("Users").child(phone)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String name = dataSnapshot.child("name").getValue(String.class);

                                                //--------------------------RECYCLER VIEW CODE--------------------------------//
                                                adapter = new AdapterJourneyList(journeys, profilePhoto, name, R.layout.journey_cell);
                                                rv.setAdapter(adapter);
                                            }
                                            @Override public void onCancelled(DatabaseError databaseError) { }
                                        });

                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getContext(),"Journeys Successfully Loaded",Toast.LENGTH_SHORT).show();
                                    }
                                });

                                //---Place Markers on Map
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

            }
            catch (Exception e)
            {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(getContext(),"Error Loading Journeys..",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }
    }

}
