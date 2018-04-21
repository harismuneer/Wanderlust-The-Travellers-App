package com.project.wanderlust;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SaveImages extends AsyncTask<Void, Integer, Void> {
    private Context context;
    private String time;
    private ArrayList<Bitmap> photos;

    public SaveImages(Context context, String time, ArrayList<Bitmap> photos) {
        this.context = context;
        this.time = time;
        this.photos = photos;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ContextWrapper wrapper = new ContextWrapper(context);
        File file = wrapper.getDir(time, MODE_PRIVATE);

        int size = photos.size();
        for(int i = 0; i < size; i++) {
            File file1 = new File(file, i + ".jpg");
            try {
                OutputStream stream = new FileOutputStream(file1);
                photos.get(i).compress(Bitmap.CompressFormat.JPEG,100,stream);
                stream.flush();
                stream.close();

                Toast.makeText(context, Integer.toString(i), Toast.LENGTH_LONG).show();
            } catch (Exception e) {}
        }
        return null;
    }
}
