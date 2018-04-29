package com.project.wanderlust;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Locale;

public class RegisterPhoneNumberActivity extends AppCompatActivity {

    String[] locales;
    FirebaseAuth mAuth;
    ArrayList<String> countries;

    public static FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case R.id.Profile:
                //start profile activity here
                startActivity(new Intent(this, SetProfileDataActivity.class));
                return true;

            case R.id.InviteLink:
                //start invite link activity here
                startActivity(new Intent(this, ExtraActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //when user presses the back arrow in action bar, he is taken to previous activity
    @Override
    public boolean onNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone_number);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.hide();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        //making firebase persistent
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            FirebaseDatabase.getInstance().getReference("Users").keepSynced(true);
            FirebaseDatabase.getInstance().getReference("Journeys").keepSynced(true);
            FirebaseDatabase.getInstance().getReference("inviteLink").keepSynced(true);
        }
        catch (Exception ex) {}

        //FirebaseDatabase.getInstance().getReference("inviteLink").setValue("abc");

        //asking for permissions
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION};
        if(!SharedFunctions.hasPermissions(getApplicationContext(), permissions)) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
        else doAfterPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) doAfterPermissions();
                else
                {
                    Bundle params = new Bundle();
                    params.putBoolean("is_given", false);
                    mFirebaseAnalytics.logEvent("permissions_given", params);

                    finish();
                }
        }
    }

    private void doAfterPermissions() {

        //logging an event that how many users give permissions and how many don't

        Bundle params = new Bundle();
        params.putBoolean("is_given", true);
        mFirebaseAnalytics.logEvent("permissions_given", params);

        //checking if user already signed in
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
        }

        createCountryDropDownMenu();
    }


    private void createCountryDropDownMenu() {
        countries= getCountryNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countries);

        Spinner countrySpinner = findViewById(R.id.country);
        countrySpinner.setAdapter(adapter);

        //setting country by default to Pakistan
        String currentCountry = "Pakistan";

        for(int i = 0; i < countries.size(); i++) {
            if(countries.get(i).equals(currentCountry)) {
                countrySpinner.setSelection(i);
                break;
            }
        }

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String CountryID= locales[position];
                String[] rl = getResources().getStringArray(R.array.CountryCodes);
                for(String x : rl){
                    String[] g = x.split(",");
                    if(g[1].trim().equals(CountryID.trim())){
                        TextView textView = findViewById(R.id.countryCode);
                        textView.setText("+" + g[0]);

                        //foreign users
                        Bundle params = new Bundle();
                        params.putString("country", countries.get(position));
                        mFirebaseAnalytics.logEvent("countries", params);

                        return;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void nextButton(View view) {
        TextView editText1 = findViewById(R.id.countryCode);
        EditText editText2 = findViewById(R.id.phone);

        if (editText2.getText().length() == 0) {
            Toast.makeText(this, "Phone number required.", Toast.LENGTH_SHORT).show();

            Bundle params = new Bundle();
            params.putBoolean("error_made", true);
            mFirebaseAnalytics.logEvent("leaving_phone_number", params);

            return;
        }
        Intent intent = new Intent(this, VerifyPhoneNumberActivity.class);
        intent.putExtra("phone", editText1.getText().toString() + editText2.getText().toString());
        startActivity(intent);
    }


    // returns an arraylist containing country names
    public ArrayList<String> getCountryNames() {

        //Getting country ISOs
        locales = Locale.getISOCountries();
        ArrayList<String> countries = new ArrayList<>();

        //getting country names
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            countries.add(obj.getDisplayCountry());
        }
        return countries;
    }
}
