package com.project.wanderlust;

import android.net.Uri;

import java.util.Date;

public class JourneyMini {
    private String title;
    private Date date;
    private String location;
    private String broaderLoocation;
    private Uri bitmap;

    public JourneyMini(String title, Date date, String location, String broaderLocation, Uri bitmap) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.broaderLoocation = broaderLocation;
        this.bitmap = bitmap;
    }

    public String getTitle() { return title; }

    public Date getDate() { return date; }

    public Uri getBitmap() { return bitmap; }

    public String getLocation() { return location; }

    public String getBroaderLocation() { return broaderLoocation; }
}
