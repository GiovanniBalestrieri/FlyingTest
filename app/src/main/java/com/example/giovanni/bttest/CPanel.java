package com.example.giovanni.bttest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Giovanni on 02/04/2015.
 */
public class CPanel extends Fragment
{
    LinearLayout takeOff;
    LinearLayout land;
    Button status;
    Button test;
    Button pid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.event, container, false);

        takeOff = (LinearLayout) v.findViewById(R.id.takeOff);
        land = (LinearLayout) v.findViewById(R.id.land);
        test = (Button) v.findViewById(R.id.testConn);
        status = (Button) v.findViewById(R.id.statusButt);
        pid = (Button) v.findViewById(R.id.enablePidButt);

        takeOff.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                //blue.getVisibility();
            }
        });


        Log.e("Cpanel report", "view created.");
        return v;
    }
}