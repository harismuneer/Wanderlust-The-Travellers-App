package com.project.wanderlust;

import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ActivitySetProfileData extends AppCompatActivity
{
    private EditText name;
    private CircleImageView imageView;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;

    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile_data);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        phone = (String) intent.getSerializableExtra("phone");
        name = findViewById(R.id.name);

        imageView = findViewById(R.id.photo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 1);
            }
        });
    }

    //For taking an image from gallery as profile photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == 1) {
                Uri imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }


    public void nextButton(View view)
    {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

        if(drawable == null)
        {
            Toast.makeText(this, "Must attach a Profile Photo", Toast.LENGTH_SHORT).show();
            return;
        }

        final Bitmap photo = drawable.getBitmap();
        final String nam = name.getText().toString();

        if(nam.length() == 0)
        {
            Toast.makeText(this, "Must input Name", Toast.LENGTH_SHORT).show();
            return;
        }

        //-------------------------------------------------------//
        //got photo and name, now storing them

        StorageReference reference = storageRef.child("profilePictures/" + phone + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try
        {
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] data = baos.toByteArray();

            final ProgressDialog dialog = ProgressDialog.show(this, "Please wait", "Uploading Profile Image...", true);

            //upload the byte array online on firebase storage
            UploadTask uploadTask = reference.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(ActivitySetProfileData.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                    File file = wrapper.getDir("profilePictures",MODE_PRIVATE);

                    //saving profile picture offline
                    file = new File(file, FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() + ".jpg");
                    OutputStream stream = null;
                    try
                    {
                        stream = new FileOutputStream(file);
                        photo.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    }
                    catch (Exception ex) {
                        Toast.makeText(ActivitySetProfileData.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    finally {
                        try {
                            dialog.dismiss();
                            stream.flush();
                            stream.close();
                        }catch (Exception ex) {}
                    }
                    //------------------------------------------------//


                    ActivityRegisterPhoneNumber.mFirebaseAnalytics.setUserProperty("user_name", nam);

                    Map<String, String> map = new HashMap<>();
                    map.put("name", nam);

                    DatabaseReference reference1 = mDatabase.child("Users").child(phone);

                    reference1.child("name").setValue(null);
                    mDatabase.child("Users").child(phone).setValue(map);

                    //Name is saved successfully in Users.

                    startActivity(new Intent(ActivitySetProfileData.this, ActivityHome.class));
                    finish();
                }
            });
        }

        catch (Exception ex)
        {
            Toast.makeText(ActivitySetProfileData.this, "Error Saving Profile Data", Toast.LENGTH_SHORT).show();
        }
    }

}
