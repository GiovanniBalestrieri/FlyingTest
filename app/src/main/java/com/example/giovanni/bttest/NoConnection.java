package com.example.giovanni.bttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.giovanni.bttest.Utils.NetworkUtil;

/**
 ** Created by userk on 12/04/15.
 **/
public class NoConnection extends Activity
{
    LinearLayout fold;
    LinearLayout check;
    NetworkUtil net;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noconnection);


        fold = (LinearLayout) findViewById(R.id.foldLL);
        check = (LinearLayout) findViewById(R.id.checkLL);

        fold.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //Developer tool
                Toast toast=Toast.makeText(NoConnection.this," Fold clicked",Toast.LENGTH_LONG);
                toast.show();

                finish();

                System.exit(0);

            }
        });

        check.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (net.getConnectivityStatus(getApplicationContext())!=0)
                {

                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    Log.e("Connection -> Main", "Check!");
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(main);
                    finish();
                }
                else
                {
                    Toast toast=Toast.makeText(NoConnection.this," No Internet Connection",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}