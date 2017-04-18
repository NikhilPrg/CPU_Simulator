package com.kurama.nikhil.cpusimulator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class EditFragment extends Fragment {
    public EditFragment() {
    }
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View fl = inflater.inflate(R.layout.fragment_edit, container, false);
        return fl;
    }

}
