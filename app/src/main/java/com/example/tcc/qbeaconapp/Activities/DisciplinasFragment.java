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

public class DisciplinasFragment extends Fragment {

    private TextView fragmentTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_disciplinas, container, false);
        fragmentTextView = (TextView) view.findViewById(R.id.fragmentTV4);
        fragmentTextView.setText("DISCIPLINAS FRAGMENT");
        return(view);
    }

}
