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
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

                return true;
            }
            case R.id.InviteLink:
            {
                Intent intent = new Intent(this, ActivityExtra.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

                return true;
            }
            case R.id.voice:
            {
                Intent intent = new Intent(this, ActivityVoice.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

                return true;
            }
            case android.R.id.home:
            {
                onBackPressed();
                return true;
            }
            case R.id.Compass:
            {
                Intent intent = new Intent(this, CompassActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

                return true;
            }
            case R.id.StepCount:
            {
                Intent intent = new Intent(this, StepCountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //-----------------------------------------------------------------------//
}
