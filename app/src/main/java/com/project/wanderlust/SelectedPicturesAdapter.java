package com.project.wanderlust;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class SelectedPicturesAdapter extends ArrayAdapter<Bitmap>
{
    private ArrayList<Bitmap> items;

    public SelectedPicturesAdapter(Context context, ArrayList<Bitmap> items)
    {
        super(context, 0, items);
        this.items = items;
    }

    @Nullable
    @Override
    public Bitmap getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Bitmap bitmap = getItem(position);
        ImageView imageView = new ViewSquareImage(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bitmap);
        convertView = imageView;
        return convertView;
    }
}
