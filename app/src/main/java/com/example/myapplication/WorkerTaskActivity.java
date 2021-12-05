package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.material.card.MaterialCardView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class WorkerTaskActivity extends AppCompatActivity {

    ArrayList<Task> taskArrayList;
    ArrayList<Order> orderArrayList;
    WorkerTaskAdapter workerTaskAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    SqlConnection connection;
    Connection con;
    ImageButton showOrders, showTask;
    OrderAdapter orderAdapter;
    ProgressBar loadingSpinner;
    MaterialCardView searchBar;
    EditText filterEditText;
    Spinner spinner;
    int setFilter;
    CardName setCardActive;
    ArrayAdapter<String> spinnerAdapter;
    ArrayList<String> list;


    @Override
    protected void onPause() {
        super.onPause();
        taskArrayList.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_task);
        getWindow().setStatusBarColor(Color.parseColor("#03a9f4"));
        recyclerView = findViewById(R.id.recycler_view);
        taskArrayList = new ArrayList<>();
        loadingSpinner = findViewById(R.id.loading_progress);
        showOrders = findViewById(R.id.show_orders);
        orderArrayList = new ArrayList<>();
        list = new ArrayList<>();
        setCardActive = CardName.WorkerTaskCard_ACTIVE;
        list.addAll(Arrays.asList(getResources().getStringArray(R.array.filter_task)));
        spinner = findViewById(R.id.spinner);
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout_custom, list);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setFilter = position;
                if (filterEditText.getText().length() != 0) {
                    try {
                        if (setCardActive == CardName.WorkerTaskCard_ACTIVE)
                            switch (setFilter) {
                                case 0:
                                    workerTaskAdapter.filterByNumber(filterEditText.getText() + "");
                                    break;
                                case 1:
                                    workerTaskAdapter.filterByWorkerName(filterEditText.getText() + "");
                                    break;
                                case 2:
                                    workerTaskAdapter.filterByResource(filterEditText.getText() + "");
                            }
                        if (setCardActive == CardName.OrderCard_ACTIVE) {
                            switch (setFilter) {
                                case 0:
                                    orderAdapter.filterByNumber(filterEditText.getText() + "");
                                    break;
                                case 1:
                                    orderAdapter.filterByProduct(filterEditText.getText() + "");
                                    break;
                            }
                        }
                    } catch (Exception e) {
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchBar = findViewById(R.id.search_bar);
        filterEditText = findViewById(R.id.filter_editText);
        filterEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                filterEditText.setText("");
            }
        });

        filterEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    if (setCardActive == CardName.WorkerTaskCard_ACTIVE)
                        switch (setFilter) {
                            case 0:
                                workerTaskAdapter.filterByNumber(filterEditText.getText() + "");
                                break;
                            case 1:
                                workerTaskAdapter.filterByWorkerName(filterEditText.getText() + "");
                                break;
                            case 2:
                                workerTaskAdapter.filterByResource(filterEditText.getText() + "");
                        }
                    if (setCardActive == CardName.OrderCard_ACTIVE) {
                        switch (setFilter) {
                            case 0:
                                orderAdapter.filterByNumber(filterEditText.getText() + "");
                                break;
                            case 1:
                                orderAdapter.filterByProduct(filterEditText.getText() + "");
                                break;
                        }
                    }
                }catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WorkerTaskActivity.this);
                    View view = WorkerTaskActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    builder.setTitle("Bład filtrowania");
                    builder.setMessage("Prawdopodobnie nie wszystkie zlecenia mają uzupełnione wyszukiwane pole");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }

                    });


                    AlertDialog alert = builder.create();
                    alert.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        }
                    });

                    alert.show();
                    filterEditText.setText("");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        showOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderAdapter = new OrderAdapter(getApplicationContext(), orderArrayList);
                recyclerView.setAdapter(orderAdapter);
                showOrders.setClickable(false);
                filterEditText.clearFocus();
                filterEditText.setHint("Szukaj");
                showTask.setClickable(true);
                new getOrders().execute();
                showOrders.setImageResource(R.drawable.order_active);
                showOrders.setAlpha((float) 1);
                showTask.setAlpha((float) 0.2);
                showTask.setImageResource(R.drawable.outline_assignment_ind_black_48dp);
                list.clear();
                list.addAll(Arrays.asList(getResources().getStringArray(R.array.filter_order)));
                spinnerAdapter.notifyDataSetChanged();
                setCardActive = CardName.OrderCard_ACTIVE;
                Log.i("Wybrano", setCardActive + "");
            }
        });

        showTask = findViewById(R.id.show_task);
        showTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workerTaskAdapter = new WorkerTaskAdapter(getApplicationContext(), taskArrayList);
                recyclerView.setAdapter(workerTaskAdapter);
                filterEditText.clearFocus();
                filterEditText.setHint("Szukaj");
                new getTasks().execute();
                showOrders.setClickable(true);
                showTask.setClickable(false);
                showTask.setImageResource(R.drawable.active_logo);
                showTask.setAlpha((float) 1);
                showOrders.setImageResource(R.drawable.baseline_assignment_black_48);
                showOrders.setAlpha((float) 0.2);
                list.clear();
                list.addAll(Arrays.asList(getResources().getStringArray(R.array.filter_task)));
                setCardActive = CardName.WorkerTaskCard_ACTIVE;
                spinnerAdapter.notifyDataSetChanged();
                Log.i("Wybrano", setCardActive + "");
            }
        });
        connection = new SqlConnection();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        workerTaskAdapter = new WorkerTaskAdapter(getApplicationContext(), taskArrayList);
        recyclerView.setAdapter(workerTaskAdapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        taskArrayList.clear();
        new getTasks().execute();
    }

    public class getTasks extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
            workerTaskAdapter.notifyDataSetChanged();
            loadingSpinner.setVisibility(View.GONE);
        }

        protected Void doInBackground(Void... voids) {
            taskArrayList.clear();
            con = connection.CONN();
            if (con != null) {
                Statement statement = null;
                try {
                    statement = con.createStatement();
                    ResultSet rs = null;

                    rs = statement.executeQuery("select\n" +
                            "CZN_NrPelny as [Numer Zlecenia]\n" +
                            ",ZZs_PrcID as [Id Pracownika]\n" +
                            ", (select top 1 CZ_Kod from dbo.CtiZasob where CZ_ID = ZZs_CZID) as [Nazwa Zasobu]\n" +
                            ", ZZs_DataOd as [Start]\n" +
                            ", (select top 1 PRE_Imie1 + ' ' + PRE_Nazwisko from CDN.PracEtaty where PRE_PraId = ZZs_PrcId) as [Pracownik Rzeczywisty]\n" +
                            ", (DATEDIFF(SECOND, ZZs_DataOd, CASE WHEN ZZs_DataDo IS NULL THEN getdate() END)/60.0000) as [Czas - Zadania Trwające w minutach ]\n" +
                            "from dbo.CtiZlecenieNag\n" +
                            "right join dbo.CtiZlecenieZasob on CZN_ID = ZZs_CZNID where ZZs_DataDo is null");
                    while (rs.next()) {
                        int workerId = rs.getInt("Id Pracownika");
                        String name = rs.getString("Pracownik Rzeczywisty");
                        String resource = rs.getString("Nazwa Zasobu");
                        String number = rs.getString("Numer Zlecenia");
                        String startDate = rs.getString("Start").toString();
                        taskArrayList.add(new Task(workerId, name, resource, number, startDate));
                    }

                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskArrayList.clear();
            loadingSpinner.setVisibility(View.VISIBLE);
        }
    }

    public class getOrders extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
            orderAdapter.notifyDataSetChanged();
            loadingSpinner.setVisibility(View.GONE);
        }

        protected Void doInBackground(Void... voids) {
            taskArrayList.clear();
            con = connection.CONN();
            if (con != null) {
                Statement statement = null;
                try {
                    statement = con.createStatement();
                    ResultSet rs = null;

                    rs = statement.executeQuery("select distinct   a.CZN_NrPelny, a.CZN_TwrKod, a.czn_ilosc\n" +
                            ",(select  sum(TrN_RazemNetto)\n" +
                            "from CtiZlecenieNag  b\n" +
                            "join CtiZlecenieDok on CZn_ID = CZD_CZNId\n" +
                            "join cdn.TraNag on Trn_TrNID = CZD_TrnId\n" +
                            " where TrN_TypDokumentu = 304 and TRN_Anulowany = 0 AND a.CZN_NrPelny = CZN_NrPelny) as 'Suma RW' \n" +
                            ",(select  sum(TrE_Ilosc)\n" +
                            " from CtiZlecenieNag \n" +
                            "join CtiZlecenieDok on CZn_ID = CZD_CZNId\n" +
                            "join cdn.TraNag on CZD_Numer = TrN_NumerPelny\n" +
                            "join cdn.TraElem on TrN_TrNID = TrE_TrNId\n" +
                            " where TrN_TypDokumentu = 303 AND a.CZN_NrPelny = CZN_NrPelny And (CZD_Odpady = 0 or CZD_Odpady is null))  as 'Ilość wyprodukowana' \n" +
                            " ,(select  sum(TrN_RazemNetto)\n" +
                            "from CtiZlecenieNag \n" +
                            "join CtiZlecenieDok on CZn_ID = CZD_CZNId\n" +
                            "join cdn.TraNag on CZD_Numer = TrN_NumerPelny\n" +
                            "where CZD_Odpady = 1 AND a.CZN_NrPelny = CZN_NrPelny) as 'Suma Odpadów(Koszt)'\n" +
                            " ,(select  sum(Cze_cena)\n" +
                            "from CtiZlecenieNag  \n" +
                            "join CtiZlecenieElem on CZE_CZNId = CZN_ID\n" +
                            "where  a.CZN_NrPelny = CZN_NrPelny and (cze_typ = 4 OR cze_typ = 5)) as 'Koszty dodatkowe'\n" +
                            ",(select \n" +
                            "SUM(case\n" +
                            "when CZ_JMCzasu = 2 then Round(DATEDIFF(SECOND,ZZs_DataOD,ZZs_DataDo)*CZ_KosztPracy/3600,5)\n" +
                            "when CZ_JMCzasu = 1 then Round(DATEDIFF(SECOND,ZZs_DataOD,ZZs_DataDo)*CZ_KosztPracy/60,5)\n" +
                            "when CZ_JMCzasu = 0 then Round(DATEDIFF(SECOND,ZZs_DataOD,ZZs_DataDo)*CZ_KosztPracy,5)\n" +
                            "end)\n" +
                            "\n" +
                            "from CtiZlecenieZasob \n" +
                            "right  join CtiZasob on ZZs_CZID = CZ_ID\n" +
                            "right join CtiZlecenieNag on ZZs_CZNID = CZN_ID\n" +
                            "where a.CZN_NrPelny = CZN_NrPelny) as 'Koszt Zasobu'\n" +
                            ",a.CZN_ID\n" +
                            "from CtiZlecenieNag a\n" +
                            "group by  a.CZN_NrPelny,a.CZN_TwrKod,a.czn_ilosc, a.CZN_ID\n" +
                            "order by  a.CZN_ID DESC");

                    while (rs.next()) {
                        String orderNumber = rs.getString(1);
                        String product = rs.getString(2);
                        double planProductQuantity = rs.getDouble(3);
                        double materialCost = rs.getDouble(4);
                        double productQuantity = rs.getDouble(5);
                        double additionalCost = rs.getDouble(7);
                        double resourcesCost = rs.getDouble(8);
                        if (orderNumber == null)
                            orderNumber = " ";
                        if (product == null)
                            product = " ";
                        orderArrayList.add(new Order(orderNumber, product, planProductQuantity, productQuantity, materialCost, resourcesCost, additionalCost));
                    }
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            orderArrayList.clear();
            loadingSpinner.setVisibility(View.VISIBLE);
        }
    }

    public enum CardName {
        WorkerTaskCard_ACTIVE,
        OrderCard_ACTIVE
    }


}













