package com.project.wanderlust.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.wanderlust.Activities.ActivityUserDetails;
import com.project.wanderlust.Adapters.AdapterContactsList;
import com.project.wanderlust.DataClasses.Contact;
import com.project.wanderlust.R;
import com.project.wanderlust.Others.SharedFunctions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FragmentContactsList extends Fragment implements RecyclerView.OnItemTouchListener
{
    Context c;

    public static ArrayList<Contact> contactslist = new ArrayList<>();
    AdapterContactsList adapter = new AdapterContactsList(null, R.layout.contact_cell);;

    RecyclerView rv;
    GestureDetector gestureDetector;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_contacts_list, container, false);

        return rootView;
    }

    @Override
    public  void onActivityCreated(Bundle b)
    {
        super.onActivityCreated(b);

        c = getContext();

        //-----------RECYCLER VIEW CODE-----------------//
        rv = getView().findViewById(R.id.contactRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.addOnItemTouchListener(this);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null)
                {
                    Contact j = contactslist.get(rv.getChildAdapterPosition(child));

                    Intent intent = new Intent(c, ActivityUserDetails.class);
                    intent.putExtra("phoneNumber", j.getPhone());
                    startActivity(intent);
                }

                return true;
            }
        }
        );

        adapter = new AdapterContactsList(contactslist, R.layout.contact_cell);
        rv.setAdapter(adapter);
        //---------------------------------------------//

        new LoadContacts().execute();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override public void onTouchEvent(RecyclerView rv, MotionEvent e) { }
    @Override public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }



    public class LoadContacts extends AsyncTask<Void, Integer, Void>
    {

        //ASYNC TASK TO LOAD CONTACTS

        private void loadContactsFromPhone() {
            final ArrayList<String> names = new ArrayList<>();
            final ArrayList<String> phones = new ArrayList<>();

            ContentResolver cr = getContext().getContentResolver();
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            while (cursor.moveToNext()) {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                //  Get all phone numbers.
                Cursor phones1 = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

                while (phones1.moveToNext()) {
                    String number = phones1.getString(phones1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\s+","");
                    String name = phones1.getString(phones1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    if(!phones.contains(number)) {
                        names.add(name);
                        phones.add(number);
                    }
                }
                phones1.close();
            }
            cursor.close();

            loadPeopleFromFirebase(names, phones);
        }

        private void loadPeopleFromFirebase(final ArrayList<String> names, final ArrayList<String> phones)
        {
            final Context context = getContext();
            FragmentContactsList.contactslist.clear();

            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    ContextWrapper wrapper = new ContextWrapper(context);
                    File file = wrapper.getDir("profilePictures", MODE_PRIVATE);
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profilePictures");

                    //going through all users present in firebase
                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        final String number = ds.getKey();

                        //checking if obtaining user is not logged in user or phone contact
                        if(!number.equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) && phones.contains(number))
                        {
                            final int i;
                            if((i = phones.indexOf(number)) != -1)
                            {
                                final File file1 = new File(file, number + ".jpg");

                                //user profile pic is present in phone
                                if(file1.exists()) {
                                    final Bitmap bitmap = SharedFunctions.decodeBitmapFromFile(file1, 100, 100);
                                    FragmentContactsList.contactslist.add(new Contact(bitmap, names.get(i), number));
                                }
                                else
                                {
                                    final StorageReference reference = storageRef.child(number + ".jpg");
                                    reference.getFile(file1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            final Bitmap bitmap = SharedFunctions.decodeBitmapFromFile(file1, 100, 100);
                                            FragmentContactsList.contactslist.add(new Contact(bitmap, names.get(i), number));
                                        }
                                    });
                                }
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override public void onCancelled(DatabaseError databaseError) { }
            });
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try{
                loadContactsFromPhone();

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(getContext(), "Contacts Loaded Successfully.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            catch (Exception e)
            {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(getContext(), "Error Loading Contacts..", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

    }








}
