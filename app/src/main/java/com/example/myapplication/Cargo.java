package com.example.myapplication;

public class Cargo {
    private String name;
    private float quiantity;
    private String unit;
    private int minimumQuantity;
    private String delivery;

    public int getOrderQuaintity() {
        return orderQuaintity;
    }

    public void setOrderQuaintity(int orderQuaintity) {
        this.orderQuaintity = orderQuaintity;
    }

    public int getReservationQuantity() {
        return reservationQuantity;
    }

    public void setReservationQuantity(int reservationQuantity) {
        this.reservationQuantity = reservationQuantity;
    }

    private int orderQuaintity, reservationQuantity;

    public int getMinimumQuantity() {
        return minimumQuantity;
    }


    public float getQuiantity() {
        return quiantity;
    }

    public String getUnit() {
        return unit;
    }

    public Cargo(String name, int quiantity, String unit) {
        this.name = name;
        this.quiantity = quiantity;
        this.unit = unit;
    }

    public Cargo(String delivery, String unit, float quiantity) {

        this.quiantity = quiantity;
        this.unit = unit;
        this.delivery = delivery;
    }

    public Cargo(String name, float quiantity, String unit, int minimumQuantity, int orderQuaintity, int reservationQuantity) {
        this.name = name;
        this.quiantity = quiantity;
        this.unit = unit;
        this.minimumQuantity = minimumQuantity;
        this.orderQuaintity = orderQuaintity;
        this.reservationQuantity = reservationQuantity;
    }

    public String getName() {
        return name;
    }


}
