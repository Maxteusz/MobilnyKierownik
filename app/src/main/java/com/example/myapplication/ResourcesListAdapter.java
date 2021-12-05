package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ResourcesListAdapter extends RecyclerView.Adapter<ResourcesListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Warehouse> warehouseArrayList;
    ArrayList<Cargo> cargoArrayList;
    SqlConnection sqlConnection;
    Connection connection;
    private String cargoCode;
    ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> attributesList;


    public ResourcesListAdapter(Context context, ArrayList<Warehouse> warehouseArrayList, ArrayList<String> attributesList, String cargoCode) {
        this.context = context;
        this.warehouseArrayList = warehouseArrayList;
        this.attributesList = attributesList;
        this.cargoCode = cargoCode;
        this.cargoArrayList = new ArrayList<>();
        attributesList.add("Wszystkie atrybuty");
        sqlConnection = new SqlConnection();
        spinnerAdapter = new ArrayAdapter<>(context, R.layout.spinner_layout_custom, attributesList);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_menu_popup_item);

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.resources_layout, parent, false);
        return new ResourcesListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        double ilosc = 0;
        Warehouse currentWarehouse = warehouseArrayList.get(position);
        cargoArrayList = getCargoArrayListWithoutAttributes(currentWarehouse.getId());
        holder.warehouseName.setText(currentWarehouse.getSymbol() + "");
        holder.spinner.setAdapter(spinnerAdapter);
        for (int i = 0; i < cargoArrayList.size(); i++)
            ilosc += cargoArrayList.get(i).getQuiantity();
        holder.cargo.setText(ilosc+"");
        if (attributesList.size() == 1) {
            //cargoArrayList = getCargoArrayListWithoutAttributes(currentWarehouse.getId());
            holder.spinner.setVisibility(View.INVISIBLE);
        }
        //else
            //Do zmiany na towary z atrybutami
            //getCargoArrayListWithoutAttributes(currentWarehouse.getId());


    }

    @Override
    public int getItemCount() {
        return warehouseArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView warehouseName,cargo;
        Spinner spinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            spinner = itemView.findViewById(R.id.attributes_spinner);
            warehouseName = itemView.findViewById(R.id.warehouse_name);
            cargo = itemView.findViewById(R.id.cargo);
        }
    }

    public ArrayList<Cargo> getCargoArrayListWithoutAttributes (int magId)
    {
        ArrayList<Cargo> cargoArrayList = new ArrayList<>();
        try {
            sqlConnection = new SqlConnection();
            connection = sqlConnection.CONN();
            ResultSet rs = null;
            if (connection != null) {
                Statement statement = connection.createStatement();
                rs = statement.executeQuery("SELECT  \n" +
                        "TrN_NumerPelny\n" +
                        ",[TwZ_Ilosc] \n" +
                        ",Twr_JM\n" +
                        ",TsC_Cecha1_Wartosc\n" +
                        "FROM [CDN].[TwrZasoby] \n" +
                        "join cdn.traSelem on TwZ_TrSIdDost = Trs_TrSIdDost\n" +
                        "join cdn.TraElem on TrS_TrEId = TrE_TrEID\n" +
                        "join cdn.TraNag on TrE_TrNId = TrN_TrNID\n" +
                        "join cdn.Towary on TwZ_TwrId = Twr_TwrId\n" +
                        "left join cdn.TraSElemCechy on TrS_TrSID = TsC_TrSID\n" +
                        "where Trs_Typ = 1 and Twr_Kod = '" + cargoCode + "' and TrN_Korekta = 0 and TwZ_MagId = '" + magId + "'");
                while (rs.next()) {
                    cargoArrayList.add(new Cargo(rs.getString(1),rs.getString(3), rs.getFloat(2)));
                }

            }
            connection.close();
            //notifyDataSetChanged();

        } catch(SQLException e){
            e.printStackTrace();
        }

        return cargoArrayList;
    }
}
