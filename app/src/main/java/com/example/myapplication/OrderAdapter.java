package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    ArrayList<Order> OrdersArray;
    ArrayList<Order> filterOrderArray;
    Context context;

    public OrderAdapter(Context context, ArrayList<Order> OrdersArray) {

        this.context = context;
        this.OrdersArray = OrdersArray;
        this.filterOrderArray = OrdersArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Order currentOrder = filterOrderArray.get(position);
        holder.product.setText(currentOrder.getProductName() + "");
        holder.orderNumber.setText(currentOrder.getNumber());
        holder.quantity.setText(currentOrder.getProductQuantity() + "");
        holder.materialCost.setText(String.format("%.2f", currentOrder.getMaterialCost()) + " zł");
        holder.resourceCost.setText(String.format("%.2f", currentOrder.getResourcesCost()) + " zł");
        holder.additionCost.setText(String.format("%.2f", currentOrder.getAdditionCost()) + " zł");


    }

    public void filterByNumber(String text) {


            text = text.toLowerCase(Locale.getDefault());
            if (text.length() == 0) {
                filterOrderArray = OrdersArray;
            } else {
                ArrayList<Order> filteredList = new ArrayList<>();
                for (Order order : OrdersArray) {
                    if (order.getNumber().toLowerCase(Locale.getDefault()).contains(text))
                        filteredList.add(order);

                }
                filterOrderArray = filteredList;
            }
            notifyDataSetChanged();

    }



    public void filterByProduct(String text) {

        text = text.toLowerCase(Locale.getDefault());
        if (text.length() == 0) {
            filterOrderArray = OrdersArray;
        } else {
            ArrayList<Order> filteredList = new ArrayList<>();
            for (Order order : OrdersArray) {
                if (order.getProductName().toLowerCase(Locale.getDefault()).contains(text))
                    filteredList.add(order);
            }
            filterOrderArray = filteredList;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filterOrderArray.size();
    }

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView orderNumber, materialCost, resourceCost, product, quantity, additionCost;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        product = itemView.findViewById(R.id.product);
        orderNumber = itemView.findViewById(R.id.order_number);
        quantity = itemView.findViewById(R.id.quntity);
        resourceCost = itemView.findViewById(R.id.resourceCost);
        materialCost = itemView.findViewById(R.id.materialCost);
        additionCost = itemView.findViewById(R.id.additonCost);

    }
}


}
