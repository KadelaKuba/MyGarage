package com.example.kad0143.mygarageproject.Entity;

public class CarInformation {
    public long id;
    public String carId;
    public String tachometer;
    public String registrationDate;
    public String stkValidity;
    public String doctorValidity;

    public CarInformation(long id, String carId, String tachometer, String registrationDate, String stkValidity, String doctorValidity) {
        this.id = id;
        this.carId = carId;
        this.tachometer = tachometer;
        this.registrationDate = registrationDate;
        this.stkValidity = stkValidity;
        this.doctorValidity = doctorValidity;
    }
}
