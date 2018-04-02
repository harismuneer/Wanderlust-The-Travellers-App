package com.project.wanderlust;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.ArrayList;

public class Contacts extends AppCompatActivity implements RecyclerView.OnItemTouchListener{

    public static ArrayList<String> names = new ArrayList<>();
    public static ArrayList<String> phones = new ArrayList<>();

    GestureDetector gestureDetector;
    Context c;
    RecyclerView rv;
    ArrayList<ContactCell> arrayList;
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        c = this;
        rv = findViewById(R.id.contactRecyclerView);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }
        });

        loadContactsFromPhone();
        int farhan = 0;
        arrayList = new ArrayList<>();
        for(int i = 0; i < names.size(); i++) {
            arrayList.add(new ContactCell(null, names.get(i), phones.get(i), "vfvd"));
        }

        adapter = new ContactAdapter(arrayList, R.layout.contact_cell);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addOnItemTouchListener(this);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
    }

    private void loadContactsFromPhone() {
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            //  Get all phone numbers.
            Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

            while (phones.moveToNext()) {
                String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\s+","");
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if(!Contacts.phones.contains(number)) {
                    Contacts.names.add(name);
                    Contacts.phones.add(number);
                }
            }
            phones.close();
        }
        cursor.close();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override public void onTouchEvent(RecyclerView rv, MotionEvent e) { }
    @Override public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
}
