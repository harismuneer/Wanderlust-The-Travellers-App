package com.project.wanderlust.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.wanderlust.R;

/**
 * Created by PAKLAP.pk on 01-May-18.
 */

public class FragmentNotesList extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_notes_list, container, false);

        return rootView;
    }

}
