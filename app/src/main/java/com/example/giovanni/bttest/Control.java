package com.example.giovanni.bttest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
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
    private SeekBar KpAR,KiAR,KdAR,KpAP,KiAP,KdAP,KpAY,KiAY,KdAY;
    private SeekBar KpwR,KiwR,KdwR,KpwP,KiwP,KdwP,KpwY,KiwY,KdwY;
    private TextView KpARVal,KiARVal,KdARVal,KpAPVal,KiAPVal,KdAPVal,KpAYVal,KiAYVal,KdAYVal;
    private TextView KpwRVal,KiwRVal,KdwRVal,KpwPVal,KiwPVal,KdwPVal,KpwYVal,KiwYVal,KdwYVal;
    private Button ARRec,ARSen,APRec,APSen,AYRec,AYSen,WRRec,WRSen,WPRec,WPSen,WYRec,WYSen;

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

        // SeekBars

        // Angular Roll
        KpAR = (SeekBar) v.findViewById(R.id.KpARseek);
        KdAR = (SeekBar) v.findViewById(R.id.KdARseek);
        KiAR = (SeekBar) v.findViewById(R.id.KiARseek);
        // Angular Pitch
        KpAP = (SeekBar) v.findViewById(R.id.KpAPseek);
        KdAP = (SeekBar) v.findViewById(R.id.KdAPseek);
        KiAP = (SeekBar) v.findViewById(R.id.KiAPseek);
        // Angular Yaw
        KpAY = (SeekBar) v.findViewById(R.id.KpAYseek);
        KdAY = (SeekBar) v.findViewById(R.id.KdAYseek);
        KiAY = (SeekBar) v.findViewById(R.id.KiAYseek);
        // w Vel Roll
        KpwR = (SeekBar) v.findViewById(R.id.KpWRseek);
        KdwR = (SeekBar) v.findViewById(R.id.KdWRseek);
        KiwR = (SeekBar) v.findViewById(R.id.KiWRseek);
        // w Vel Pitch
        KpwP = (SeekBar) v.findViewById(R.id.KpWPseek);
        KdwP = (SeekBar) v.findViewById(R.id.KdWPseek);
        KiwP = (SeekBar) v.findViewById(R.id.KiWPseek);
        // w Vel Yaw
        KpwY = (SeekBar) v.findViewById(R.id.KpWYseek);
        KdwY = (SeekBar) v.findViewById(R.id.KdWYseek);
        KiwY = (SeekBar) v.findViewById(R.id.KiWYseek);

        // TextViews
        // Angular positions
        KpARVal = (TextView) v.findViewById(R.id.KpARval);
        KdARVal = (TextView) v.findViewById(R.id.KdARval);
        KiARVal = (TextView) v.findViewById(R.id.KiARval);
        KpAPVal = (TextView) v.findViewById(R.id.KpAPval);
        KpAPVal = (TextView) v.findViewById(R.id.KdAPval);
        KpAPVal = (TextView) v.findViewById(R.id.KiAPval);
        KpAYVal = (TextView) v.findViewById(R.id.KpAYval);
        KpAYVal = (TextView) v.findViewById(R.id.KdAYval);
        KpAYVal = (TextView) v.findViewById(R.id.KiAYval);
        // Angular Speed
        KpwRVal = (TextView) v.findViewById(R.id.KpWRVal);
        KdwRVal = (TextView) v.findViewById(R.id.KdWRVal);
        KiwRVal = (TextView) v.findViewById(R.id.KiWRVal);
        KpwPVal = (TextView) v.findViewById(R.id.KpWPval);
        KpwPVal = (TextView) v.findViewById(R.id.KdWPval);
        KpwPVal = (TextView) v.findViewById(R.id.KiWPval);
        KpwYVal = (TextView) v.findViewById(R.id.KpWYval);
        KpwYVal = (TextView) v.findViewById(R.id.KdWYval);
        KpwYVal = (TextView) v.findViewById(R.id.KiWYval);





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
                    if (blue.isAssociated()) {
                        Log.e("Control Report", "enabling Roll PID");
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
