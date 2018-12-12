package com.example.kad0143.mygarageproject;

import android.graphics.Bitmap;

public class CarEntity {
    public long id;
    public String brand;
    public String model;
    public String year;
    public String engine;
    public Bitmap image;

    public CarEntity(long id, String brand, String model, String year, String engine, Bitmap image) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.engine = engine;
        this.image = image;
    }
}
