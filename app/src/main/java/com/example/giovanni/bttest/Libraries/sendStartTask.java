package com.example.giovanni.bttest.Libraries;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.giovanni.bttest.RoverService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by userk on 12/04/15.
 */
public class sendStartTask extends AsyncTask<Activity, Void, JSONObject>
{

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_START = "start";
    private static final String TAG_OK = "ok";
    private static final String TAG_PROJECT = "project";
    // reference to our imageview
    //private ImageView mImage;

    private View mContent;
    private View mSpinner;
    private int mDuration;
    private RoverService roverService;

    public sendStartTask(View content, View spinner, int duration)
    {
        mContent = content;
        mSpinner = spinner;
        mDuration = duration;
        roverService = new RoverService();
    }

    protected JSONObject doInBackground(Activity... act)
    {
        JSONObject jsonevent = roverService.start();
        try
        {
            String success = jsonevent.getString(TAG_OK);
            if (success != null)
            {
                if (Integer.parseInt(jsonevent.getString(TAG_OK)) != 0)
                {
                    // event found
                    String project = jsonevent.getString(TAG_PROJECT);
                    if (project.equals("mars"))
                        Log.d("sendStartTask Report", "Ok ");

                }
                else
                {
                    Log.d("Events Report: ", "NO event found");
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonevent;
    }
}
