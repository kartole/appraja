package com.example.oshin.epraja;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;


import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class RegisterDAO{

    public void insertUser(String user, String cpf, String email, String password){

        ConnectionDB connect = new ConnectionDB();
        Connection con = connect.connectionDB();

        String query =  "INSERT INTO TB_T_USER(USER_NAME, CPF, EMAIL, USER_PASSWORD, ATIVO) "
                +"VALUES('"+ user +"', REPLACE(REPLACE('"+ cpf +"','.', ''), '-', ''),'"+ email +"','"+ password +"',1)";


        try{
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
            //Log.d("SQL Erro", e.getMessage());
            //return;
        }

    }

}
