package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DocumentActivity extends AppCompatActivity {

    TextView order_number;
    SqlConnection sqlConnection;
    Connection connection;
    String orderNumber;
    int workerID;
    ArrayList<Document> documentArrayList;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    DocumentAdapter documentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        getWindow().setStatusBarColor(Color.parseColor("#03a9f4"));
        order_number = findViewById(R.id.order_number);
        recyclerView = findViewById(R.id.document_recyclerView);
        sqlConnection = new SqlConnection();
        Intent intent = getIntent();
        orderNumber = intent.getStringExtra("Numer Zlecenia");

        order_number.setText(orderNumber + "");
        workerID = intent.getIntExtra("WorkerID",0);
        Log.i("Pracownik", workerID+"");
        documentArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.document_recyclerView);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        documentAdapter = new DocumentAdapter(getApplicationContext(), documentArrayList);
        recyclerView.setAdapter(documentAdapter);


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        documentArrayList.clear();
        new getDocuments().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        documentArrayList.clear();


    }

    public class getDocuments extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
            documentAdapter.notifyDataSetChanged();

        }

        protected Void doInBackground(Void... voids) {

            try {
                connection = sqlConnection.CONN();
                ResultSet rs = null;
                if (connection != null) {
                    Statement statement = connection.createStatement();
                    rs = statement.executeQuery("SELECT CZD_Numer, CZD_DataWyst, TrN_Bufor, Trn_RazemNetto from dbo.CtiZlecenieDok \n" +
                            " join CtiZlecenieNag ON CZn_Id = CZD_CZNId" + "\n" +
                            " join cdn.TraNag on Trn_TrNID = CZD_TrnId \n" +
                            "where CZN_NrPelny = '" + orderNumber + "'" + " AND TrN_Anulowany = 0 and CZD_PracownikId = " + workerID);
                    while (rs.next()) {
                        documentArrayList.add(new Document(rs.getString(1).toString(), rs.getString(2).toString(), rs.getInt(3), rs.getDouble(4)));
                    }
                    connection.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            documentArrayList.clear();

        }
    }


}
