package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.Locale;

public class WarehouseCargoListAdapter extends RecyclerView.Adapter<WarehouseCargoListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Cargo> cargoArrayList;
    private ArrayList<Cargo> filterCargoArrayList;

    public WarehouseCargoListAdapter (Context context, ArrayList<Cargo> cargoArrayList)
    {
        this.context = context;
        this.cargoArrayList = cargoArrayList;
        this.filterCargoArrayList = cargoArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.warehouse_cargo_listview, parent, false);
        return new WarehouseCargoListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cargo currentCargo = filterCargoArrayList.get(position);
        holder.name.setText(currentCargo.getName()+"");
        holder.quantity.setText(String.format("%.2f",currentCargo.getQuiantity()) + " " + currentCargo.getUnit());
        holder.reservations.setText(currentCargo.getReservationQuantity() + " " + currentCargo.getUnit());
        holder.orders.setText(currentCargo.getOrderQuaintity() + " " + currentCargo.getUnit());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ResourcesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Kod towaru", holder.name.getText().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterCargoArrayList.size();
    }

    public void filterByName(String text) {

        text = text.toLowerCase(Locale.getDefault());
        if (text.length() == 0) {
            filterCargoArrayList = cargoArrayList;
        } else {
            ArrayList<Cargo> filteredList = new ArrayList<>();
            for (Cargo cargo : cargoArrayList) {
                if (cargo.getName().toLowerCase(Locale.getDefault()).contains(text))
                    filteredList.add(cargo);
            }
            filterCargoArrayList = filteredList;
        }
        notifyDataSetChanged();
    }


    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView name,quantity,reservations, orders;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_cargo);
            quantity = itemView.findViewById(R.id.quantity_cargo);
            reservations = itemView.findViewById(R.id.reservation_quantity);
            orders = itemView.findViewById(R.id.order_quantity);
            linearLayout = itemView.findViewById(R.id.cardCargo);
        }
    }


}
