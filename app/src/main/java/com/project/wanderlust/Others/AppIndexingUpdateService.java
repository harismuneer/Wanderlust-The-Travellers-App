package com.project.wanderlust.Others;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.Indexable;
import com.project.wanderlust.DataClasses.JourneyMini;
import com.project.wanderlust.Fragments.FragmentJourneysList;

import java.util.ArrayList;

public class AppIndexingUpdateService extends JobIntentService {
    // Job-ID must be unique across your whole app.
    private static final int UNIQUE_JOB_ID = 42;

    public static void enqueueWork(Context context) {
        enqueueWork(context, AppIndexingUpdateService.class, UNIQUE_JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        ArrayList<Indexable> indexableJourneys = new ArrayList<>();

        ArrayList<JourneyMini> myJourneys = FragmentJourneysList.journeys;

        for (int i = 0; i < myJourneys.size(); i++) {
            //-------------------------------------------
            //Index Journey
            Indexable journeyToIndex = Indexables.noteDigitalDocumentBuilder()
                    .setName(myJourneys.get(i).title)
                    .setText("Indexed Journey")
                    .setUrl("http://www.wanderlust.com/message/" + myJourneys.get(i).title)
                    .build();

            indexableJourneys.add(journeyToIndex);
        }


        if (indexableJourneys.size() > 0) {
            Indexable[] journeysArr = new Indexable[indexableJourneys.size()];
            journeysArr = indexableJourneys.toArray(journeysArr);

            // batch insert indexable notes into index
            FirebaseAppIndex.getInstance().update(journeysArr);
        }
    }
}
