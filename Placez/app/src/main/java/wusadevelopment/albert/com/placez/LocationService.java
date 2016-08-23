package wusadevelopment.albert.com.placez;

/**
 * Created by markk on 22.08.2016.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.MainThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by markk on 15.07.2016.
 */
public class LocationService implements LocationListener {

    //The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 3; // 10 meters

    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute

    private final static boolean forceNetwork = false;

    private static LocationService instance = null;

    private LocationManager locationManager;
    private Location location;
    private double longitude;
    private double latitude;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private boolean locationServiceAvailable;
    private static Context context;


    /**
     * Singleton implementation
     *
     * @return
     */
    public static LocationService getLocationManager(Context _context) {
        if (instance == null) {
            instance = new LocationService(_context);
            context = _context;
        }
        return instance;
    }

    /**
     * Local constructor
     */
    private LocationService(Context context) {

        initLocationService(context);
    }


    /**
     * Sets up location service after permissions is granted
     */
    //@TargetApi(23)
    public void initLocationService(final Context context) {


        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            this.longitude = 0.0;
            this.latitude = 0.0;
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (forceNetwork) isGPSEnabled = false;

            if (!isNetworkEnabled && !isGPSEnabled) {
                // cannot get location
                this.locationServiceAvailable = false;
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage(context.getResources().getString(R.string.setLocationText));
                dialog.show();
            }
            //else
            {
                this.locationServiceAvailable = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                    }
                }//end if

                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                    }
                }
            }
        } catch (Exception ex) {

        }
    }

    private void refreshData() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }
            }//end if

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }
            }
        } catch (Exception ex) {

        }
    }

    public double getLongitude() {
        refreshData();
        return this.longitude;
    }

    public double getLatitude() {
        refreshData();
        return this.latitude;
    }


    @Override
    public void onLocationChanged(Location location) {
        SharedPreferences sp = context.getSharedPreferences("MapsInfo", context.MODE_PRIVATE);
        if (sp.getBoolean("active", false) && !MapsActivity.mMap.equals(null)) {
            MapsActivity.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
            MapsActivity.latitude = location.getLatitude();
            MapsActivity.longitude = location.getLongitude();
            MapsActivity.currentPositionMarker.remove();
            MapsActivity.currentPositionMarker = MapsActivity.mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(String.valueOf(R.string.currentPositionText)));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}