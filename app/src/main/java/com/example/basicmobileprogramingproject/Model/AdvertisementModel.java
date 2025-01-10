package com.example.basicmobileprogramingproject.Model;

public class AdvertisementModel {
    public int AdvertisementId;
    public String AdvertisementName;
    public String AdvertisementImage;
    public String Location;
    public int AdvertiserId;
    public Boolean Deleted;

    public AdvertisementModel(){
        this.AdvertisementId = 0;
        this.AdvertisementName = "";
        this.AdvertisementImage = "";
        this.Location = "";
        this.AdvertiserId = 0;
        this.Deleted = false;
    }
}
