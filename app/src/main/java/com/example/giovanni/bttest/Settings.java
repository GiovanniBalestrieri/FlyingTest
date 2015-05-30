package com.example.giovanni.bttest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.giovanni.bttest.Libraries.DeviceListActivity;

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
    private Button On,Off,Visible,Scan,Find;
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
    private ProgressDialog mProgressDlg;

    ArrayList<HashMap<String, String>> devicesList;
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    IntentFilter filter = new IntentFilter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        android.view.View view = inflater.inflate(R.layout.settings, container, false);
        On = (Button) view.findViewById(R.id.turnOnBlu);
        Off = (Button) view.findViewById(R.id.turnOffBlu);
        Off.setText("Turn interface Off");
        Visible = (Button) view.findViewById(R.id.visibleBlu);
        Scan = (Button) view.findViewById(R.id.scanBlu);
        Find = (Button) view.findViewById(R.id.findBlu);
        toggle = (ToggleButton) view.findViewById(R.id.bluOnOff);
        bluList = (ListView) view.findViewById(R.id.blueDevices);

        take = (Button) view.findViewById(R.id.takeOffButt1);
        pid = (Button) view.findViewById(R.id.enablePidButt1);
        land = (Button) view.findViewById(R.id.landButt1);

        On.setVisibility(view.GONE);
        Off.setVisibility(view.GONE);

        final Bluetooth blue = new Bluetooth(getActivity().getApplicationContext(), this.getActivity());

        mBluetoothAdapter	= BluetoothAdapter.getDefaultAdapter();

        mProgressDlg 		= new ProgressDialog(getActivity());
        mProgressDlg.setMessage("Scanning...");
        mProgressDlg.setCancelable(false);
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mBluetoothAdapter.cancelDiscovery();
            }
        });

        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity().getApplicationContext(), " Sorry Bluetooth unsupported: ", Toast.LENGTH_SHORT)
                    .show();
        } else {
            land.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (blue.isAssociated()) {
                        Log.e("Settings Report", "Click land");
                        String msg = "L";
                        msg += "\n";
                        if (blue.blueWrite(msg)) {
                            Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), " Message error ", Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else {
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
                        } else {
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
                    if (blue.isAssociated()) {
                        Log.e("Settings Report", "Click take");
                        String msg = "a";
                        msg += "\n";
                        if (blue.blueWrite(msg)) {
                            Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                                    .show();
                        } else {
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
                    if (isChecked) {
                        blue.turnOn();
                    } else {
                        blue.turnOff();
                    }
                }
            });

            Visible.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    blue.getVisibility();
                }
            });

            Find.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mBluetoothAdapter.startDiscovery();
                }
            });

            Scan.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    devicesList = blue.getDevices(bluList);
                }
            });
        }

        // Register the BroadcastReceiver
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        getActivity().getApplicationContext().registerReceiver(mReceiver, filter);

        Log.e("Settings report", "view created.");
        return view;
    }

    public void onPause()
    {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }
        super.onPause();
    }

    // Broadcast reveiver for ACtion found state changed
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    showToast("Enabled");

                    //showEnabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();

                mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mProgressDlg.dismiss();

                Intent newIntent = new Intent(getActivity(), DeviceListActivity.class);

                newIntent.putParcelableArrayListExtra("device.list", mDeviceList);

                startActivity(newIntent);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mDeviceList.add(device);

                showToast("Found device " + device.getName());
            }
        }
    };


    private void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        getActivity().getApplicationContext().unregisterReceiver(mReceiver);

        super.onDestroy();
    }

}