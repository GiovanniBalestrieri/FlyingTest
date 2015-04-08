package com.example.giovanni.bttest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

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
    TextView log;
    TextView roll,pitch,yaw,warning,state;

    static Handler handler;
    static final byte delimiter = 10;

    static public byte[] readBuffer;
    static int readBufferPosition;
    static int counter;

    static public Thread workerThread;
    static public boolean stopWorker;

    InputStream inputStream;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        final Bluetooth blue = new Bluetooth(getActivity().getApplicationContext(),this.getActivity());

        inputStream = blue.getInput();

        final byte delimiter = 10; //This is the ASCII code for a newline character
        handler = new Handler();
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];

        if (blue.isAssociated()) {
            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                        try {
                            int bytesAvailable = inputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                inputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                log.setText(data);
                                                Toast.makeText(getActivity().getApplicationContext(), data, Toast.LENGTH_LONG)
                                                        .show();
                                                char first = data.charAt(0);
                                                if (first=='o') {
                                                    // Orientation status
                                                    String values[] = data.replace("o,","").split(",");
                                                    String v1 = "";
                                                    String v2 = "";
                                                    String v3 = "";
                                                    if(values != null && values.length == 4) {
                                                        v1 = values[0];
                                                        v2 = values[1];
                                                        v3 = values[2];
                                                    }
                                                    roll.setText(v1);
                                                    pitch.setText(v2);
                                                    yaw.setText(v3);
                                                }
                                                else if (first=='c')
                                                {
                                                    // Commands
                                                    String values[] = data.replace("c","").split(",");
                                                    String v1 = "";
                                                    if(values != null && values.length == 3) {
                                                        v1 =  values[1];
                                                        if (v1.equals("p"))
                                                        {
                                                            warning.setTextColor(getResources().getColor(R.color.green));
                                                            warning.setText("Pid Enabled");
                                                        }else
                                                        {
                                                            warning.setTextColor(getResources().getColor(R.color.red));
                                                            warning.setText("Pid Disabled");
                                                        }
                                                    }
                                                }
                                                else if (first=='s') {
                                                    // TODO state.setText()
                                                }
                                            }
                                        });
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

        land.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (blue.isAssociated()) {
                    Log.e("CPanel Report", "Click Land");
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


        Log.e("Cpanel report", "view created.");
        return v;
    }

}