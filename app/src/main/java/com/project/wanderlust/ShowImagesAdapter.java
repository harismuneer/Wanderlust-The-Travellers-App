package com.project.wanderlust;

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

import java.util.ArrayList;


public class ShowImagesAdapter extends ArrayAdapter<Uri> {
    private ArrayList<Uri> items;
    private LayoutInflater inflater;

    public ShowImagesAdapter(Context context, ArrayList<Uri> items) {
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
        ((ImageView) convertView).setImageURI(getItem(position));
        return convertView;
    }
}
