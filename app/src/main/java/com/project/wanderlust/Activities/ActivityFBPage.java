package com.project.wanderlust.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.project.wanderlust.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivityFBPage extends ActionBarMenu {

    private ShareDialog shareDialog;
    private String name, surname, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbpage);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Bundle inBundle = getIntent().getExtras();
        name = inBundle.getString("name");
        surname = inBundle.getString("surname");

        if(ActivityFBLogin.profilePicture != null)
        {
            imageUrl = inBundle.getString("imageUrl");
            new DownloadImage((ImageView)findViewById(R.id.profileImage)).execute(imageUrl);
        }

        TextView nameView = (TextView)findViewById(R.id.nameAndSurname);
        nameView.setText("" + name + " " + surname);
    }


    public void onClick(View view)
    {
        switch (view.getId()){
            case R.id.share:
                share();
                break;


            case R.id.logout:
                logout();
                break;
        }
    }


    private void share()
    {
        shareDialog = new ShareDialog(this);
        List<String> taggedUserIds= new ArrayList<String>();
        taggedUserIds.add("{USER_ID}");
        taggedUserIds.add("{USER_ID}");
        taggedUserIds.add("{USER_ID}");

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://www.wanderlust.com"))
                .setShareHashtag(new ShareHashtag.Builder().setHashtag("#wanderlust").build())
                .setPeopleIds(taggedUserIds)
                .setPlaceId("{PLACE_ID}")
                .build();

        shareDialog.show(content);
    }


    public class DownloadImage extends AsyncTask<String, Void, Bitmap>
    {
        ImageView bmImage;

        public DownloadImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void logout()
    {
        LoginManager.getInstance().logOut();
        finish();
    }
}
