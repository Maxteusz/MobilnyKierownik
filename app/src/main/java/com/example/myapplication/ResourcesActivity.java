package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ResourcesActivity extends AppCompatActivity {


    TextView nameCargo;
    RecyclerView resourcesList;
    ResourcesListAdapter adapter;
    ArrayList<Warehouse> warehouseArrayList;
    Connection connection;
    LinearLayoutManager linearLayoutManager;
    SqlConnection sqlConnection;
    ArrayList<String> attrubitesList;
    String cargo;

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        getWindow().setStatusBarColor(Color.parseColor("#03a9f4"));

        nameCargo = findViewById(R.id.order_number);
        attrubitesList = new ArrayList<>();
        warehouseArrayList = new ArrayList<>();
        resourcesList = findViewById(R.id.document_recyclerView);
        Intent intent = getIntent();
        cargo = intent.getStringExtra("Kod towaru");
        nameCargo.setText(cargo);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        resourcesList.setLayoutManager(linearLayoutManager);
        adapter = new ResourcesListAdapter(getApplicationContext(), warehouseArrayList, attrubitesList, cargo);
        resourcesList.setAdapter(adapter);
        sqlConnection = new SqlConnection();
        new getWarehouse().execute();


    }

    public class getWarehouse extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                connection = sqlConnection.CONN();
                ResultSet rs = null;
                if (connection != null) {
                    Statement statement = connection.createStatement();
                    rs = statement.executeQuery("SELECT distinct\n" +
                            "  Mag_MagId\n" +
                            "  ,Mag_Symbol\n" +
                            "  FROM cdn.TwrZasoby\n" +
                            "  join cdn.Towary on TwZ_TwrId = Twr_TwrId\n" +
                            "  join cdn.TraSElem on TwZ_TrSIdDost = TrS_TrSIdDost\n" +
                            "  join cdn.TraElem on TrS_TrEId = TrE_TrEID\n" +
                            "  join cdn.tranag on TrE_TrNId = TrN_TrNID\n" +
                            "  join cdn.Magazyny on TwZ_MagId = Mag_MagId\n" +
                            "  where TrS_Typ = 1 and Twr_Kod like '" + cargo + "'");
                    while (rs.next()) {
                        warehouseArrayList.add(new Warehouse(rs.getInt(1), rs.getString(2)));
                    }

                    getAtriibutes();
                }
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            warehouseArrayList.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();


        }
    }

    public void getAtriibutes() {
        try {
            connection = sqlConnection.CONN();
            ResultSet rs;
            if (connection != null) {
                Statement statement = connection.createStatement();
                rs = statement.executeQuery("SELECT distinct  DeA_Kod\n" +
                        "  FROM [CDN].[TwrAtrybuty]\n" +
                        "  join cdn.Towary on Twr_TwrId = TwA_TwrId\n" +
                        "   join cdn.DefAtrybuty on DeA_DeAId = TwA_DeAId\n" +
                        "  where twr_kod like '" + cargo + "'");
                while (rs.next()) {
                    attrubitesList.add(rs.getString(1));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}