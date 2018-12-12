package com.example.kad0143.mygarageproject.Entity;

public class OwnerData {
    public long id;
    public String carId;
    public String name;
    public String phone;
    public String street;
    public String city;
    public String postcode;

    // TODO JK UDELAT gettery radsi???
    public OwnerData(long id, String carId, String name, String phone, String street, String city, String postcode) {
        this.id = id;
        this.carId = carId;
        this.name = name;
        this.phone = phone;
        this.street = street;
        this.city = city;
        this.postcode = postcode;
    }
}
