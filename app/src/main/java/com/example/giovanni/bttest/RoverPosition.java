package com.example.giovanni.bttest;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.SupportMapFragment;

import com.example.giovanni.bttest.Utils.AlertDialogManager;
import com.example.giovanni.bttest.Utils.JSONParser;
import com.example.giovanni.bttest.Utils.NetworkUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by userk on 12/04/15.
 */

public class RoverPosition extends Activity
{
    private static final String TAG_LAT = "latg";
    private static final String TAG_LONG = "long";
    private static final String TAG_STATUS = "status";
    private GoogleMap map;
    private LatLng locationevent;
    private LatLng locationUser;
    private String nome,pokeroom,uid,coord;
    LocationManager locationManager;
    private String provider;
    MapFragment fragment;
    NetworkUtil net;
    // GPS Location
    GPSTracker gps;
    JSONParser parser;
    private static String url = "http://192.168.0.105:6543/rover/position";

    AlertDialogManager alert;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapevent);

        alert = new AlertDialogManager();
        if (net.getConnectivityStatus(getApplicationContext())==0)
        {
            // Internet Connection is not present
            alert.showAlertDialog(this,"Internet Connection Error",
                    "Please connect to working Internet connection");
            // stop executing code by return
            return;
        }

        gps = new GPSTracker(this);

        // check if GPS location can get
        if (gps.canGetLocation())
        {
            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
        } else
        {
            // Can't get user's current location
            alert.showAlertDialog(this, "GPS Status",
                    "Couldn't get location information. Please enable GPS");
            // stop executing code by return
            return;
        }

        String user_latitude = Double.toString(gps.getLatitude());
        String user_longitude = Double.toString(gps.getLongitude());
        double latu = Double.parseDouble(user_latitude);
        double lngu = Double.parseDouble(user_longitude);
        locationUser = new LatLng(latu,lngu);

        /*
        String input = coord.substring(0, coord.length());
        String[] latlngStr = input.split(",",2);
        String lat = latlngStr[0];
        String lng = latlngStr[1];
        double lati = Double.parseDouble(lat);
        double lngi = Double.parseDouble(lng);
        locationevent = new LatLng(lati,lngi);
*/
        /*LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

     	// Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        // Check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
        if (!enabled)
        {
          Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          startActivity(intent);
        }
        */
        //fragment = new SupportMapFragment();
        // this.getSupportFragmentManager().beginTransaction().add(R.id.map,fragment).commit();
        map =  ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        //map = fragment.getMap();
        map.setMyLocationEnabled(true);

        Marker user = map.addMarker(new MarkerOptions()
                .position(locationUser)
                .title("My Position")
                        //.snippet(nome)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.mark_red)));

        // Move the camera instantly to user position with a zoom of 16.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(locationUser, 17));

        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 3000, null);


        CameraPosition UserPosition = new CameraPosition.Builder()
        .target(locationUser)      // Sets the center of the map to marker point
        .zoom(16)                   // Sets the zoom
        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
        .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(UserPosition));

/*

        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(locationevent, 17), 4000, null);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(locationevent)      // Sets the center of the map to marker point
                .zoom(17)                   // Sets the zoom
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),2000,null);
	    /*
		// Construct a CameraPosition focusing on Mountain View and animate the camera to that position.


        Marker kiel = map.addMarker(new MarkerOptions()
        .position(location)
        .title(pokeroom)
        .snippet(nome)
        .icon(BitmapDescriptorFactory
            .fromResource(R.drawable.ace)));

	    // Zoom in, animating the camera.
	    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);



        Marker kiel = map.addMarker(new MarkerOptions()
        .position(location)
        .title(pokeroom)
        .snippet(nome)
        .icon(BitmapDescriptorFactory
            .fromResource(R.drawable.ace)));


            // Move the camera instantly to hamburg with a zoom of 15.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

            // Zoom in, animating the camera.
            map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

            */
    }


    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

class GetCoordinator extends Thread
{
    public GetCoordinator() {
    }

    public void run()
    {
        List<NameValuePair> params = null;
        String lng, lat;
        double lati, lngi;
        while (true)
        {
            try
            {
                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "pos"));
                JSONObject json = parser.getJSONFromUrl(url, params);
                    if (Integer.parseInt(json.getString(TAG_STATUS)) != 0)
                    {
                        lat =  json.getString(TAG_LAT);
                        lng = json.getString(TAG_LONG);
                        lati = Double.parseDouble(lat);
                        lngi = Double.parseDouble(lng);
                        locationevent = new LatLng(lati,lngi);

                        Marker a = map.addMarker(new MarkerOptions()
                                .position(locationevent)
                                .title("Rover")
                                .snippet("THOR")
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.mark_red)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}