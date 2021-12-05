package com.example.myapplication;

import android.os.StrictMode;
import android.util.Log;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class SqlConnection {

    static String ip = "192.168.1.198\\optima";
    static String db = "";
    static String un = "sa";
    static String password = "Comarch!2011";
    String [] serverData;


    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            serverData = ip.split("\\\\");
            //ConnURL = "jdbc:jtds:sqlserver://" + ip + ";DatabaseName=" +"loginTimeout=5;socketTimeout=2" + db + ";user=" + un + ";password=" + password + "loginTimeout=5;socketTimeout=2";
            //ConnURL = "jdbc:jtds:sqlserver://" + ip +"/" + db  + ";instance=Optima2;loginTimeout=5;socketTimeout=5;queryTimeout=5";
            ConnURL = "jdbc:jtds:sqlserver://" + serverData[0] +"/" + db  + ";instance=" + serverData[1] + ";loginTimeout=5;socketTimeout=2;queryTimeout=5;sendStringParametersAsUnicode=false";
            conn = DriverManager.getConnection(ConnURL, un, password);


        } catch (SQLException se) {
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e) {
            Log.e("error here 3 : ", e.getMessage());
        }

        return conn;
    }


}
