package com.project.wanderlust;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private ArrayList<Contact> items;
    private int itemLayout;

    public ContactAdapter(ArrayList<Contact> items, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @NonNull @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        if(items != null) {
            Contact cell = items.get(position);

            if(cell.getPhoto() == null) {
                holder.photoImageView.setImageResource(R.drawable.account);
            } else {
                holder.photoImageView.setImageBitmap(cell.getPhoto());
            }
            holder.nameTextView.setText(cell.getName());
            holder.statusTextView.setText(cell.getStatus());
        }
    }

    @Override
    public int getItemCount() {
        if(items != null)
            return items.size();
        else
            return 0;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView photoImageView;
        public LinearLayout layout;
        public TextView nameTextView;
        public TextView statusTextView;
        public ContactViewHolder(View view) {
            super(view);

            photoImageView = view.findViewById(R.id.photo);
            layout = view.findViewById(R.id.layout);
            nameTextView = view.findViewById(R.id.person);
            statusTextView = view.findViewById(R.id.status);
        }
    }
}
