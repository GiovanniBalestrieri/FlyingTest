package com.example.giovanni.bttest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by userk on 09/04/15.
 */
public class Control extends Fragment {
    LinearLayout takeOff;
    LinearLayout land;
    InputStream inputStream;
    private LinearLayout aRLL, wRLL, aPLL, wPLL, aYLL, wYLL;
    private Switch aRoll,wRoll,aPitch,wPitch,aYaw,wYaw;

    static Handler handler;
    static final byte delimiter = 10;

    static public byte[] readBuffer;
    static int readBufferPosition;
    static int counter;

    static public Thread workerThread;
    static public boolean stopWorker;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        android.view.View v = inflater.inflate(R.layout.control, container, false);

        takeOff = (LinearLayout) v.findViewById(R.id.takeOff);
        land = (LinearLayout) v.findViewById(R.id.land);

        final Bluetooth blue = new Bluetooth(getActivity().getApplicationContext(), this.getActivity());

        inputStream = blue.getInput();

        // ToggleBtns

        aRoll = (Switch) v.findViewById(R.id.switchARoll);
        wRoll = (Switch) v.findViewById(R.id.switchWRoll);
        aPitch = (Switch) v.findViewById(R.id.switchAPitch);
        wPitch = (Switch) v.findViewById(R.id.switchWPitch);
        aYaw = (Switch) v.findViewById(R.id.switchAYaw);
        wYaw = (Switch) v.findViewById(R.id.switchWYaw);

        // LinearLayouts

        aRLL = (LinearLayout) v.findViewById(R.id.ARollView);
        wRLL = (LinearLayout) v.findViewById(R.id.WRollView);
        aPLL = (LinearLayout) v.findViewById(R.id.APitchView);
        wPLL = (LinearLayout) v.findViewById(R.id.WPitchView);
        aYLL = (LinearLayout) v.findViewById(R.id.AYawView);
        wYLL = (LinearLayout) v.findViewById(R.id.WYawView);

        //TODO
        // Add a state check and modify
        //aRoll.setChecked(true);

        //check the current state before we display the screen
        if(aRoll.isChecked())
        {
            aRLL.setVisibility(LinearLayout.VISIBLE);
        }
        else
        {
            aRLL.setVisibility(LinearLayout.GONE);
        }

        //attach a listener to check for changes in state
        aRoll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    //aRoll.setText("Switch is currently ON");
                    aRLL.setVisibility(LinearLayout.VISIBLE);
                }else{
                    //aRoll.setText("Switch is currently OFF");
                    aRLL.setVisibility(LinearLayout.GONE);
                }
            }
        });


        //TODO
        // Add a state check and modify
        //aRoll.setChecked(true);

        //check the current state before we display the screen
        if(aPitch.isChecked()){
            aPLL.setVisibility(LinearLayout.VISIBLE);
        }
        else {
            aPLL.setVisibility(LinearLayout.GONE);
        }

        //attach a listener to check for changes in state
        aPitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    //aRoll.setText("Switch is currently ON");
                    aPLL.setVisibility(LinearLayout.VISIBLE);
                }else{
                    //aRoll.setText("Switch is currently OFF");
                    aPLL.setVisibility(LinearLayout.GONE);
                }
            }
        });

        //TODO
        // Add a state check and modify
        //aRoll.setChecked(true);

        //check the current state before we display the screen
        if(aYaw.isChecked()){
            aYLL.setVisibility(LinearLayout.VISIBLE);
        }
        else {
            aYLL.setVisibility(LinearLayout.GONE);
        }

        //attach a listener to check for changes in state
        aYaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    //aRoll.setText("Switch is currently ON");
                    aYLL.setVisibility(LinearLayout.VISIBLE);
                }else{
                    //aRoll.setText("Switch is currently OFF");
                    aYLL.setVisibility(LinearLayout.GONE);
                }
            }
        });

        //TODO
        // Add a state check and modify
        //aRoll.setChecked(true);

        //check the current state before we display the screen
        if(wRoll.isChecked()){
            wRLL.setVisibility(LinearLayout.VISIBLE);
        }
        else {
            wRLL.setVisibility(LinearLayout.GONE);
        }

        //attach a listener to check for changes in state
        wRoll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    //aRoll.setText("Switch is currently ON");
                    wRLL.setVisibility(LinearLayout.VISIBLE);
                }else{
                    //aRoll.setText("Switch is currently OFF");
                    wRLL.setVisibility(LinearLayout.GONE);
                }
            }
        });

        //TODO
        // Add a state check and modify
        //aRoll.setChecked(true);

        //check the current state before we display the screen
        if(wPitch.isChecked()){
            wPLL.setVisibility(LinearLayout.VISIBLE);
        }
        else {
            wPLL.setVisibility(LinearLayout.GONE);
        }

        //attach a listener to check for changes in state
        wPitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    //aRoll.setText("Switch is currently ON");
                    wPLL.setVisibility(LinearLayout.VISIBLE);
                }else{
                    //aRoll.setText("Switch is currently OFF");
                    wPLL.setVisibility(LinearLayout.GONE);
                }
            }
        });

        //TODO
        // Add a state check and modify
        //aRoll.setChecked(true);

        //check the current state before we display the screen
        if(wYaw.isChecked()){
            wYLL.setVisibility(LinearLayout.VISIBLE);
        }
        else {
            wYLL.setVisibility(LinearLayout.GONE);
        }

        //attach a listener to check for changes in state
        wYaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    //aRoll.setText("Switch is currently ON");
                    wYLL.setVisibility(LinearLayout.VISIBLE);
                }else{
                    //aRoll.setText("Switch is currently OFF");
                    wYLL.setVisibility(LinearLayout.GONE);
                }
            }
        });

        /*
        if (blue.isAssociated())
        {
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
                                                //Toast.makeText(getActivity().getApplicationContext(), data, Toast.LENGTH_LONG)
                                                //        .show();
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
        */
        return v;
    }
}
