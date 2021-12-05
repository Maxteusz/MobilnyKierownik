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

public class WarehouseListAdapter extends ArrayAdapter<Warehouse> {

    private Context context;
    private ArrayList<Warehouse> warehouseArrayList;

    public WarehouseListAdapter (@NonNull Context context, ArrayList<Warehouse> warehouseArrayList){
super(context, 0, warehouseArrayList);

        this.context = context;
        this.warehouseArrayList = warehouseArrayList;
    }

    public WarehouseListAdapter(@NonNull android.content.Context context, int resource) {
        super(context, resource);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.dropdown_menu_popup_item,parent,false);


        Warehouse currentWarehouse = warehouseArrayList.get(position);


        TextView name = (TextView) listItem.findViewById(R.id.input_text);
        name.setText(currentWarehouse.getSymbol());


        return listItem;
    }


}