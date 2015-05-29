package com.example.giovanni.bttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect);
        tour = (Button) findViewById(R.id.tour);
        connect = (LinearLayout) findViewById(R.id.btnConnect);
        logs = (TextView) findViewById(R.id.connectionLogs);


        final Bluetooth blue = new Bluetooth(getApplicationContext(),this);

        connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (blue.isAssociated()) {
                    Log.e("CPanel Report", "Click Land");
                    String msg = "L";
                    msg += "\n";
                    if (blue.blueWrite(msg)) {
                        Toast.makeText(getApplicationContext(), " Sent: " + msg, Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Toast.makeText(getApplicationContext(), " Message error ", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Select device first", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

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
    }
}
