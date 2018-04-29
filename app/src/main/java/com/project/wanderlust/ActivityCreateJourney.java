package com.project.wanderlust;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ActivityCreateJourney extends ActionBarMenu
{
    public static final int CAMERA = 2;
    public static final int GALLERY = 3;

    public static String TITLE = "title";
    public static String DESCRIPTION = "description";
    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    DatabaseReference mReference;

    private EditText title;
    private EditText description;

    final ArrayList<Bitmap> photos = new ArrayList<>();
    SelectedPicturesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_journey);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mReference = FirebaseDatabase.getInstance().getReference("Journeys").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);

        adapter = new SelectedPicturesAdapter(this, photos);
        GridView gridView = findViewById(R.id.photoGrid);
        gridView.setAdapter(adapter);
    }


    //For Getting Pictures Using Gallery and Camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            if (requestCode == CAMERA)
            {
                photos.add((Bitmap) data.getExtras().get("data"));
                adapter.notifyDataSetChanged();
            }
            else if (requestCode == GALLERY)
            {
                if(photos.size() < 10)
                {
                    if (data.getClipData() != null)
                    {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++)
                        {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            Bitmap bitmap = SharedFunctions.decodeBitmapFromFile(new File(getRealPathFromURI(uri)), 500, 500);
                            photos.add(bitmap);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                else Toast.makeText(this, "Cannot add more than 10 pictures", Toast.LENGTH_LONG).show();
            }
        }
    }




    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    //-----------------------BUTTON LISTENERS--------------------------//
    public void createJourney(View view)
    {
        final String t = title.getText().toString();
        final String d = description.getText().toString();
        if(t.equals("")) {
            Toast.makeText(this, "Please give your journey a title", Toast.LENGTH_LONG).show();
            return;
        }
        Date date = new Date();

        final String time = new SimpleDateFormat(DATE_FORMAT).format(date);

        //save all images locally
        new SaveImages(getApplicationContext(), time, photos).execute();

        //save title and description in online db
        final Map<String, String> map = new HashMap<>();
        map.put(TITLE, t);
        map.put(DESCRIPTION, d);
        mReference.child(time).setValue(map);

        //-------------------------------------------
        //Index Journey
        Indexable journeyToIndex = Indexables.noteDigitalDocumentBuilder()
                .setName(t)
                .setText(d)
                .setUrl("http://www.wanderlust.com/message/" + t)
                .build();

        FirebaseAppIndex.getInstance()
                .update(journeyToIndex);
        //-------------------------------------------

        FragmentJourneysList.journeys.add(new JourneyMini(t, date, "Faisal Town", "Lahore", null));
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Journey Created Successfully!", Toast.LENGTH_LONG).show();

        onBackPressed();
    }

    public void Cancel(View view) { onBackPressed(); }

    public void getCameraPicture(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA);
    }

    public void getGalleryPictures(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pictures"), GALLERY);
    }

}
