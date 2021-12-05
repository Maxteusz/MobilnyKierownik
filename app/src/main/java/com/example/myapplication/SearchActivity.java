package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    MaterialButton button;
    TextView kodProduct, nameProduct, contraintsDocument,customer;
    SqlConnection connection;
    ListView listView;
    CargoListViewAdapter adapter ;
    ArrayList<Cargo> cargoArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        button = findViewById(R.id.scan_button);
        listView = findViewById(R.id.list_item);
        cargoArrayList = new ArrayList<>();
      adapter = new CargoListViewAdapter(this,cargoArrayList);
      listView.setAdapter(adapter);
        getWindow().setStatusBarColor(Color.parseColor("#03a9f4"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(SearchActivity.this).setCaptureActivity(ScanActivity.class).setPrompt("Zeskanuj Produkt").setBarcodeImageEnabled(true).setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES).initiateScan();

            }
        });
        kodProduct = findViewById(R.id.kod_product);
        nameProduct = findViewById(R.id.name_product);

        customer = findViewById(R.id.customer);
        connection = new SqlConnection();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Błąd odczytania ", Toast.LENGTH_LONG).show();
            } else {
                kodProduct.setText(result.getContents()+"");
                getSourceDocument(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public String getSourceDocument(String document) {
        String result = "";
        Connection con = connection.CONN();
        if (con != null) {
            Statement statement = null;

            try {
                statement = con.createStatement();
                statement.setQueryTimeout(5);

                ResultSet rs = null;

                rs = statement.executeQuery("SELECT TrR_TrNId, TrR_FaId\n" +
                        "     ,(select TrN_NumerPelny from CDN.TraNag where TrR_TrNId = TrN_TrNID) as [Matka]\n" +
                        "  FROM CDN.TraNagRelacje \n" +
                        "where  \n" +
                        "EXISTS \n" +
                        "(select  TrN_NumerPelny from CDN.TraNag where TrR_FaId = TrN_TrNID AND TrN_NumerPelny ='" + document + "')");
                if (rs.next()) {

                    result =  rs.getString(3);
                    getCargoOnDocument(statement,document);

                }
                else {

                    getCargoOnDocument(statement, document);
                    customer.setText("Brak Powiązania");
                }
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                result = "Brak danych";

            }
        }
        return result;
    }

    public void getCargoOnDocument(Statement statement, String document) {
        String temp = "";
        ResultSet rs = null;
        cargoArrayList.clear();

        try {
            rs = statement.executeQuery("select Tre_TrNid, TrN_NumerPelny, TrE_TwrNazwa, TrE_Ilosc, TrE_Jm, TrN_OdbNazwa1 from CDN.TraElem \n" +
                    "join CDN.TraNag on TrE_TrNId = TrN_TrNId \n" +
                    "where TrN_NumerPelny = '" + document +"'");

            while (rs.next())
            {

              customer.setText(rs.getString(6)+"");
              cargoArrayList.add(new Cargo(rs.getString(3).toString(),rs.getInt(4),rs.getString(5).toLowerCase() ));
              Log.i("Ilośc", cargoArrayList.size()+"");
            }
            nameProduct.setText(temp+"");
            adapter.notifyDataSetChanged();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}




