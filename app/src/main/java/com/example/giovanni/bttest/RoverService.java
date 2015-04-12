package com.example.giovanni.bttest;

import android.util.Log;

import com.example.giovanni.bttest.Utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by userk on 12/04/15.
 */
public class RoverService {
    private String start = "start";
    private JSONParser jsonParser;

    private static String rover = "http://192.168.0.105:6543/rover/start";

    public RoverService()
    {
        jsonParser = new JSONParser();
    }

    public JSONObject start()
    {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("tag", start));
        JSONObject json = jsonParser.getJSONFromUrl(rover,params);
        // return json
        Log.e("JSON -> LogIn", json.toString());
        return json;
    }

}
