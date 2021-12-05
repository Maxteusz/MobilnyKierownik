package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MenuActivity extends AppCompatActivity {
    SqlConnection connection;
    TextView textTask, textWarehouse;
    Timer timer;
    MaterialButton settingButton;
    EditText ip_Text, username_Text, password_Text;
    Dialog dialog;
    CardView card_view, search_cardView, testCard, warehouseCard;
    Connection con;
    boolean isAccident;
    ImageView taskImage;


    int x;
    ArrayAdapter<String> adapter;
    ArrayList<String> listOfDatabases;
    AutoCompleteTextView editTextFilledExposedDropdown;
    TextInputLayout inputLayout, ipEditTextLayout;


    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        dialog = new Dialog(MenuActivity.this);

        testCard = findViewById(R.id.testCard);
        isAccident = false;

        listOfDatabases = new ArrayList<>();
        textTask = (TextView) findViewById(R.id.text_task);
        textWarehouse = findViewById(R.id.text_warehouse);

        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, listOfDatabases);
        settingButton = (MaterialButton) findViewById(R.id.setting_button);
        taskImage = findViewById(R.id.task_image);

        connection = new SqlConnection();
        ip_Text = dialog.findViewById(R.id.ip_editText);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timer.cancel();
                timer.purge();
                listOfDatabases.clear();
                dialog.setContentView(R.layout.dialog_setting);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                editTextFilledExposedDropdown = dialog.findViewById(R.id.filled_exposed_dropdown);
                editTextFilledExposedDropdown.setAdapter(adapter);
                ipEditTextLayout = dialog.findViewById(R.id.ip_editText_layout);


                inputLayout = dialog.findViewById(R.id.database_editText);
                inputLayout.setEndIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listOfDatabases.clear();
                        new LoadingListDatabases().execute();
                        ipEditTextLayout.clearFocus();
                        ip_Text.clearFocus();
                        username_Text.clearFocus();
                        password_Text.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(ip_Text.getWindowToken(), 0);
                        //editTextFilledExposedDropdown.showDropDown();
                    }
                });
                ip_Text = dialog.findViewById(R.id.ip_editText);
                username_Text = dialog.findViewById(R.id.username_editText);
                password_Text = dialog.findViewById(R.id.password_editText);
                ip_Text.setText(SqlConnection.ip);
                editTextFilledExposedDropdown.setText(SqlConnection.db);
                username_Text.setText(SqlConnection.un);
                password_Text.setText(SqlConnection.password);
                dialog.show();


            }
        });
        card_view = findViewById(R.id.TaskWorkerCard);
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (x == 0) {
                    Intent intent = new Intent(getApplicationContext(), WorkerTaskActivity.class);
                    startActivity(intent);
                    x++;

                }
            }
        });
        search_cardView = findViewById(R.id.SearchCard);
        search_cardView.setOnClickListener(v -> {

            if (x == 0) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                x++;
            }
        });

        testCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (x == 0) {
                    Intent intent = new Intent(getApplicationContext(), ScanOccpancy.class);
                    startActivity(intent);
                    x++;
                }
            }
        });
        testCard.setVisibility(View.INVISIBLE);

        warehouseCard = findViewById(R.id.WarehouseCard);
        warehouseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (x == 0) {
                    Intent intent = new Intent(getApplicationContext(), WarehouseActivity.class);
                    startActivity(intent);
                    x++;
                }
            }
        });

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        x = 0;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                new getData().execute();
            }
        }, 0, 1000);
    }

    @Override
    protected void onPause() {
        timer.cancel();
        timer.purge();


        adapter.notifyDataSetChanged();
        super.onPause();
    }


    @Override
    protected void onStop() {
        timer.cancel();
        timer.purge();

        super.onStop();
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public class getData extends AsyncTask<Void, Void, Void> {
        int number_tasks, warehouse;

        public getData() {
            number_tasks = 0;
            warehouse = 0;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            textTask.setText(number_tasks + "");
            textWarehouse.setText(warehouse + "");
            adapter.notifyDataSetChanged();
            if (isAccident) {
                taskImage.setBackgroundResource(R.drawable.active_logo_error);
                taskImage.setBackgroundTintList(null);
                Log.i("Zgłoszono awarie", isAccident + "");
            }
            else
                Log.i("Nie ma awarii", isAccident + "");


        }

        @Override
        protected Void doInBackground(Void... voids) {

            con = connection.CONN();
            if (con != null) {

                try {
                    Statement statement = null;
                    statement = con.createStatement();
                    ResultSet rs = null;

                    rs = statement.executeQuery("SELECT count(ZZs_ID) as ilośćZlecen\n" +
                            " ,(select count(*) from\n" +
                            "(select twr_kod, Twr_IloscMin, sum(TwZ_Ilosc) as ilosc from cdn.Towary\n" +
                            "left join cdn.TwrZasoby on Twr_TwrId = TwZ_TwrId\n" +
                            "where Twr_IloscMin > 0 AND Twr_Typ = 1 AND (TwZ_Ilosc < Twr_IloscMin or TwZ_Ilosc is null)\n" +
                            "group by  twr_kod, Twr_IloscMin) as f)\n" +
                            ",(select top 1 [IDPrzerwy]\n" +
                            "FROM [dbo].[CtiProdukcjaPanelPrzerwy]\n" +
                            "where DataZakonczenia is null)\n" +
                            "FROM CtiZlecenieZasob where ZZs_DataDo is null");
                    while (rs.next()) {
                        number_tasks = rs.getInt(1);
                        warehouse = rs.getInt(2);
                        if (rs.getInt(3) > 0)
                            isAccident = true;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }


    public void setSettings(View v) throws SQLException {

        SqlConnection.ip = ip_Text.getText() + "";
        SqlConnection.db = editTextFilledExposedDropdown.getText().toString();
        SqlConnection.un = username_Text.getText() + "";
        SqlConnection.password = password_Text.getText() + "";
        saveSettings(SqlConnection.ip, SqlConnection.db, SqlConnection.un, SqlConnection.password);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                new getData().execute();
            }
        }, 0, 2000);
        if (!CheckLicense()) {
            warehouseCard.setVisibility(View.INVISIBLE);
            search_cardView.setVisibility(View.INVISIBLE);
            card_view.setVisibility(View.INVISIBLE);
        } else {
            warehouseCard.setVisibility(View.VISIBLE);
            search_cardView.setVisibility(View.VISIBLE);
            card_view.setVisibility(View.VISIBLE);
        }
        dialog.dismiss();
    }

    public void saveSettings(String ip, String database, String usersname, String password) {
        SharedPreferences sharedPref = this.getSharedPreferences("general_setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ip, "ipData");
        editor.putString(database, "databaseData");
        editor.putString(usersname, "usernameData");
        editor.putString(password, "passwordData");
        editor.commit();

    }

    public class LoadingListDatabases extends AsyncTask<Void, Void, Void> {

        ArrayList<String> tempArraydatabese;
        String error = "Sprawdź połączenie";

        @Override
        protected Void doInBackground(Void... voids) {

            SqlConnection connection = new SqlConnection();
            Connection con = connection.CONN();
            tempArraydatabese = new ArrayList<>();


            if (con != null) {
                try {
                    Statement statement = null;
                    statement = con.createStatement();
                    ResultSet rs = null;
                    rs = statement.executeQuery("SELECT name FROM master.sys.databases where database_id > 4");

                    while (rs.next()) {
                        tempArraydatabese.add(rs.getString(1));
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
            listOfDatabases.clear();
            editTextFilledExposedDropdown.setText("Ładowanie...");
            SqlConnection.ip = ip_Text.getText().toString();
            SqlConnection.password = password_Text.getText().toString();
            SqlConnection.un = username_Text.getText().toString();
            SqlConnection.db = "";


        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            editTextFilledExposedDropdown.getEditableText().clear();
            adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, listOfDatabases);
            editTextFilledExposedDropdown.setAdapter(adapter);
            listOfDatabases.addAll(tempArraydatabese);
            if (listOfDatabases.isEmpty()) {
                editTextFilledExposedDropdown.setText(error);
            } else
                editTextFilledExposedDropdown.getEditableText().clear();
            editTextFilledExposedDropdown.showDropDown();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }

    public boolean CheckLicense() throws SQLException {

        boolean temp = false;
        SqlConnection connection = new SqlConnection();
        Connection con = connection.CONN();

        Date date = null;
        ResultSet rs = null;
        Date limitDate = null;
        String dtStart = "2022-02-30";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            limitDate = format.parse(dtStart);
            System.out.println(limitDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (con != null) {
            try {

                Statement statement = null;
                statement = con.createStatement();
                rs = statement.executeQuery("select getDate()");
                if (rs.next()) {
                    date = rs.getDate(1);

                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (date != null)
            if (date.after(limitDate)) {
                temp = false;
            } else {
                temp = true;
            }
        return temp;
    }


}







