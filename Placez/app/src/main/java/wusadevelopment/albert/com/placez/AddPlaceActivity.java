package wusadevelopment.albert.com.placez;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.location.Address;
import android.location.Geocoder;
import org.json.JSONException;
import org.json.JSONObject;


import com.google.android.gms.maps.model.LatLng;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;

public class AddPlaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int category;
    private String gLobalGoogleUrl="http://maps.googleapis.com/maps/api/geocode/json?address=";
    private String key ="&key=AIzaSyAj0nKuNZu_FC9Y6uGW0uT7orQ1fBIzR6U";
    private String formatted_address;
    private String plz;
    private String ort;
    private String result;
    private LatLng coords;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);


        Spinner spinner = (Spinner) findViewById(R.id.AddPlaceSpinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        geocoder = new Geocoder(getApplicationContext());
        ImageButton confirmbtn = (ImageButton) findViewById(R.id.AddPlaceConfirmBtn);
        if (confirmbtn != null) {
            confirmbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText addName = (EditText) findViewById(R.id.AddPlaceEditName);
                    EditText addAddress = (EditText) findViewById(R.id.AddPlaceEditAddress);
                    EditText addDescription = (EditText) findViewById(R.id.AddPlaceEditDescription);
                    String name = addName.getText().toString();
                    String address = addAddress.getText().toString();
                    String description = addDescription.getText().toString();

                    String[] latlng = address.split(",");
                    if(latlng[0] != null && latlng[1] != null){
                        try{
                            double latitude = Double.parseDouble(latlng[0]);
                            double longitude = Double.parseDouble(latlng[1]);
                            coords = new LatLng(latitude, longitude);
                        }catch (NumberFormatException e){
                            System.out.println("ILLEGAL NUMBER FORMAT");
                        }

                        try {
                            if(coords != null) {
                                List<Address> adressList = geocoder.getFromLocation(coords.latitude, coords.longitude, 1);

                                if (adressList.get(0) != null) {
                                    String street = adressList.get(0).getAddressLine(0);
                                    String plz = adressList.get(0).getPostalCode();
                                    String locality = adressList.get(0).getLocality();
                                    formatted_address = street + " " + plz + " " + locality;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                         if(Controller.getInstance(getApplicationContext()).addPlace(name,description,formatted_address,coords.latitude,coords.longitude,"abc",1,1)){
                             System.out.println("ORT ERSTELLT!!!");
                         }
                        /*try {
                            result =getAddress(latitude,longitude);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                        /*if (result != null) {
                            try {
                                JSONObject object = new JSONObject(result);
                                String status = object.getString("status");
                                System.out.println(status);
                                formatted_address = object.getString("formatted_address");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }*/
                    }else {
                        try {
                            List<Address> adressList = geocoder.getFromLocationName(address,1);
                            coords= new LatLng(adressList.get(0).getLatitude(),adressList.get(0).getLongitude());
                            if(Controller.getInstance(getApplicationContext()).addPlace(name,description,address,coords.latitude,coords.longitude,"abc",1,1)){
                                System.out.println("ORT ERSTELLT!!!");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                       /* //String newAddress = address.replace(" ","+");
                        try {
                            result = getLatLng(newAddress);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                       /* if(result != null){
                            try {
                                JSONObject object = new JSONObject(result);
                                String status = object.getString("status");
                                System.out.println(status);
                                JSONObject location = object.getJSONObject("location");
                                double lng = location.getDouble("location.lng");
                                double lat = location.getDouble("location.lat");
                                coords = new LatLng(lng,lat);
                                formatted_address = object.getString("formatted_address");
                                Controller.getInstance().addPlace(name,description,address,coords,"abc",1,1);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }*/

                    }



                    Intent i = new Intent(getApplicationContext(),Home.class);
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int category = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }
/*    public String GET(String url) throws Exception {//GET Method
        String result = null;
        InputStream inputStream = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            Log.v("ExecuteGET: ", httpGet.getRequestLine().toString());

            HttpResponse httpResponse = httpclient.execute(httpGet);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertToString(inputStream);
                Log.v("Result: ", "result\n" + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public String convertToString(InputStream in){
        String resource = new Scanner(in).useDelimiter("\\Z").next();
        return resource;
    }

    public String getLatLng(String address) throws Exception{
        String query=gLobalGoogleUrl+"address=\""+ URLEncoder.encode(address)+key;
        Log.v("GETGoogleGeocoder", query+"");
        return GET(query);
    }

    public String getAddress(Double lat, double lng) throws Exception{
        String query=gLobalGoogleUrl+"latlng="+lat+","+lng+"location_type=ROOFTOP&result_type=street_address"+key;
        Log.v("GETGoogleGeocoder", query+"");
        return GET(query);
    }*/
}
