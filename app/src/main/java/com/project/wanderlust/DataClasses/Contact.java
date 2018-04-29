package com.project.wanderlust.DataClasses;

import android.graphics.Bitmap;

public class Contact {
    private Bitmap photo;
    private String name;

    private String phone;

    public Contact(Bitmap photo, String name, String phone) {
        this.photo = photo;
        this.name = name;
        this.phone = phone;
    }

    public Bitmap getPhoto() { return photo; }

    public String getName() { return name; }

    public String getPhone() { return phone; }

    public void setPhoto(Bitmap photo) { this.photo = photo; }

    public void setName(String name) { this.name = name; }

    public void setPhone(String phone) { this.phone = phone; }
}
