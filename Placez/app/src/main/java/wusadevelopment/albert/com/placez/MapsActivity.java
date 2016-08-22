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
    private LocationService ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ls = new LocationService(this);
        this.longitude = ls.getLongitude();
        this.latitude = ls.getLatitude();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
