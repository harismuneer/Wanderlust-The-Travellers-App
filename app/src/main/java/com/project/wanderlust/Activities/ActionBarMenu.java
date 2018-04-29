package com.project.wanderlust.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.project.wanderlust.R;
import com.project.wanderlust.Sensors.CompassActivity;
import com.project.wanderlust.Sensors.StepCountActivity;

/**
 * Created by PAKLAP.pk on 29-Apr-18.
 */

public class ActionBarMenu extends AppCompatActivity {


    //------------RELATED TO ACTION BAR------------------//
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
            {
                Intent intent = new Intent(this, ActivityUserDetails.class);
                intent.putExtra("phoneNumber", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                startActivity(intent);

                return true;
            }
            case R.id.InviteLink:
            {
                startActivity(new Intent(this, ActivityExtra.class));

                return true;
            }
            case R.id.voice:
            {
                startActivity(new Intent(this, ActivityVoice.class));

                return true;
            }
            case android.R.id.home:
            {
                onBackPressed();
                return true;
            }
            case R.id.Compass:
            {
                startActivity(new Intent(this, CompassActivity.class));

                return true;
            }
            case R.id.StepCount:
            {
                startActivity(new Intent(this, StepCountActivity.class));

                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //-----------------------------------------------------------------------//
}
