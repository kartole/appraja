package com.example.oshin.epraja;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ConnectionDB {

    @SuppressLint("NewAPI")
    public Connection connectionDB(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://eprajaproject.database.windows.net:1433;DatabaseName=epraja_project;user=washington.filho@eprajaproject;password=vDcCAgDb2104?;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            connection = DriverManager.getConnection(ConnectionURL);
        }catch (SQLException se){
            Log.e("Erro de conexão!", se.getMessage());
        }catch (ClassNotFoundException e){
            Log.e("Classe não encontrada!", e.getMessage());
        }catch (Exception e){
            Log.e("Exceção!", e.getMessage());
        }
        return connection;
    }

}
