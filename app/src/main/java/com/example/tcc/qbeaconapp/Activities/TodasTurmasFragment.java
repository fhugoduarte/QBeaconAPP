package com.example.tcc.qbeaconapp.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tcc.qbeaconapp.R;

/**
 * Created by hugoduarte on 24/03/18.
 */

public class TodasTurmasFragment extends Fragment {

    private TextView fragmentTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_todas_turmas, container, false);
        fragmentTextView = (TextView) view.findViewById(R.id.fragmentTV3);
        fragmentTextView.setText("TODAS TURMAS FRAGMENT");
        return(view);
    }

}
