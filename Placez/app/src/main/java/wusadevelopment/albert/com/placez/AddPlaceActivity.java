package wusadevelopment.albert.com.placez;

import android.content.SharedPreferences;
import android.widget.ImageView;

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

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import com.google.android.gms.maps.model.LatLng;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AddPlaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int category;
    private String gLobalGoogleUrl = "http://maps.googleapis.com/maps/api/geocode/json?address=";
    private String key = "&key=AIzaSyAj0nKuNZu_FC9Y6uGW0uT7orQ1fBIzR6U";
    private String formatted_address;
    private String plz;
    private String ort;
    private String result;
    private LatLng coords;
    private Geocoder geocoder;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private int id = 0;

    private ImageButton AddPlaceAddPictureGalleryBtn;
    private ImageButton AddPlaceAddPictureTakePictureBtn;
    private ImageButton AddPlaceMyLocationBtn;

    private ImageView AddPlaceImagePreview;

    private EditText editAdress;

    private Uri picUri;

    String encodedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        this.AddPlaceAddPictureGalleryBtn = (ImageButton) findViewById(R.id.AddPlaceAddPictureGalleryBtn);
        this.AddPlaceAddPictureTakePictureBtn = (ImageButton) findViewById(R.id.AddPlaceAddPictureTakePictureBtn);
        this.AddPlaceMyLocationBtn = (ImageButton) findViewById(R.id.AddPlaceMyLocationBtn);

        this.AddPlaceAddPictureGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        this.AddPlaceAddPictureTakePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        this.AddPlaceMyLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentPosition();
            }
        });

        this.AddPlaceImagePreview = (ImageView) findViewById(R.id.AddPlaceImagePreview);
        this.editAdress = (EditText) findViewById(R.id.AddPlaceEditAddress);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();

        this.id = pref.getInt("id", 0);

        Spinner spinner = (Spinner) findViewById(R.id.AddPlaceSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
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
                    EditText addDescription = (EditText) findViewById(R.id.AddPlaceEditDescription);
                    String name = addName.getText().toString();
                    String address = editAdress.getText().toString();
                    String description = addDescription.getText().toString();


                    List<String> items = Arrays.asList(address.split("\\s*,\\s*"));
                    if(items.size() == 2 && items.get(0).matches("^[\\+\\-]{0,1}[0-9]+[\\.\\,]{1}[0-9]+$") && items.get(1).matches("^[\\+\\-]{0,1}[0-9]+[\\.\\,]{1}[0-9]+$")){
                        try{
                            double latitude = Double.parseDouble(items.get(0));
                            double longitude = Double.parseDouble(items.get(1));
                            coords = new LatLng(latitude, longitude);
                        } catch (NumberFormatException e) {
                            System.out.println("ILLEGAL NUMBER FORMAT");
                        }

                        try {
                            if (coords != null) {
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

                         if(Controller.getInstance(getApplicationContext()).addPlace(name,description,formatted_address,coords.latitude,coords.longitude,encodedImage,category,pref.getInt("id", 0))){
                             System.out.println("ORT ERSTELLT!!! mit Koordinaten");
                             editor.putInt("id", ++id);
                             editor.commit();
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

                    }else if(items.size() >= 2) {
                        try {
                            List<Address> adressList = geocoder.getFromLocationName(address,2);
                            coords= new LatLng(adressList.get(0).getLatitude(),adressList.get(0).getLongitude());
                            if(Controller.getInstance(getApplicationContext()).addPlace(name,description,address,coords.latitude,coords.longitude,encodedImage,category,pref.getInt("id", 0))){
                                System.out.println("ORT ERSTELLT!!! mit Adresse");
                                editor.putInt("id", ++id);
                                editor.commit();

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


                    Intent i = new Intent(getApplicationContext(), Home.class);
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
        int category = 1;
    }

    protected void getCurrentPosition(){
        LocationService ls = LocationService.getLocationManager(this);
        coords = new LatLng(ls.getLatitude(), ls.getLongitude());
        this.editAdress.setText(coords.latitude + "," + coords.longitude);
    }

    protected void pickImage() {
        try {
            Intent cropIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            cropIntent.setType("image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 800);
            cropIntent.putExtra("outputY", 800);

            File f = new File(Environment.getExternalStorageDirectory(), "/tmp_img.jpg");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            }

            Uri tmpImageUri = Uri.fromFile(f);

            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, tmpImageUri);

            startActivityForResult(cropIntent, 1);

        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected void takePicture() {
        try {
            //use standard intent to capture an image
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //we will handle the returned data in onActivityResult
            startActivityForResult(captureIntent, 2);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            String filePath = Environment.getExternalStorageDirectory()
                    + "/tmp_img.jpg";

            Bitmap thumbnail = BitmapFactory.decodeFile(filePath);
            File file = new File(filePath);
            file.delete();
            AddPlaceImagePreview.setImageBitmap(thumbnail);
            AddPlaceImagePreview.setVisibility(View.VISIBLE);

            encodedImage = encodeToBase64(thumbnail, Bitmap.CompressFormat.JPEG, 100);

        } else if (requestCode == 2) {
            picUri = data.getData();
            performCrop();
        }
    }

    protected void performCrop() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 800);
            cropIntent.putExtra("outputY", 800);
            cropIntent.putExtra("return-data", true);
            File f = new File(Environment.getExternalStorageDirectory(), "/tmp_img.jpg");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            }

            Uri tmpImageUri = Uri.fromFile(f);

            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, tmpImageUri);
            startActivityForResult(cropIntent, 1);
        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
