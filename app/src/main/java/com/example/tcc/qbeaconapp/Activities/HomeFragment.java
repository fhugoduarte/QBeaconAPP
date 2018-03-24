package com.example.tcc.qbeaconapp.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tcc.qbeaconapp.R;

public class HomeFragment extends Fragment{

    private TextView fragmentTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        fragmentTextView = (TextView) view.findViewById(R.id.fragmentTV);
        fragmentTextView.setText("HOME FRAGMENT");
        return(view);
    }

}
