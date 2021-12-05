package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {

    ArrayList<Document> documentArray;
    Context context;

    public DocumentAdapter(Context context, ArrayList<Document> documentArray) {
        this.documentArray = documentArray;
        this.context = context;


    }

    @NonNull
    @Override
    public DocumentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.document_layout, parent, false);
        return new DocumentAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull DocumentAdapter.ViewHolder holder, int position) {

        ArrayList<Cargo> cargoArrayList = getCargoArrayList(documentArray.get(position).getNumber());
        String temp = "";
        if (documentArray.get(position).getBuford() == 1)
            holder.document_number.setTextColor(Color.rgb(42, 133, 37));
        holder.document_number.setText(documentArray.get(position).getNumber());
        holder.date_document.setText(documentArray.get(position).getDate());
        holder.value.setText(String.format("%.2f",documentArray.get(position).getNettoVallue())+" zł");

        if (!cargoArrayList.isEmpty()) {
            for (int i = 0; i < cargoArrayList.size(); i++) {
                temp += cargoArrayList.get(i).getName() + " - " + cargoArrayList.get(i).getQuiantity() + " " + cargoArrayList.get(i).getUnit().toLowerCase() + "\n" ;
            }
            holder.cargo.setText(temp + "");
        }



    }

    @Override
    public int getItemCount() {
        return documentArray.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView document_number, date_document, cargo, value;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            document_number = itemView.findViewById(R.id.document_number);
            date_document = itemView.findViewById(R.id.date_document);
            cargo = itemView.findViewById(R.id.cargo);
            value = itemView.findViewById(R.id.value);


        }
    }

    public ArrayList<Cargo> getCargoArrayList(String numberDocument) {

        ArrayList<Cargo> arrayList = new ArrayList<>();
        SqlConnection connection = new SqlConnection();
        Connection con = connection.CONN();
        if (con != null) {
            Statement statement = null;
            try {
                statement = con.createStatement();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ResultSet rs = null;
            try {
                rs = statement.executeQuery("SELECT TrE_TwrNazwa,TrE_Ilosc, TrE_Jm FROM CDN.TraElem\n" +
                        "join CDN.TraNag on TrE_TrNId = TrN_TrNID\n" +
                        "where TrN_NumerPelny = '" + numberDocument + "'");
                while (rs.next()) {
                    String nameCargo = rs.getString(1);
                    int quaintityCargo = rs.getInt(2);
                    String unitCargo = rs.getString(3);
                    arrayList.add((new Cargo(nameCargo, quaintityCargo, unitCargo)));
                }
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }
}
