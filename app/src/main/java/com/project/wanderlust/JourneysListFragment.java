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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

public class JourneysListFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_journeys_list, container, false);

        return rootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        final Context context = getContext();

        ContextWrapper wrapper = new ContextWrapper(context);
        File file = wrapper.getDir("profilePictures",MODE_PRIVATE);
        file = new File(file, FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() + ".jpg");
        final Bitmap bitmap = SharedFunctions.decodeBitmapFromFile(file, 100, 100);
        //((CircleImageView) findViewById(R.id.userPhoto)).setImageBitmap(bitmap);

        FirebaseDatabase.getInstance().getReference("Journeys").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ContextWrapper wrapper = new ContextWrapper(context);
                        ArrayList<JourneyMini> journeys = new ArrayList<>();
                        SimpleDateFormat format = new SimpleDateFormat(CreateJourneyActivity.DATE_FORMAT);
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            try {
                                Date date = format.parse(ds.getKey());
                                String title = ds.child(CreateJourneyActivity.TITLE).getValue(String.class);
                                File file = wrapper.getDir(ds.getKey(), MODE_PRIVATE);
                                Uri photo = null;
                                //if that journey has a photo then choose one photo from it.
                                if (file.isDirectory()) {
                                    String[] images = file.list();
                                    if(images.length > 0)
                                        photo = Uri.parse(new File(file, images[0]).getAbsolutePath());
                                }
                                //Location of Journey par kaam karna abhi
                                journeys.add(new JourneyMini(title, date, "Faisal Town", "Lahore", photo));
                            } catch (ParseException e) {}
                        }
                        Collections.reverse(journeys);

                        //Kindly get username using database instead of hardcode
                        JourneyListAdapter adapter = new JourneyListAdapter(context, journeys, bitmap, "Farhan");
                        ListView view = getView().findViewById(R.id.journeysList);
                        view.setAdapter(adapter);
                    }

                    @Override public void onCancelled(DatabaseError databaseError) { }
                });

        FloatingActionButton button = (FloatingActionButton) getView().findViewById(R.id.createjourney);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createJourney(null);
            }
        });
    }

    public void createJourney(View view) {
        startActivity(new Intent(getActivity(), CreateJourneyActivity.class));
    }
}
