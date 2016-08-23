package wusadevelopment.albert.com.placez;

import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class DetailsActivity extends Fragment {

    private Controller instance;
    private int position;
    private Place place;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.activity_details, container, false);
        instance = Controller.getInstance(getActivity());
        Bundle arguments = this.getArguments();
        if (arguments != null) {
            position = arguments.getInt("clicked_position");
        } else {
            position = 0;
        }
        place = instance.getPlaceList().get(position);
        ImageView image= (ImageView) v.findViewById(R.id.PlaceDetailsImage);
        TextView name = (TextView) v.findViewById(R.id.PlaceDetailsName);
        TextView address = (TextView) v.findViewById(R.id.PlaceDetailsAddress);
        TextView category = (TextView) v.findViewById(R.id.PlaceDetailsCategory);
        TextView description = (TextView) v.findViewById(R.id.PlaceDetailsDescription);

        image.setImageBitmap(instance.decodeBase64(place.getPicture()));
        name.setText(place.getName());
        address.setText(place.getAddress());
        //category.setText(place.getCategory());
        description.setText(place.getDescription());


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
