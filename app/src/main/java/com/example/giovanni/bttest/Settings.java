package com.example.giovanni.bttest;

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

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }


        land.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("Settings Report", "Click land");
                String msg = "L";
                msg += "\n";
                try {
                    mmOutputStream.write(msg.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        pid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("Settings Report", "Click pid");
                String msg = "p";
                msg += "\n";
                try {
                    mmOutputStream.write(msg.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        take.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("Settings Report", "Click take");
                String msg = "a";
                msg += "\n";
                try {
                    mmOutputStream.write(msg.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Log.e("Settings Report", "Turning On Bluetooth");
                    // Turn On Bluetooth
                    if (!mBluetoothAdapter.isEnabled())
                    {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        Toast.makeText(getActivity().getApplicationContext(), "Turned on"
                                , Toast.LENGTH_LONG).show();
                    } else
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "Already on",
                                Toast.LENGTH_LONG).show();
                    }
                } else
                {
                    Log.e("Settings Report", "Turning Off Bluetooth");
                    mBluetoothAdapter.disable();
                    Toast.makeText(getActivity().getApplicationContext(), "Turned off",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        On.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("Settings Report", "Turning On Bluetooth");
                // Turn On Bluetooth
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    Toast.makeText(getActivity().getApplicationContext(), "Turned on"
                            , Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Already on",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        Off.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Turn On Bluetooth
                Log.e("Settings Report", "Turning Off Bluetooth");
                mBluetoothAdapter.disable();
                Toast.makeText(getActivity().getApplicationContext(), "Turned off",
                        Toast.LENGTH_LONG).show();
            }
        });

        Visible.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("Settings Report", "Requesting visibility");
                // Turn device visible - Bluetooth
                Intent getVisible = new Intent(mBluetoothAdapter.
                        ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(getVisible, 0);
            }
        });

        Scan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.e("Settings Report", "Requesting bluetooth devices");
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                devicesList = new ArrayList<HashMap<String, String>>();
                //ArrayList list = new ArrayList();
                if (pairedDevices.size() > 0) {
                    HashMap<String, String> devices = new HashMap<String, String>();
                    // Loop through paired devices
                    for (BluetoothDevice device : pairedDevices) {
                        devices.put(TAG_ID, device.getAddress());
                        devices.put(TAG_NAME, device.getName());
                        devicesList.add(devices);
                        Log.e("Settings Report", "Added: " + device.getName() + "with ID: " + device.getName());
                    }


                    Toast.makeText(getActivity().getApplicationContext(), "Showing Paired Devices",
                            Toast.LENGTH_SHORT).show();

                    BluAdapter adapter = new BluAdapter(getActivity(), devicesList);
                    bluList.setAdapter(adapter);

                    bluList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            String map = (String) parent.getItemAtPosition(position).toString();
                            Log.e("BluList Report", "Clicked list item: " +
                                    position + " Device's name: \n" + devicesList.get(position).get(TAG_NAME).toString());

                            try {
                                openBlueDev(devicesList.get(position).get(TAG_ID).toString());
                            }
                            catch (IOException ex)
                            {
                                Log.e("BluList Report", " !!! Something went wrong - Device's name: \n" + devicesList.get(position).get(TAG_NAME).toString());
                            }

                            //String item = (String) parent.getItemAtPosition(position);
                            Log.e("Settings report", "Requesting connection to device: " + devicesList.get(position).get(TAG_NAME).toString());
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Connected: " + devicesList.get(position).get(TAG_NAME).toString(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }
        });

        Log.e("Settings report","view created.");
        return view;
    }

    void openBlueDev(String uid) throws IOException
    {
        BluetoothSocket mmSocket = null;

        //UUID uuid = UUID.fromString(uid);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(uid);
        try {
            mmSocket = device.createRfcommSocketToServiceRecord(uuid);
        }
        catch (IOException e) {
            // Qualcosa
        }
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try
        {
            mmSocket.connect();
            //out.append("\n...Connection established and data link opened...");
        } catch (IOException e) {
            try {
                mmSocket.close();
            } catch (IOException e2) {
                //AertBox("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                Toast.makeText(getActivity().getApplicationContext(), "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".", Toast.LENGTH_LONG)
                        .show();
            }
        }
        //mmSocket.connect();
        mmInputStream = mmSocket.getInputStream();

        try {
            mmOutputStream = mmSocket.getOutputStream();
        } catch (IOException e) {

            Log.e("Settings report","Output stream creation failed:" + e.getMessage() + ".");
        }
    }
}