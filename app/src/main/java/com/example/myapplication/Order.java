package com.example.myapplication;

public class Order {
    private String number, productName;
    private double materialCost;
    private double resourcesCost;
    private double additionCost;
    private double productQuantity;
    private double planProductQuantity;

    public Order(String number, String productName, double productQuantity, double materialCost, double resourcesCost) {
        this.number = number;
        this.productName = productName;
        this.materialCost = materialCost;
        this.resourcesCost = resourcesCost;
        this.productQuantity = productQuantity;
    }

    public Order(String number, String productName, double planProductQuantity, double productQuantity, double materialCost, double resourcesCost, double additionCost) {
        this.number = number;
        this.materialCost = materialCost;
        this.resourcesCost = resourcesCost;
        this.productQuantity = productQuantity;
        this.productName = productName;
        this.additionCost = additionCost;
        this.planProductQuantity = planProductQuantity;
    }



    public Order(String number, String product, float materialCost, float resourcesCost, int progress) {
        this.number = number;
        this.productName = product;
        this.materialCost = materialCost;
        this.resourcesCost = resourcesCost;

    }


    public String getNumber() {
        return number;
    }

    public String getProductName() {
        return productName;
    }

    public double getMaterialCost() {
        return materialCost;
    }

    public double getResourcesCost() {
        return resourcesCost;
    }

    public double getProductQuantity() {
        return productQuantity;
    }

    public double getAdditionCost() {
        return additionCost/planProductQuantity*productQuantity;
    }
}
