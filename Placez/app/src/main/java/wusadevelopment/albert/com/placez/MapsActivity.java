package wusadevelopment.albert.com.placez;

import android.*;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static GoogleMap mMap;
    public static double longitude = 0.0;
    public static double latitude = 0.0;
    public static Marker currentPositionMarker;
    private LocationService ls;

    private Place current;
    private List<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ls = LocationService.getLocationManager(this);
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
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        Controller controller = Controller.getInstance(this);
        controller.readFromJSON();
        places = controller.getPlaceList();
        for (Place tmp : controller.getPlaceList()) {
            current = tmp;
            addGoogleMapsMarker(tmp);
        }
    }

    public void addGoogleMapsMarker(Place place) {
        Marker newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(), place.getLng())));
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void moveMapToPosition(LatLng position) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sp = getSharedPreferences("MapsInfo", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", true);
        ed.commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sp = getSharedPreferences("MapsInfo", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        ed.commit();
    }

    public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {


        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            double lat = marker.getPosition().latitude;
            double lng = marker.getPosition().longitude;
            for (Place tmp : places){
                if(tmp.getLat() == lat && tmp.getLng() == lng){
                    View v = getLayoutInflater().inflate(R.layout.marker_layout, null);
                    if (tmp.getPicture() != null) {
                        Bitmap image = decodeBase64(tmp.getPicture());
                        ImageView markerImage = (ImageView) v.findViewById(R.id.markerImage);
                        markerImage.setImageBitmap(image);
                    }
                    TextView markerName = (TextView) v.findViewById(R.id.markerName);
                    markerName.setText(tmp.getName());

                    TextView markerDescription = (TextView) v.findViewById(R.id.markerDescription);
                    markerDescription.setText(tmp.getDescription());

                    TextView markerAddress = (TextView) v.findViewById(R.id.markerAddress);
                    markerAddress.setText(tmp.getAddress());

                    return v;
                }
            }
            return null;
        }
    }
}

