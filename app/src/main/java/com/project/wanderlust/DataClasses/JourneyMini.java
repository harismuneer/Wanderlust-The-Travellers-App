package com.project.wanderlust.DataClasses;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.maps.model.Marker;

import java.util.Date;

public class JourneyMini {
    public String title;
    public Date date;
    public String location;
    public Bitmap bitmap;
    public String desc;
    public Marker marker;

    public JourneyMini(String title, Date date, String location, Bitmap bitmap, String desc) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.bitmap = bitmap;
        this.desc = desc;
    }



}
