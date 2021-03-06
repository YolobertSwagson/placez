package wusadevelopment.albert.com.placez;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import java.io.IOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

/**
 * Created by Albert on 21.08.2016.
 */
public class Controller {

    private static Controller instance = null;
    private static List<Place> placeList;
    private static Context context;


    private Controller(){
        // Exists only to defeat instantiation.
    }
    private Controller(Context context){
        this.context=context;
        placeList = new ArrayList<>();
        System.out.println(placeList.isEmpty());
        System.out.println("CONTROLLER ERSTELLT!");
    }

    public static Controller getInstance(Context context){
        if (instance == null){
            instance = new Controller(context);

        }
            return instance;

    }

    public boolean writeToJSON(){
        String filename = "places";
        File file = new File(context.getFilesDir(),filename);
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = gson.toJson(placeList);
            outputStream.write(json.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void readFromJSON(){

        String filename = "places";
        Gson gson = new Gson();
        try {
            JsonReader reader= new JsonReader(new FileReader(context.getFilesDir()+"/"+filename));
            Type collectionType = new TypeToken<ArrayList<Place>>(){}.getType();
            placeList = gson.fromJson(reader,collectionType);
            if (placeList == null){
                placeList = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public boolean addPlace(String name, String description,String address, double lat, double lng , String picture, int category, int id){
        Place place = new Place(name,description,address,lat,lng,picture,category,id);
        placeList.add(place);


       return writeToJSON();
    }

    public boolean editPlace(String name, String description,String address, double lat, double lng , String picture, int category, int position){
        placeList.get(position).setName(name);
        placeList.get(position).setDescription(description);
        placeList.get(position).setAddress(address);
        placeList.get(position).setLat(lat);
        placeList.get(position).setLng(lng);
        placeList.get(position).setPicture(picture);
        placeList.get(position).setCategory(category);
        return writeToJSON();
    }


    public List<Place> getPlaceList(){
        return placeList;
    }



    public void clearList(){
        placeList.clear();
        writeToJSON();
    }

    public boolean removePlace(int id){
        for (Place tmp : placeList) {
            if (id == tmp.getId()) {
                placeList.remove(tmp);
                Toast.makeText(context, R.string.removedPlace, Toast.LENGTH_LONG).show();
                break;
            }
        }
        return writeToJSON();
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
