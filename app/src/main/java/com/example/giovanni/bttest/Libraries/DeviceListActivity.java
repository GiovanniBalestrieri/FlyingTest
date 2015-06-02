package com.example.giovanni.bttest.Libraries;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.giovanni.bttest.Bluetooth;
import com.example.giovanni.bttest.MainActivity;
import com.example.giovanni.bttest.R;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by userk on 29/05/15.
 */
public class DeviceListActivity extends Activity
{
    String intentConn = "connection";
    String intentTour = "tour";
    private ListView mListView;
    private DeviceListAdapter mAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_devices);

        mDeviceList		= getIntent().getExtras().getParcelableArrayList("device.list");
        mListView		= (ListView) findViewById(R.id.lv_paired);
        mAdapter		= new DeviceListAdapter(this);
        final Bluetooth blue = new Bluetooth(getApplicationContext(),this);

        mListView.setAdapter(mAdapter);
        registerReceiver(mPairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));

        mAdapter.setData(mDeviceList);
        mAdapter.setListener(new DeviceListAdapter.OnPairButtonClickListener() {
            @Override
            public void onPairButtonClick(int position) {
                BluetoothDevice device = mDeviceList.get(position);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED)
                {
                    // Connect to the device
                    BluetoothDevice deviceBlu = mDeviceList.get(position);
                    try {
                            blue.connect(deviceBlu.getAddress());
                            showToast("Handshake");
                            finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // If the device has not been paired you can start the association
                    showToast("Pairing...");
                    pairDevice(device);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mPairReceiver);
        super.onDestroy();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state 		= intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    showToast("Paired");
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    showToast("Unpaired");
                }

                mAdapter.notifyDataSetChanged();
            }
        }
    };
}
