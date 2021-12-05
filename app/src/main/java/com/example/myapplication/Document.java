package com.example.myapplication;

public class Document {

   private String number;
   private String sourceWarehouse;
   private String targetWarehouse;
   private String owner;
    private double nettoVallue;

    public Document(String number, String date, int buford,double nettoVallue) {
        this.number = number;
        this.nettoVallue = nettoVallue;
        this.date = date;
        this.buford = buford;
    }

    public double getNettoVallue() {
        return nettoVallue;
    }

    public String getDate() {
        return date;
    }

    public int getBuford() {
        return buford;
    }

    private String date;
   private int buford;


    public Document(String number, String date, int buford) {
        this.number = number;
        this.date = date;
        this.buford = buford;

    }

    public Document(String number, String date) {
        this.number = number;
        this.date = date;
    }

    public Document(String number, String sourceWarehouse, String targetWarehouse, String owner, String date) {
        this.number = number;
        this.sourceWarehouse = sourceWarehouse;
        this.targetWarehouse = targetWarehouse;
        this.owner = owner;
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSourceWarehouse() {
        return sourceWarehouse;
    }

    public void setSourceWarehouse(String sourceWarehouse) {
        this.sourceWarehouse = sourceWarehouse;
    }

    public String getTargetWarehouse() {
        return targetWarehouse;
    }

    public void setTargetWarehouse(String targetWarehouse) {
        this.targetWarehouse = targetWarehouse;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
