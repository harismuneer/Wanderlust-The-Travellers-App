package com.project.wanderlust;

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

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ContactsFragment extends Fragment implements RecyclerView.OnItemTouchListener{
    GestureDetector gestureDetector;
    Context c;
    RecyclerView rv;
    final static ArrayList<Contact> contactslist = new ArrayList<>();
    ContactAdapter adapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        c = getContext();

        rv = getView().findViewById(R.id.contactRecyclerView);

        gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null)
                {
                    int position = rv.getChildAdapterPosition(child);
                    Intent intent = new Intent(c, UserDetailsActivity.class);
                    intent.putExtra("phoneNumber", contactslist.get(position).getPhone());
                    startActivity(intent);
                }
                return true;
            }
        });

        loadContactsFromPhone();
    }

    //Kindly add some comments for this function that what is it doing in each line
    private void loadContactsFromPhone() {
        final ArrayList<String> names = new ArrayList<>();
        final ArrayList<String> phones = new ArrayList<>();

        ContentResolver cr = getActivity().getContentResolver();
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
        contactslist.clear();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i;
                ContextWrapper wrapper = new ContextWrapper(context);
                File file = wrapper.getDir("profilePictures", MODE_PRIVATE);
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profilePictures");
                //going through all users present in firebase
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    String number = ds.getKey();
                    String status = ds.child("status").getValue(String.class);
                    //checking if obtaining user is not logged in user or phone contact
                    if(!number.equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) && phones.contains(number)) {
                        if((i = phones.indexOf(number)) != -1) {
                            final File file1 = new File(file, number + ".jpg");
                            //user profile pic is present in phone
                            if(file1.exists()) {
                                final Bitmap bitmap = SharedFunctions.decodeBitmapFromFile(file1, 100, 100);
                                contactslist.add(new Contact(bitmap, names.get(i), status, number));
                            }
                            else {
                                final StorageReference reference = storageRef.child(number + ".jpg");
                                reference.getFile(file1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) { }
                                });
                                contactslist.add(new Contact(null, names.get(i), status, number));
                            }
                        }
                    }
                }
                viewFriends();
            }

            @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private void viewFriends() {
        adapter = new ContactAdapter(contactslist, R.layout.contact_cell);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.addOnItemTouchListener(this);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override public void onTouchEvent(RecyclerView rv, MotionEvent e) { }
    @Override public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
}
