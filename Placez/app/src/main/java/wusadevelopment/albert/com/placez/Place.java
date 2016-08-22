package wusadevelopment.albert.com.placez;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Albert on 21.08.2016.
 */
public class Place {
    private String name;
    private String description;
    private LatLng coords;
    private String address;
    private String picture;
    private int category;
    private int id;

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

    public void setCoords(LatLng coords) {
        this.coords = coords;
    }

    public LatLng getCoords() {
        return coords;
    }
}
