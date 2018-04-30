package com.project.wanderlust.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.project.wanderlust.R;

import java.util.ArrayList;
import java.util.Timer;


public class AdapterShowImages extends ArrayAdapter<Uri>
{
    private ArrayList<Uri> items;
    private LayoutInflater inflater;

    public AdapterShowImages(Context context, ArrayList<Uri> items) {
        super(context, 0, items);
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Nullable
    @Override
    public Uri getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.image, null);

        try {
            ((ImageView) convertView).setImageURI(getItem(position));
        }
        catch (Exception e)
        {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), "Error in show image adapter.", Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }
}
