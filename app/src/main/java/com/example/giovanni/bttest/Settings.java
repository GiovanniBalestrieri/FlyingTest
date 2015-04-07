package com.example.giovanni.bttest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * Created by userk on 06/04/15.
 */
public class Settings extends Fragment
{
    private static final int REQUEST_ENABLE_BT = 0;
    private static final String TAG_NAME = "name";
    private static final String TAG_ID = "id";
    private Button On,Off,Visible,Scan;
    private Button take,pid,land;
    private ListView bluList;
    private ToggleButton toggle;

    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    BluetoothAdapter mBluetoothAdapter;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    ArrayList<HashMap<String, String>> devicesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.settings, container, false);
        On = (Button) view.findViewById(R.id.turnOnBlu);
        Off = (Button) view.findViewById(R.id.turnOffBlu);
        Off.setText("Turn interface Off");
        Visible = (Button) view.findViewById(R.id.visibleBlu);
        Scan = (Button) view.findViewById(R.id.scanBlu);
        toggle = (ToggleButton) view.findViewById(R.id.bluOnOff);
        bluList = (ListView) view.findViewById(R.id.blueDevices);

        take = (Button) view.findViewById(R.id.takeOffButt1);
        pid = (Button) view.findViewById(R.id.enablePidButt1);
        land = (Button) view.findViewById(R.id.landButt1);

        On.setVisibility(view.GONE);
        Off.setVisibility(view.GONE);

        final Bluetooth blue = new Bluetooth(getActivity().getApplicationContext(),this.getActivity());

        land.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (blue.isAssociated()) {
                    Log.e("Settings Report", "Click land");
                    String msg = "L";
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

        pid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (blue.isAssociated()) {
                    Log.e("Settings Report", "Click pid");
                    String msg = "p";
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                                .show();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), " Message error ", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), " No device found. Connect first!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        take.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (blue.isAssociated())
                {
                    Log.e("Settings Report", "Click take");
                    String msg = "a";
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                                .show();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), " Message error ", Toast.LENGTH_LONG)
                            .show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), " No device found. Connect first!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    blue.turnOn();
                } else
                {
                    blue.turnOff();
                }
            }
        });

        Visible.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                blue.getVisibility();
            }
        });

        Scan.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                devicesList = blue.getDevices(bluList);
            }
        });

        Log.e("Settings report","view created.");
        return view;
    }
}