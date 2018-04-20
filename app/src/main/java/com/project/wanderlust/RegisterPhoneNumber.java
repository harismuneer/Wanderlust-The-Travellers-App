package com.project.wanderlust;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class RegisterPhoneNumber extends AppCompatActivity {

    private String[] locales;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone_number);

        //making firebase persistent
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            FirebaseDatabase.getInstance().getReference("users").keepSynced(true);
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
                else finish();
        }
    }

    private void doAfterPermissions() {
        //checking if user already signed in
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, CreateJourneyActivity.class));
            finish();
        }

        createCountryDropDownMenu();
    }

    private void createCountryDropDownMenu() {
        ArrayList<String> countries= getCountryNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countries);

        Spinner countrySpinner = findViewById(R.id.country);
        countrySpinner.setAdapter(adapter);

        //setting country to current country
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

        Intent intent = new Intent(this, VerifyPhoneNumberActivity.class);
        intent.putExtra("phone", editText1.getText().toString() + editText2.getText().toString());
        startActivity(intent);
    }

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
