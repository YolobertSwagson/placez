package wusadevelopment.albert.com.placez;

import java.util.HashMap;
import wusadevelopment.albert.com.placez.Place;

/**
 * Created by Albert on 21.08.2016.
 */
public class Controller {

    private static Controller instance = null;
    private HashMap placeList;


    protected Controller(){
        // Exists only to defeat instantiation.
    }

    public static Controller getInstance(){
        if (instance == null){
            instance = new Controller();

        }
            return instance;

    }

    public void writeToJSON(){}
    public void readFromJSON(){}
    public boolean addPlaceFromGeoLoc(String name, String description, GeoLocation coords , String picture, int category, int id){
        return true;
    }

    public boolean addPlaceFromAddress(String name, String description, String address , String picture, int category, int id){
        return true;
    }

    public HashMap getPlaceList(){
        return this.placeList;
    }

    public void clearList(){}

    public boolean removePlace(int id){
        return true;
    }
}
