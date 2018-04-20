package com.project.wanderlust;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;

public class CreateJourneyActivity extends AppCompatActivity {
    public static final int CAMERA = 2;
    public static final int GALLERY = 3;

    FirebaseUser mUser;
    DatabaseReference mReference;

    final ArrayList<Bitmap> photos = new ArrayList<>();
    SelectedPicturesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_journey);

        getActionBar().setTitle("Step 2: Create Journey");
        getSupportActionBar().setTitle("Step 2: Create Journey");

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference("Journeys").child(mUser.getPhoneNumber());

        adapter = new SelectedPicturesAdapter(this, photos);
        GridView gridView = findViewById(R.id.photoGrid);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            if (requestCode == CAMERA) {
                photos.add((Bitmap) data.getExtras().get("data"));
                adapter.notifyDataSetChanged();
                //Toast.makeText(this, photos.size(), Toast.LENGTH_LONG).show();
            }
            else if (requestCode == GALLERY) {
                if(data.getClipData() != null){
                    ClipData mClipData = data.getClipData();
                    for(int i=0; i < mClipData.getItemCount(); i++){
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();

                        Bitmap bitmap = SharedFunctions.decodeBitmapFromFile(new File(getRealPathFromURI(uri)), 500, 500);
                        photos.add(bitmap);
                        adapter.notifyDataSetChanged();

                        //Toast.makeText(this, photos.size(), Toast.LENGTH_LONG).show();
                    }
                }
//                if(data.getData() != null) {
//                    Uri mImageUri=data.getData();
//                    Bitmap bitmap = SharedFunctions.decodeBitmapFromFile(new File(mImageUri.toString()), 500, 500);
//                    photos.add(bitmap);
//                    adapter.notifyDataSetChanged();
//                }
//                else {
//                    if(data.getClipData() != null){
//                        ClipData mClipData = data.getClipData();
//                        for(int i=0; i < mClipData.getItemCount(); i++){
//                            ClipData.Item item = mClipData.getItemAt(i);
//                            Uri uri = item.getUri();
//                            Bitmap bitmap = SharedFunctions.decodeBitmapFromFile(new File(uri.toString()), 500, 500);
//                            photos.add(bitmap);
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                }
            }
        }
    }

    public void createJourney(View view) {

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
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY);
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
}
