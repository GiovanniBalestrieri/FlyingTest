package com.example.giovanni.bttest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by userk on 06/04/15.
 */
public class Settings extends Fragment
{
    private static final int REQUEST_ENABLE_BT = 0;
    private Button On,Off,Visible,Scan;
    private ListView bluList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.settings, container, false);
        On = (Button) view.findViewById(R.id.turnOnBlu);
        Off = (Button) view.findViewById(R.id.turnOffBlu);
        Off.setText("Turn interface Off");
        Visible = (Button) view.findViewById(R.id.visibleBu);
        Scan = (Button) view.findViewById(R.id.scanBlu);

        bluList = (ListView) view.findViewById(R.id.blueDevices);

        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        On.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Log.e("Settings Report", "Turning On Bluetooth");
                // Turn On Bluetooth
                if (!mBluetoothAdapter.isEnabled())
                {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    Toast.makeText(getActivity().getApplicationContext(), "Turned on"
                            , Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Already on",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        Off.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Turn On Bluetooth
                Log.e("Settings Report", "Turning Off Bluetooth");
                mBluetoothAdapter.disable();
                Toast.makeText(getActivity().getApplicationContext(),"Turned off" ,
                        Toast.LENGTH_LONG).show();
            }
        });

        Visible.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
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

                ArrayList list = new ArrayList();
                if (pairedDevices.size() > 0)
                {
                    // Loop through paired devices
                    for (BluetoothDevice device : pairedDevices)
                        list.add(device.getName() + "\n" + device.getAddress());

                    Toast.makeText(getActivity().getApplicationContext(), "Showing Paired Devices",
                            Toast.LENGTH_SHORT).show();
                    ArrayAdapter adapter = new ArrayAdapter
                            (getActivity(), android.R.layout.simple_list_item_1, list);
                    bluList.setAdapter(adapter);
                }
            }

        });

        Log.e("Settings report","view created.");
        return view;
    }
}