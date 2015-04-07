package com.example.giovanni.bttest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by userk on 07/04/15.
 */
public class Bluetooth {
    public static int TURNED_ON = 1;
    public static int TURNED_OFF = 0;
    public static int ASSOCIATED = 2;
    public static int NOT_ASSOCIATED = 3;
    private static final String TAG_NAME = "name";
    private static final String TAG_ID = "id";

    private static final int REQUEST_ENABLE_BT = 0;
    private static BluetoothAdapter mBluetoothAdapter;
    private static Activity activity;
    public static BluetoothDevice device;
    public static BluetoothSocket mmSocket;
    public static OutputStream mmOutputStream;
    public static InputStream mmInputStream;
    public static boolean associated = false;

    public static ArrayList<HashMap<String, String>> devicesList;

    private static Context ctx;

    public Bluetooth(Context context,Activity acti)
    {
        super();
        ctx=context;
        this.activity = acti;

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null)
        {
            // Device does not support Bluetooth
        }
    }

    public int turnOn()
    {
        int result;
        Log.e("Settings Report", "Turning On Bluetooth");
        // Turn On Bluetooth
        if (!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Toast.makeText(ctx, "Turned on"
                    , Toast.LENGTH_LONG).show();
            result = 1;
        } else
        {
            Toast.makeText(ctx, "Already on",
                    Toast.LENGTH_LONG).show();
            result = 0;
        }
        return result;
    }

    public void turnOff()
    {
        Log.e("Bluetooth Class Report", "Turning Off Bluetooth");
        mBluetoothAdapter.disable();
        Toast.makeText(activity.getApplicationContext(), "Turned off",
                Toast.LENGTH_LONG).show();
        associated = false;
    }

    public static int getVisibility()
    {
        Log.e("Bluetooth Class Report", "Requesting visibility");
        // Turn device visible - Bluetooth
        Intent getVisible = new Intent(mBluetoothAdapter.
                ACTION_REQUEST_DISCOVERABLE);
        activity.startActivityForResult(getVisible, 0);
        return 1;
    }

    public static ArrayList<HashMap<String, String>> getDevices(ListView list) {
        Log.e("Bluetooth Class Report", "Requesting bluetooth devices");
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
                Log.e("Bluetooth Class Report", "Added: " + device.getName() + "with ID: " + device.getName());
            }

        }
        BluAdapter adapter = new BluAdapter(activity, devicesList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String map = (String) parent.getItemAtPosition(position).toString();
                Log.e("Bluetooth Class Report", "Clicked list item: " +
                        position + " Device's name: \n" + devicesList.get(position).get(TAG_NAME).toString());

                try {
                    connect(devicesList.get(position).get(TAG_ID).toString());
                }
                catch (IOException ex)
                {
                    Log.e("Bluetooth Class Report", " !!! Something went wrong - Device's name: \n" + devicesList.get(position).get(TAG_NAME).toString());
                }

                //String item = (String) parent.getItemAtPosition(position);
                Log.e("Bluetooth Class Report", "Requesting connection to device: " + devicesList.get(position).get(TAG_NAME).toString());
                Toast.makeText(ctx,"Connected: " + devicesList.get(position).get(TAG_NAME).toString(), Toast.LENGTH_LONG).show();
            }
        });
        return devicesList;
    }

    public static void connect(String uid) throws IOException
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
            associated = false;
        }
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try
        {
            mmSocket.connect();
            associated = true;
            //out.append("\n...Connection established and data link opened...");
        } catch (IOException e) {
            try {
                mmSocket.close();
                associated = false;
            } catch (IOException e2) {
                //AertBox("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                Toast.makeText(activity.getApplicationContext(), "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".", Toast.LENGTH_LONG)
                        .show();
                associated = false;
            }
        }
        try {
            mmInputStream = mmSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mmOutputStream = mmSocket.getOutputStream();
        } catch (IOException e) {

            Log.e("Settings report","Output stream creation failed:" + e.getMessage() + ".");
        }
    }

    public boolean isAssociated()
    {
        if (associated)
            return true;
        else
            return false;
    }

    public boolean blueWrite(String s)
    {
        try {
            mmOutputStream.write(s.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
