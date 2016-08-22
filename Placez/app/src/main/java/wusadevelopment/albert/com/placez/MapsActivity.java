package wusadevelopment.albert.com.placez;

import android.*;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static GoogleMap mMap;
    public static double longitude = 0.0;
    public static double latitude = 0.0;
    public static Marker currentPositionMarker;
    boolean locationpermissionGranted = false;
    boolean storagepermissionGranted = false;
    private LocationService ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhaveLocationPermission()) {
                requestForLocationPermission();
            }else{
                locationpermissionGranted = true;
            }
            if (!checkIfAlreadyhaveStoragePermission()) {
                requestForStoragePermission();
            }else{
                storagepermissionGranted = true;
            }
        }
        if(locationpermissionGranted && storagepermissionGranted) {
            ls = new LocationService(this);
            this.longitude = ls.getLongitude();
            this.latitude = ls.getLatitude();

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    private boolean checkIfAlreadyhaveLocationPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
    }

    private boolean checkIfAlreadyhaveStoragePermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    LocationService ls = new LocationService(this);
                    this.longitude = ls.getLongitude();
                    this.latitude = ls.getLatitude();
                    locationpermissionGranted = true;
                } else {
                    //not granted
                    Toast.makeText(this, R.string.locationWarning, Toast.LENGTH_LONG).show();
                }
                break;
            case 102:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    storagepermissionGranted = true;
                } else {
                    //not granted
                    Toast.makeText(this, R.string.storageWarning, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng currentPosition = new LatLng(this.latitude, this.longitude);
        currentPositionMarker = mMap.addMarker(new MarkerOptions().position(currentPosition).title(getString(R.string.currentPositionText)));
        moveMapToPosition(currentPosition);
    }

    public void moveMapToPosition(LatLng position) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
    }
}
