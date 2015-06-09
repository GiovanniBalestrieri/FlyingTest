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

        //Buttons
        ARRec = (Button) v.findViewById(R.id.ARReceive);
        ARSen = (Button) v.findViewById(R.id.ARSend);
        APRec = (Button) v.findViewById(R.id.APReceive);
        APSen = (Button) v.findViewById(R.id.APSend);
        AYRec = (Button) v.findViewById(R.id.AYReceive);
        AYSen = (Button) v.findViewById(R.id.AYSend);
        WRRec = (Button) v.findViewById(R.id.WRReceive);
        WRSen = (Button) v.findViewById(R.id.WRSend);
        WPRec = (Button) v.findViewById(R.id.WPReceive);
        WPSen = (Button) v.findViewById(R.id.WPSend);
        WYRec = (Button) v.findViewById(R.id.WYReceive);
        WYSen = (Button) v.findViewById(R.id.WYSend);



        ARRec.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (blue.isAssociated()) {
                    Log.e("CPanel Report", "Click ARRec");
                    String msg = "parr";
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        //Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                          //      .show();
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

        ARSen.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (blue.isAssociated()) {
                    Log.e("CPanel Report", "Click ARRSend");
                    int kd = KdAR.getProgress();
                    int kp = KpAR.getProgress();
                    int ki = KiAR.getProgress();
                    String msg = "pars,";
                    msg += kp;
                    msg += ",";
                    msg += ki;
                    msg += ",";
                    msg += kd;
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        //Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                        //        .show();
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

        APRec.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (blue.isAssociated()) {
                    Log.e("CPanel Report", "Click AP Rec");
                    String msg = "papr";
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        //Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                        //        .show();
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

        APSen.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (blue.isAssociated()) {
                    Log.e("CPanel Report", "Click AP Send");
                    int kd = KdAP.getProgress();
                    int kp = KpAP.getProgress();
                    int ki = KiAP.getProgress();
                    String msg = "paps,";
                    msg += kp;
                    msg += ",";
                    msg += ki;
                    msg += ",";
                    msg += kd;
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        //Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                        //        .show();
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

        AYRec.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (blue.isAssociated()) {
                    Log.e("CPanel Report", "Click ARRec");
                    String msg = "payr";
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        //Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                        //        .show();
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

        AYSen.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (blue.isAssociated()) {
                    Log.e("CPanel Report", "Click AYSend");
                    int kd = KdAY.getProgress();
                    int kp = KpAY.getProgress();
                    int ki = KiAY.getProgress();
                    String msg = "pays,";
                    msg += kp;
                    msg += ",";
                    msg += ki;
                    msg += ",";
                    msg += kd;
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        //Toast.makeText(getActivity().getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                        //        .show();
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
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if (blue.isAssociated()) {
                    Log.e("Control Report", "enabling Roll PID");
                    String msg = "p";
                    msg += "ar";
                    if(isChecked)
                    {
                        msg += "e";
                        aRLL.setVisibility(LinearLayout.VISIBLE);
                    }else{
                        //aRoll.setText("Switch is currently OFF");
                        msg += "d";
                        aRLL.setVisibility(LinearLayout.GONE);
                    }
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
            if (blue.isAssociated()) {
                Log.e("Control Report", "enabling Pitch PID");
                String msg = "p";
                msg += "ap";
                if(isChecked)
                {
                    msg += "e";
                    aPLL.setVisibility(LinearLayout.VISIBLE);
                }else{
                    //aRoll.setText("Switch is currently OFF");
                    msg += "d";
                    aPLL.setVisibility(LinearLayout.GONE);
                }
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
            if (blue.isAssociated()) {
                Log.e("Control Report", "enabling Yaw PID");
                String msg = "p";
                msg += "ay";
                if(isChecked)
                {
                    msg += "e";
                    aYLL.setVisibility(LinearLayout.VISIBLE);
                }else{
                    //aRoll.setText("Switch is currently OFF");
                    msg += "d";
                    aYLL.setVisibility(LinearLayout.GONE);
                }
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
            if (blue.isAssociated()) {
                Log.e("Control Report", "enabling ùW Roll PID");
                String msg = "p";
                msg += "wr";
                if(isChecked)
                {
                    msg += "e";
                    wRLL.setVisibility(LinearLayout.VISIBLE);
                }else{
                    //aRoll.setText("Switch is currently OFF");
                    msg += "d";
                    wRLL.setVisibility(LinearLayout.GONE);
                }
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
            if (blue.isAssociated()) {
                Log.e("Control Report", "enabling W Pitch PID");
                String msg = "p";
                msg += "wp";
                if(isChecked)
                {
                    msg += "e";
                    wPLL.setVisibility(LinearLayout.VISIBLE);
                }else{
                    //aRoll.setText("Switch is currently OFF");
                    msg += "d";
                    wPLL.setVisibility(LinearLayout.GONE);
                }
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
            if (blue.isAssociated()) {
                Log.e("Control Report", "enabling W Yaw PID");
                String msg = "p";
                msg += "wy";
                if(isChecked)
                {
                    msg += "e";
                    wYLL.setVisibility(LinearLayout.VISIBLE);
                }else{
                    //aRoll.setText("Switch is currently OFF");
                    msg += "d";
                    wYLL.setVisibility(LinearLayout.GONE);
                }
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

        KpAR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                float val = (float) progress/100;
                KpARVal.setText(Float.toString(val));
            }
        });

        KdAR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub

                float val = (float) progress/100;
                KdARVal.setText(Float.toString(val));
            }
        });

        KiAR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub

                float val = (float) progress/100;
                KiARVal.setText(Float.toString(val));
            }
        });

        KdAP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub

                float val = (float) progress/100;
                KpAPVal.setText(Float.toString(val));
            }
        });

        inputStream = blue.getInput();

        final byte delimiter = 10; //This is the ASCII code for a newline character
        handler = new Handler();
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[512];

        if (blue.isAssociated())
        {
            workerThread = new Thread(new Runnable()
            {
                public void run()
                {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker)
                    {
                        try
                        {
                            int bytesAvailable = inputStream.available();
                            if (bytesAvailable > 0)
                            {
                                byte[] packetBytes = new byte[bytesAvailable];
                                inputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++)
                                {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                if (data.length() >= 1)
                                                {
                                                    char first = data.charAt(0);
                                                    if (first == 's') {
                                                        // Orientation status
                                                        String values[] = data.replace("s,", "").split(",");
                                                        String v1 = "";
                                                        String v2 = "";
                                                        String v3 = "";
                                                        String v4 = "";
                                                        String v5 = "";
                                                        String kpF = "";
                                                        String kiF = "";
                                                        String kdF = "";
                                                        if (values != null && values.length == 5) {
                                                            v1 = values[0];
                                                            v2 = values[1];
                                                            v3 = values[2];
                                                            v4 = values[3];
                                                        }
                                                        else if (values != null && values.length == 8) {
                                                            v1 = values[0];
                                                            v2 = values[1];
                                                            v3 = values[2];
                                                            v4 = values[3];
                                                            if (0 != Integer.parseInt(v4))
                                                            {
                                                                kpF = values[4];
                                                                kiF = values[5];
                                                                kdF = values[6];
                                                                // Angular Position
                                                                if(Integer.parseInt(v2)==1 && Integer.parseInt(v3)==0) {
                                                                    KpARVal.setText(kpF);
                                                                    KiARVal.setText(kiF);
                                                                    KdARVal.setText(kdF);
                                                                    int kp = (int) (Float.parseFloat(kpF) * 100);
                                                                    int ki = (int) (Float.parseFloat(kiF) * 100);
                                                                    int kd = (int) (Float.parseFloat(kdF) * 100);
                                                                    KpAR.setProgress(kp);
                                                                    KiAR.setProgress(ki);
                                                                    KdAR.setProgress(kd);
                                                                }
                                                                else if(Integer.parseInt(v2)==1 && Integer.parseInt(v3)==1) {
                                                                    KpAPVal.setText(kpF);
                                                                    KiAPVal.setText(kiF);
                                                                    KdAPVal.setText(kdF);
                                                                    int kp = (int) (Float.parseFloat(kpF) * 100);
                                                                    int ki = (int) (Float.parseFloat(kiF) * 100);
                                                                    int kd = (int) (Float.parseFloat(kdF) * 100);
                                                                    KpAP.setProgress(kp);
                                                                    KiAP.setProgress(ki);
                                                                    KdAP.setProgress(kd);
                                                                }
                                                                else if(Integer.parseInt(v2)==1 && Integer.parseInt(v3)==2) {
                                                                    KpAYVal.setText(kpF);
                                                                    KiAYVal.setText(kiF);
                                                                    KdAYVal.setText(kdF);
                                                                    int kp = (int) (Float.parseFloat(kpF) * 100);
                                                                    int ki = (int) (Float.parseFloat(kiF) * 100);
                                                                    int kd = (int) (Float.parseFloat(kdF) * 100);
                                                                    KpAY.setProgress(kp);
                                                                    KiAY.setProgress(ki);
                                                                    KdAY.setProgress(kd);
                                                                }
                                                                // Angular Velocity
                                                                if(Integer.parseInt(v2)==2 && Integer.parseInt(v3)==0) {
                                                                    KpwRVal.setText(kpF);
                                                                    KiwRVal.setText(kiF);
                                                                    KdwRVal.setText(kdF);
                                                                    int kp = (int) (Float.parseFloat(kpF) * 100);
                                                                    int ki = (int) (Float.parseFloat(kiF) * 100);
                                                                    int kd = (int) (Float.parseFloat(kdF) * 100);
                                                                    KpwR.setProgress(kp);
                                                                    KiwR.setProgress(ki);
                                                                    KdwR.setProgress(kd);
                                                                }
                                                                else if(Integer.parseInt(v2)==2 && Integer.parseInt(v3)==1) {
                                                                    KpwPVal.setText(kpF);
                                                                    KiwPVal.setText(kiF);
                                                                    KdwPVal.setText(kdF);
                                                                    int kp = (int) (Float.parseFloat(kpF) * 100);
                                                                    int ki = (int) (Float.parseFloat(kiF) * 100);
                                                                    int kd = (int) (Float.parseFloat(kdF) * 100);
                                                                    KpwP.setProgress(kp);
                                                                    KiwP.setProgress(ki);
                                                                    KdwP.setProgress(kd);
                                                                }
                                                                else if(Integer.parseInt(v2)==2 && Integer.parseInt(v3)==2) {
                                                                    KpwYVal.setText(kpF);
                                                                    KiwYVal.setText(kiF);
                                                                    KdwYVal.setText(kdF);
                                                                    int kp = (int) (Float.parseFloat(kpF) * 100);
                                                                    int ki = (int) (Float.parseFloat(kiF) * 100);
                                                                    int kd = (int) (Float.parseFloat(kdF) * 100);
                                                                    KpwY.setProgress(kp);
                                                                    KiwY.setProgress(ki);
                                                                    KdwY.setProgress(kd);
                                                                }
                                                                showToast("Dak");
                                                            }
                                                        }
                                                    } else {
                                                        // Received non correct message
                                                        showToast(data);
                                                    }
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
        return v;
    }

    private void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
