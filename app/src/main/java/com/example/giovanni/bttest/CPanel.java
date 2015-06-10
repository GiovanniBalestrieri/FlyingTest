package com.example.giovanni.bttest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giovanni.bttest.Libraries.sendStartTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Giovanni on 02/04/2015.
 */
public class CPanel extends Fragment
{
    LinearLayout takeOff;
    LinearLayout land;
    LinearLayout map;
    LinearLayout orient;
    Button status;
    Button test;
    Button pid;
    TextView log;
    TextView roll,pitch,yaw,warning,state;
    private Switch pidSwitch,orientSwitch;

    static Handler handler;
    static final byte delimiter = 10;

    static public byte[] readBuffer;
    static int readBufferPosition;
    static int counter;

    static public Thread workerThread;
    static public boolean stopWorker;

    InputStream inputStream;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.event, container, false);

        takeOff = (LinearLayout) v.findViewById(R.id.takeOff);
        land = (LinearLayout) v.findViewById(R.id.land);
        test = (Button) v.findViewById(R.id.testConn);
        status = (Button) v.findViewById(R.id.statusButt);
        pid = (Button) v.findViewById(R.id.enablePidButt);
        log = (TextView) v.findViewById(R.id.log1);
        roll = (TextView) v.findViewById(R.id.rollTxt);
        pitch = (TextView) v.findViewById(R.id.pitchTxt);
        yaw = (TextView) v.findViewById(R.id.yawTxt);
        state = (TextView) v.findViewById(R.id.state);
        warning = (TextView) v.findViewById(R.id.warning);
        map = (LinearLayout) v.findViewById(R.id.luogoButtonLayout);
        orient = (LinearLayout) v.findViewById(R.id.infosOrientationPage);
        orientSwitch = (Switch) v.findViewById(R.id.getOrientationSlider);
        pidSwitch = (Switch) v.findViewById(R.id.enPidSlider);

        final Bluetooth blue = new Bluetooth(getActivity().getApplicationContext(),this.getActivity());

        inputStream = blue.getInput();

        final byte delimiter = 10; //This is the ASCII code for a newline character
        handler = new Handler();
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];

        if (blue.isAssociated())
        {
            workerThread = new Thread(new Runnable()
            {
                public void run()
                {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker)
                    {
                        try
                        {
                            int bytesAvailable = inputStream.available();
                            if (bytesAvailable > 0)
                            {
                                byte[] packetBytes = new byte[bytesAvailable];
                                inputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++)
                                {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                log.setText(data);
                                                if (data.length() >= 1)
                                                {
                                                    char first = data.charAt(0);
                                                    if (first == 'o') {
                                                        // Orientation status
                                                        String values[] = data.replace("o,", "").split(",");
                                                        String v1 = "";
                                                        String v2 = "";
                                                        String v3 = "";
                                                        if (values != null && values.length == 4) {
                                                            v1 = values[0];
                                                            v2 = values[1];
                                                            v3 = values[2];
                                                        }
                                                        roll.setText(v1);
                                                        pitch.setText(v2);
                                                        yaw.setText(v3);
                                                    } else if (first == 'c') {
                                                        // Commands
                                                        String values[] = data.replace("c", "").split(",");
                                                        String v1 = "";
                                                        if (values != null && values.length == 3) {
                                                            v1 = values[1];
                                                            if (v1.equals("p")) {
                                                                warning.setTextColor(getResources().getColor(R.color.green));
                                                                warning.setText("Pid Enabled");
                                                            } else {
                                                                warning.setTextColor(getResources().getColor(R.color.red));
                                                                warning.setText("Pid Disabled");
                                                            }
                                                        }
                                                    } else if (first == 's') {
                                                        // TODO state.setText()
                                                    } else if (first == 'K') {
                                                        blue.associated = true;
                                                        blue.blueWrite("K");
                                                    } else if (first == 'A') {
                                                        showToast("Ack Received");
                                                    } else {
                                                        // Received non correct message
                                                        //showToast(data);
                                                    }
                                                }

                                            }
                                        });
                                    // clean buffer
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }
                    }
                }
            });
            workerThread.start();
        }

        takeOff.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (blue.isAssociated()) {
                    Log.e("CPanel Report", "Click TakeOff");
                    String msg = "a";
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        //Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                        //        .show();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), " Message error ", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(), " No device found. Connect first!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        land.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (blue.isAssociated()) {
                    Log.e("CPanel Report", "Click Land");
                    String msg = "L";
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        //Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                        //        .show();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), " Message error ", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(), " No device found. Connect first!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        status.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (blue.isAssociated()) {
                    Log.e("CPanel Report", "Click Status");
                    String msg = "s";
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                                .show();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), " Message error ", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(), " No device found. Connect first!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        test.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (blue.isAssociated()) {
                    Log.e("CPanel Report", "Click Test");
                    String msg = "t";
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                                .show();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), " Message error ", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(), " No device found. Connect first!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        pid.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (blue.isAssociated()) {
                    Log.e("CPanel Report", "Click Status");
                    String msg = "s";
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                                .show();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), " Message error ", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(), " No device found. Connect first!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        //TODO
        // Add a state check and modify
        //aRoll.setChecked(true);

        //check the current state before we display the screen
        if(orientSwitch.isChecked())
        {
            orient.setVisibility(LinearLayout.VISIBLE);
        }
        else
        {
            orient.setVisibility(LinearLayout.GONE);
        }

        //attach a listener to check for changes in state
        orientSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked)
            {
                //aRoll.setText("Switch is currently ON");
                orient.setVisibility(LinearLayout.VISIBLE);
                if (blue.isAssociated())
                {
                    String msg;
                    Log.e("CPanel Report", "Click Status");
                    if(orientSwitch.isChecked())
                        msg = "te";
                    else
                    {
                        msg = "td";
                        orient.setVisibility(LinearLayout.GONE);
                    }
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                                .show();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), " Message error ", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(), " No device found. Connect first!", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });

        //attach a listener to check for changes in state
        pidSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked)
            {
                if (blue.isAssociated())
                {
                    Log.e("CPanel Report", "Click Status");
                    String msg;
                    if(pidSwitch.isChecked())
                    {
                         msg = "pe";
                    }
                    else {
                        msg = "pd";
                    }
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                                .show();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), " Message error ", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(), " No device found. Connect first!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        /*
        map.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Exploration firstFragment = new Exploration();
                transaction.addToBackStack("OK");
                transaction.replace(R.id.container, firstFragment);
                transaction.commit();
            }
        });
        */
        map.setOnClickListener(new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Intent map = new Intent(getActivity().getApplicationContext(), RoverPosition.class);
            //map.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(map);
        }
    });

    Log.e("Cpanel report", "view created.");
    return v;

    }
    private void setUpMapIfNeeded()
    {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null)
        {
            mMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null)
            {

            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}