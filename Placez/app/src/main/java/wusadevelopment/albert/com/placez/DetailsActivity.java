package wusadevelopment.albert.com.placez;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
        category.setText(getResources().getStringArray(R.array.categories)[place.getCategory()]);
        description.setText(place.getDescription());
        ImageButton navigateToBtn = (ImageButton) v.findViewById(R.id.PlaceDetailsNavigateToBtn);
        navigateToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" +
                        Math.round(place.getLat() * 100000.0) / 100000.0 +
                        "," + Math.round(place.getLng() * 100000.0) / 100000.0)
                );
                startActivity(navIntent);
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
