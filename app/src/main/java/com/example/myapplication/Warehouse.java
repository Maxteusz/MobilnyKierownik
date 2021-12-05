package com.example.myapplication;

import java.util.ArrayList;

public class Warehouse {

    private int id;
    private String symbol;
    private ArrayList<Cargo> resources;

    public Warehouse(int id, String symbol)
    {
        this.id = id;
        this.symbol = symbol;
    }

    public Warehouse(String symbol, ArrayList<Cargo> resources)
    {
        this.resources = resources;
        this.symbol = symbol;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
