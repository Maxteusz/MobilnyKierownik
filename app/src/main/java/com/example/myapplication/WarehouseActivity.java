package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class WarehouseActivity extends AppCompatActivity {

    RecyclerView listView;
    WarehouseCargoListAdapter adapter;
    ArrayList<Cargo> cargoList;
    LinearLayoutManager linearLayoutManager;
    SqlConnection connection;
    AsyncTask<Void, Void, Void> taskInbacground;
    FloatingActionButton floatingActionButton;
    Dialog dialog;
    ArrayList<Warehouse> warehousesList;
    WarehouseListAdapter filterAdapter;
    AutoCompleteTextView filterSpinner;
    Button filterButton;
    TextInputLayout filterTextLayout;
    Warehouse currentWarehouse;
    RadioButton minimumState, allCargo;
    ProgressBar loadingSpinner;
    int radioButtonChoice;
    EditText filter_Editext;
    LinearLayout switchLayout;
    SwitchMaterial cargoWithZeroQuantity;
    Boolean showCargoWithZeroQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);
        getWindow().setStatusBarColor(Color.parseColor("#03a9f4"));
        floatingActionButton = findViewById(R.id.filter_button);
        listView = findViewById(R.id.cargoListView);
        loadingSpinner = findViewById(R.id.loading_spinner);
        filter_Editext = findViewById(R.id.filter_editText);
        filter_Editext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                adapter.filterByName(filter_Editext.getText() + "");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        filter_Editext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                filter_Editext.setText("");
            }
        });
        cargoList = new ArrayList<>();
        dialog = new Dialog(this);
        showCargoWithZeroQuantity = true;
        warehousesList = new ArrayList<>();
        warehousesList.add(new Warehouse(0, "Wszystkie magazyny"));
        currentWarehouse = warehousesList.get(0);
        radioButtonChoice = 0;

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(linearLayoutManager);
        adapter = new WarehouseCargoListAdapter(getApplicationContext(), cargoList);
        listView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && floatingActionButton.isShown()) {
                    floatingActionButton.hide();
                    filter_Editext.setHint("Szukaj");

                } else if (dy < 0 && !floatingActionButton.isShown()) {
                    floatingActionButton.show();
                    filter_Editext.setHint("Szukaj");

                }
            }


        });
        filterAdapter = new WarehouseListAdapter(getApplicationContext(), warehousesList);
        taskInbacground = new getData("0", 0).execute();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getWarehouses().execute();
                dialog.setContentView(R.layout.filter_dialog);
                minimumState = dialog.findViewById(R.id.radioButton);
                allCargo = dialog.findViewById(R.id.radioButton2);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);
                cargoWithZeroQuantity = dialog.findViewById(R.id.cargoSwitch);
                Log.i("Wybór", showCargoWithZeroQuantity + "");
                switchLayout = dialog.findViewById(R.id.switch_layout);
                filterSpinner = dialog.findViewById(R.id.filter_spinner);
                filterSpinner.setText(currentWarehouse.getSymbol());
                if (radioButtonChoice == 0)
                    switchLayout.setVisibility(View.GONE);
                else
                    switchLayout.setVisibility(View.VISIBLE);
                cargoWithZeroQuantity.setChecked(showCargoWithZeroQuantity);


                filterAdapter = new WarehouseListAdapter(getApplicationContext(), warehousesList);
                filterSpinner.setAdapter(filterAdapter);
                filterTextLayout = dialog.findViewById(R.id.filter_Layout);

                filterTextLayout.setEndIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterAdapter = new WarehouseListAdapter(getApplicationContext(), warehousesList);
                        filterSpinner.setAdapter(filterAdapter);
                        filterSpinner.showDropDown();
                    }
                });


                filterSpinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterAdapter = new WarehouseListAdapter(getApplicationContext(), warehousesList);
                        filterSpinner.setAdapter(filterAdapter);
                    }
                });
                filterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        filterSpinner.setText(warehousesList.get(position).getSymbol());
                        currentWarehouse = warehousesList.get(position);
                        Log.i("Ilość Magazynów2", currentWarehouse.getId() + "");
                    }
                });
                filterButton = dialog.findViewById(R.id.filter_button);
                filterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        cargoList.clear();
                        new getData(Integer.toString(currentWarehouse.getId()), radioButtonChoice).execute();

                        adapter.notifyDataSetChanged();

                    }
                });


                minimumState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radioButtonChoice = 0;
                        minimumState.setChecked(true);
                        switchLayout.setVisibility(View.GONE);


                    }
                });

                allCargo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radioButtonChoice = 1;
                        allCargo.setChecked(true);
                        switchLayout.setVisibility(View.VISIBLE);

                    }
                });
                switch (radioButtonChoice) {
                    case 0:
                        minimumState.setChecked(true);
                        break;
                    case 1:
                        allCargo.setChecked(true);
                        break;
                }
                cargoWithZeroQuantity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (buttonView.isChecked())
                            showCargoWithZeroQuantity = true;
                        else
                            showCargoWithZeroQuantity = false;

                        Log.i("Wybór", showCargoWithZeroQuantity + "");
                    }
                });

                dialog.show();
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onPause() {

        Log.i("Pause", "Pazuza");
        super.onPause();

    }

    public void getCargoList(String warehouse, int check) {


        connection = new SqlConnection();
        Connection con = connection.CONN();
        String tempWarehouse = "";

        if (warehouse.equals("0")) {
            tempWarehouse = "is null";

        } else tempWarehouse = "= " + warehouse;


        if (con != null) {
            Statement statement = null;
            ResultSet rs = null;


            if (check == 0) {
                try {
                    statement = con.createStatement();


                    rs = statement.executeQuery("WITH CTE AS\n" +
                            "(\n" +
                            "select TwI_TwIId, twI_twrId,TwI_Data, ROW_NUMBER() OVER(PARTITION BY twI_twrID ORDER by TwI_Data desc ) As 'Position' ,TwI_Ilosc, TwI_Rezerwacje, TwI_Zamowienia from cdn.TwrIlosci\n" +
                            "where TwI_Data <= GETDATE() AND TwI_MagId " + tempWarehouse + "\n" +
                            " )\n" +
                            "SELECT  Twr_Kod, Twr_jm, TwI_Ilosc as ilosc ,Twr_IloscMin, TwI_Rezerwacje, TwI_Zamowienia FROM CTE\n" +
                            "right join cdn.Towary on Twr_TwrId = TwI_TwrId\n" +
                            "where (Position = 1 or Position is null)  and (TwI_Ilosc >= 0 or TwI_Ilosc is null)  and (TwI_Ilosc < Twr_IloscMin or TwI_Ilosc is null) and twr_typ = 1 and Twr_IloscMin > 0 and twr_NieAktywny = 0\n" +
                            "\n" +
                            "group by Twr_Kod,Twr_IloscMin,Twr_jm, TwI_Rezerwacje, TwI_Zamowienia, TwI_Ilosc");

                    while (rs.next()) {

                        String name = rs.getString(1);
                        String unit = rs.getString(2);
                        float quantity = rs.getFloat(3);
                        int minimumQuantity = rs.getInt(4);
                        int orderQuantity = rs.getInt(6);
                        int reservationQuantity = rs.getInt(5);
                        cargoList.add(new Cargo(name, quantity, unit, minimumQuantity, orderQuantity, reservationQuantity));

                    }

                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (check == 1 && !showCargoWithZeroQuantity) {
                try {
                    statement = con.createStatement();

                    rs = statement.executeQuery("WITH CTE AS\n" +
                            "(\n" +
                            "select TwI_TwIId, twI_twrId,TwI_Data, ROW_NUMBER() OVER(PARTITION BY twI_twrID ORDER BY TwI_Data desc ) As 'Position' ,TwI_Ilosc, TwI_Rezerwacje, TwI_Zamowienia from cdn.TwrIlosci\n" +
                            "where TwI_Data <= GETDATE() AND TwI_MagId " + tempWarehouse + "\n" +
                            " )\n" +
                            "SELECT  Twr_Kod, Twr_jm, TwI_Ilosc as ilosc ,Twr_IloscMin, TwI_Rezerwacje, TwI_Zamowienia FROM CTE\n" +
                            "right join cdn.Towary on Twr_TwrId = TwI_TwrId\n" +
                            "where (Position = 1 or Position is null)  and (TwI_Ilosc > 0)  and twr_typ = 1 and twr_NieAktywny = 0\n" +
                            "\n" +
                            "group by Twr_Kod,Twr_IloscMin,Twr_jm, TwI_Rezerwacje, TwI_Zamowienia, TwI_Ilosc");

                    while (rs.next()) {

                        String name = rs.getString(1);
                        String unit = rs.getString(2);
                        float quantity = rs.getFloat(3);
                        int minimumQuantity = rs.getInt(4);
                        int orderQuantity = rs.getInt(6);
                        int reservationQuantity = rs.getInt(5);
                        cargoList.add(new Cargo(name, quantity, unit, minimumQuantity, orderQuantity, reservationQuantity));

                    }
                    Log.i("ilosc towarów", cargoList.size() + "");
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            if (check == 1 && showCargoWithZeroQuantity) {
                try {
                    statement = con.createStatement();

                    rs = statement.executeQuery("WITH CTE AS\n" +
                            "(\n" +
                            "select TwI_TwIId, twI_twrId,TwI_Data, ROW_NUMBER() OVER(PARTITION BY twI_twrID ORDER BY TwI_Data desc ) As 'Position' ,TwI_Ilosc, TwI_Rezerwacje, TwI_Zamowienia from cdn.TwrIlosci\n" +
                            "where TwI_Data <= GETDATE() AND TwI_MagId " + tempWarehouse + "\n" +
                            " )\n" +
                            "SELECT  Twr_Kod, Twr_jm, TwI_Ilosc as ilosc ,Twr_IloscMin, TwI_Rezerwacje, TwI_Zamowienia FROM CTE\n" +
                            "right join cdn.Towary on Twr_TwrId = TwI_TwrId\n" +
                            "where (Position = 1 or Position is null)  and (TwI_Ilosc >=  0 or TwI_Ilosc is null)  and twr_typ = 1 and twr_NieAktywny = 0\n" +
                            "\n" +
                            "group by Twr_Kod,Twr_IloscMin,Twr_jm, TwI_Rezerwacje, TwI_Zamowienia, TwI_Ilosc");

                    while (rs.next()) {

                        String name = rs.getString(1);
                        String unit = rs.getString(2);
                        float quantity = rs.getFloat(3);
                        int minimumQuantity = rs.getInt(4);
                        int orderQuantity = rs.getInt(6);
                        int reservationQuantity = rs.getInt(5);
                        cargoList.add(new Cargo(name, quantity, unit, minimumQuantity, orderQuantity, reservationQuantity));

                    }
                    Log.i("ilosc towarów", cargoList.size() + "");
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        }

    }


    public class getData extends AsyncTask<Void, Void, Void> {

        String warehouse;
        int check;

        public getData(String warehouse, int check) {
            this.warehouse = warehouse;
            this.check = check;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);

            loadingSpinner.setVisibility(View.GONE);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            getCargoList(warehouse, check);

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cargoList.clear();
            loadingSpinner.setVisibility(View.VISIBLE);
        }
    }

    public class getWarehouses extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            filterAdapter.notifyDataSetChanged();


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            connection = new SqlConnection();
            Connection con = connection.CONN();


            if (con != null) {
                Statement statement = null;
                ResultSet rs = null;

                try {
                    statement = con.createStatement();
                    rs = statement.executeQuery("Select Mag_MagId, Mag_Symbol from CDN.Magazyny");
                    while (rs.next()) {
                        warehousesList.add(new Warehouse(rs.getInt(1), rs.getString(2)));

                    }
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            warehousesList.clear();
            warehousesList.add(new Warehouse(0, "Wszystkie magazyny"));


        }
    }

    public void clearFiltr (View v)
    {
filter_Editext.setText("");
        filter_Editext.setHint("Szukaj");
        Log.i("Clear Filttr", "Czyszczenie filtrowania");
    }


}