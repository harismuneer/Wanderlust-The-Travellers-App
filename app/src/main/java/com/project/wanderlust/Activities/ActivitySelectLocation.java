package com.project.wanderlust.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.project.wanderlust.R;

public class ActivitySelectLocation extends AppCompatActivity {

    final private static int PLACE_PICKER_REQUEST = 45874;
    String callActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        Intent intent = getIntent();
        callActivity = (String) intent.getSerializableExtra("callActivity");

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try
        {
            Intent i = builder.build(this);
            startActivityForResult(i,PLACE_PICKER_REQUEST );
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place currentPlace = PlacePicker.getPlace(this, data);

            if (currentPlace != null)
            {
                LatLng l = currentPlace.getLatLng();
                String lon = Double.toString(l.longitude);
                String lat = Double.toString(l.latitude);

                String address = currentPlace.getAddress().toString();

                if (callActivity.equals("journey"))
                {
                    Intent i = new Intent(this, ActivityCreateJourney.class);
                    i.putExtra("lon" ,lon);
                    i.putExtra("lat" ,lat);
                    i.putExtra("address" ,address);

                    startActivity(i);
                    finish();
                }
                else if (callActivity.equals("note"))
                {

                }
            }
            else
                finish();

        }
        else
            finish();

    }

}
