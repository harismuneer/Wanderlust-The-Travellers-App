package com.project.wanderlust;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class JourneyListAdapter extends ArrayAdapter<JourneyMini> {
    private ArrayList<JourneyMini> items;
    private Bitmap userPic;
    private String userName;
    private LayoutInflater inflater;

    public JourneyListAdapter(Context context, ArrayList<JourneyMini> items, Bitmap userPic, String userName) {
        super(context, 0, items);
        this.items = items;
        this.userPic = userPic;
        this.userName = userName;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Nullable
    @Override
    public JourneyMini getItem(int position) {
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
            convertView = inflater.inflate(R.layout.journey_cell, null);
        JourneyMini item = getItem(position);
        ((ImageView) convertView.findViewById(R.id.photo)).setImageBitmap(userPic);
        ((TextView) convertView.findViewById(R.id.userName)).setText(userName);
        ((TextView) convertView.findViewById(R.id.location)).setText(item.getLocation());
        ((TextView) convertView.findViewById(R.id.locationBroader)).setText(item.getBroaderLocation());
        ((TextView) convertView.findViewById(R.id.date)).setText(SharedFunctions.months[item.getDate().getMonth() - 1] + " " + item.getDate().getDate());
        ((TextView) convertView.findViewById(R.id.title)).setText(item.getTitle());
        if(item.getBitmap() != null) {
            ImageView imageView = convertView.findViewById(R.id.journeyPic);
            imageView.setImageURI(item.getBitmap());
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            imageView.setLayoutParams(layoutParams);
        }
        else {
            ImageView imageView = convertView.findViewById(R.id.journeyPic);
            imageView.setImageBitmap(null);
        }

        convertView.setTag(item);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JourneyMini journey = (JourneyMini)  v.getTag();
                if(journey != null) {
                    Intent intent = new Intent(getContext(), ShowJourneyActivity.class);
                    intent.putExtra("timestamp", new SimpleDateFormat(CreateJourneyActivity.DATE_FORMAT).format(journey.getDate()));
                    getContext().startActivity(intent);
                }
            }
        });
        return convertView;
    }
}
