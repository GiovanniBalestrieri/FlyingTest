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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giovanni.bttest.Libraries.DeviceListActivity;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by userk on 02/05/15.
 */
public class Connection extends  Activity
{
    Button tour;
    LinearLayout connect;
    TextView logs;

    String intentConn = "connection";
    String intentTour = "tour";

    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    BluetoothAdapter mBluetoothAdapter;
    private ProgressDialog mProgressDlg;
    ArrayList<HashMap<String, String>> devicesList;
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    IntentFilter filter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect);
        tour = (Button) findViewById(R.id.tour);
        tour.setVisibility(View.GONE);
        connect = (LinearLayout) findViewById(R.id.btnConnect);
        //logs = (TextView) findViewById(R.id.connectionLogs);

        mBluetoothAdapter	= BluetoothAdapter.getDefaultAdapter();


        final Bluetooth blue = new Bluetooth(getApplicationContext(),this);

        mProgressDlg 		= new ProgressDialog(this);
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
            Toast.makeText(getApplicationContext(), " Sorry Bluetooth unsupported: ", Toast.LENGTH_SHORT)
                    .show();
        } else {
            int con;
            connect.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    int con;
                    //mBluetoothAdapter.startDiscovery();int con;
                    if (blue.isAssociated())
                        con = 1;
                    else
                        con = 0;
                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(intentConn, String.valueOf(con));
                    extras.putString(intentTour,"1");
                    i.putExtras(extras);
                    startActivity(i);
                    // Close Registration View
                    finish();
                }
            });
        }

        tour.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                int con;
                if (blue.isAssociated())
                    con = 1;
                else
                    con = 0;
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                Bundle extras = new Bundle();
                extras.putString(intentConn, String.valueOf(con));
                extras.putString(intentTour,"1");
                i.putExtras(extras);
                startActivity(i);
                // Close Registration View
                finish();
            }
        });


        // Register the BroadcastReceiver
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        getApplicationContext().registerReceiver(mReceiver, filter);
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

                Intent newIntent = new Intent(getApplicationContext(), DeviceListActivity.class);

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
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        getApplicationContext().unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
