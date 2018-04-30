package com.project.wanderlust.DataClasses;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Date;

public class JourneyMini {
    public String title;
    public Date date;
    public String location;
    public String broaderLoocation;
    public Bitmap bitmap;

    public JourneyMini(String title, Date date, String location, String broaderLocation, Bitmap bitmap) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.broaderLoocation = broaderLocation;
        this.bitmap = bitmap;
    }

}
