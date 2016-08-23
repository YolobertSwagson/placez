package wusadevelopment.albert.com.placez;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class DetailsActivity extends Fragment {

    private Controller instance;
    private int position;
    private Place place;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.activity_details, container, false);
        instance = Controller.getInstance(getActivity());
        Bundle arguments = getArguments();
        if (arguments != null) {
            position = arguments.getInt("clicked_position");
        } else {
            position = 0;
        }
        System.out.println(position);
        place = instance.getPlaceList().get(position);
        ImageView image= (ImageView) v.findViewById(R.id.PlaceDetailsImage);
        TextView name = (TextView) v.findViewById(R.id.PlaceDetailsName);
        TextView address = (TextView) v.findViewById(R.id.PlaceDetailsAddress);
        TextView category = (TextView) v.findViewById(R.id.PlaceDetailsCategory);
        TextView description = (TextView) v.findViewById(R.id.PlaceDetailsDescription);
        if(place.getPicture() != null){
            image.setImageBitmap(instance.decodeBase64(place.getPicture()));
        }

        name.setText(place.getName());
        address.setText(place.getAddress());
        //category.setText(place.getCategory());
        description.setText(place.getDescription());
        ImageButton navigateToBtn = (ImageButton) v.findViewById(R.id.PlaceDetailsNavigateToBtn);
        navigateToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

                // Get GPS and network status
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if(!isNetworkEnabled && !isGPSEnabled){
                    Toast.makeText(getContext(), "GPS vorher aktivieren!", Toast.LENGTH_LONG).show();
                }else{
                    Intent mapsIntent = new Intent(getContext(), MapsActivity.class);
                    mapsIntent.putExtra("id", place.getId());
                    startActivity(mapsIntent);
                }
            }
        });

        ImageButton editBtn = (ImageButton) v.findViewById(R.id.PlaceDetailsEditBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPlaceActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });

        ImageButton delBtn = (ImageButton) v.findViewById(R.id.PlaceDetailsDelBtn);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WarningAlert warningAlert = new WarningAlert();
                Bundle args = new Bundle();
                args.putInt("id",place.getId());
                warningAlert.setArguments(args);
                warningAlert.show(getFragmentManager(),"warningAlert");
              }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
