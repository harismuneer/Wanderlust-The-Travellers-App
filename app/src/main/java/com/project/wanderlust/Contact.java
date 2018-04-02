package com.project.wanderlust;

import android.graphics.Bitmap;

public class Contact {
    private Bitmap photo;
    private String name;
    private String status;
    private String phone;

    public Contact(Bitmap photo, String name, String status, String phone) {
        this.photo = photo;
        this.name = name;
        this.status = status;
        this.phone = phone;
    }

    public Bitmap getPhoto() { return photo; }

    public String getName() { return name; }

    public String getStatus() { return status; }

    public String getPhone() { return phone; }

    public void setPhoto(Bitmap photo) { this.photo = photo; }

    public void setName(String name) { this.name = name; }

    public void setStatus(String status) { this.status = status; }

    public void setPhone(String phone) { this.phone = phone; }
}
