package wusadevelopment.albert.com.placez;

/**
 * Created by Albert on 21.08.2016.
 */
public class Place {
    private String name;
    private String description;
    private String address;
    private double lat;
    private double lng;
    private String picture;
    private int category;
    private int id;

    public Place(String name,String description,String address, double lat,double lng, String picture, int category, int id){
        this.name = name;
        this.description = description;
        this.address = address;
        this.lat = lat;
        this.lng =lng;
        this.picture = picture;
        this.category = category;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public int getCategory() {
        return category;
    }

    public int getId() {
        return id;
    }

    public String getPicture() {
        return picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
