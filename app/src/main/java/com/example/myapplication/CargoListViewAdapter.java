package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CargoListViewAdapter extends ArrayAdapter<Cargo> {

    private Context context;
    private ArrayList<Cargo> cargoArrayList;

    public CargoListViewAdapter(@NonNull Context context, ArrayList<Cargo> cargoArrayList) {
        super(context, 0, cargoArrayList);
        this.context = context;
        this.cargoArrayList = cargoArrayList;


    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.custom_listview_cargo,parent,false);
        listItem.setOnClickListener(null);

        Cargo currentCargo = cargoArrayList.get(position);


        TextView name = (TextView) listItem.findViewById(R.id.name_cargo);
        name.setText(currentCargo.getName() + "");

        TextView quantity = (TextView) listItem.findViewById(R.id.quantity_cargo);
        quantity.setText(currentCargo.getQuiantity() +" " + currentCargo.getUnit());

        return listItem;
    }


}
