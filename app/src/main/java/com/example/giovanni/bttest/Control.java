package com.example.giovanni.bttest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by userk on 09/04/15.
 */
public class Control extends Fragment {
    LinearLayout takeOff;
    LinearLayout land;
    InputStream inputStream;

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
