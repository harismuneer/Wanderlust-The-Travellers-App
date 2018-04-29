package com.project.wanderlust;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
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
    static ArrayList<JourneyMini> journeys;
    static AdapterJourneyList adapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_journeys_list, container, false);

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
                    startActivity(intent);
                }

                return true;
            }
        }
        );
        //---------------------------------------------//

        try {

            final Context context = getContext();

            //get profile picture to show with every journey
            ContextWrapper wrapper = new ContextWrapper(context);
            File file = wrapper.getDir("profilePictures", MODE_PRIVATE);
            file = new File(file, FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() + ".jpg");
            final Bitmap profilePhoto = SharedFunctions.decodeBitmapFromFile(file, 100, 100);


            FirebaseDatabase.getInstance().getReference("Journeys").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ContextWrapper wrapper = new ContextWrapper(context);
                            journeys = new ArrayList<>();
                            SimpleDateFormat format = new SimpleDateFormat(ActivityCreateJourney.DATE_FORMAT);
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                try {
                                    Date date = format.parse(ds.getKey());
                                    String title = ds.child(ActivityCreateJourney.TITLE).getValue(String.class);
                                    File file = wrapper.getDir(ds.getKey(), MODE_PRIVATE);
                                    Uri photo = null;

                                    //if that journey has a photo then choose one photo from it.
                                    if (file.isDirectory()) {
                                        String[] images = file.list();
                                        if (images.length > 0)
                                            photo = Uri.parse(new File(file, images[0]).getAbsolutePath());
                                    }

                                    //Location of Journey par kaam karna abhi
                                    journeys.add(new JourneyMini(title, date, "Faisal Town", "Lahore", photo));
                                } catch (ParseException e) {
                                }
                            }

                            Collections.reverse(journeys);

                            String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                            String username = FirebaseDatabase.getInstance().getReference("Users").child(phone).child("name").getKey();

                            //--------------------------RECYCLER VIEW CODE--------------------------------//
                            adapter = new AdapterJourneyList(journeys, profilePhoto, username, R.layout.journey_cell);
                            rv.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

        }
        catch (Exception e)
        {
            Crashlytics.logException(e);
            Toast.makeText(getContext(),"Error Loading Journeys..",Toast.LENGTH_SHORT).show();
        }

        FloatingActionButton button = (FloatingActionButton) getView().findViewById(R.id.createjourney);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ActivityCreateJourney.class));
            }
        });
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
}
