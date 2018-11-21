package com.example.oshin.epraja;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UsuarioDAO{

    ArrayList<Usuario> productsList;
    String z = null;

    public static int userId;
    public static String userName;
    public static String userMail;
    public static String userCPF;



    public boolean validateUser(String user, String password) {


        try{
            ConnectionDB connect = new ConnectionDB();
            Connection con = connect.connectionDB();
            if (con == null) {
                z = "Cheque a conexão!";
            } else {
                String query = "SELECT A.ID_USER, " +
                        "A.USER_NAME, " +
                        "A.CPF, " +
                        "A.EMAIL, " +
                        "A.USER_PASSWORD, " +
                        "A.ATIVO " +
                        "FROM TB_T_USER A " +
                        "WHERE (EMAIL = '" + user + "'" +
                        " OR CPF = '" + user + "')" +
                        " AND USER_PASSWORD = '" + password + "'" +
                        " AND ATIVO = 1"
                        ;
                Statement statement = null;
                try {
                    statement = con.createStatement();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                ResultSet rs = statement.executeQuery(query);

                //productsList = new ArrayList<>();

                if (rs.next()) {

                    String id_user = rs.getString("ID_USER");

                    if (id_user == null){
                        return false;
                    }
                    else {

                        int id_usuario = rs.getInt("ID_USER");
                        String nome = rs.getString("USER_NAME");
                        String cpf = rs.getString("CPF");
                        String email = rs.getString("EMAIL");
                        String passwrd = rs.getString("USER_PASSWORD");

                        Usuario usuario = new Usuario(id_usuario,
                                                        nome,
                                                        cpf,
                                                        email,
                                                        passwrd
                                                        );
                        userId = usuario.getId_user();
                        userName = usuario.getUser_name();
                        userCPF = usuario.getUser_cpf();
                        userMail = usuario.getUser_mail();

                        return true;
                    }
                } else {
                    z = "Invalid Query";
                }
            }
        }
        catch (Exception e){
            z = e.getMessage();
            Log.d("Sql Error", z);
            return false;
        }
        return false;
    }



}






