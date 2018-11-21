package com.example.oshin.epraja;

public class Usuario {

    private int id_user;
    private String user_name;
    private String user_cpf;
    private String user_mail;
    private String user_password;



    public Usuario(int id_user, String user_name, String user_cpf, String user_mail, String user_password) {
        this.id_user = id_user;
        this.user_name = user_name;
        this.user_cpf = user_cpf;
        this.user_mail = user_mail;
        this.user_password = user_password;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_cpf() {
        return user_cpf;
    }

    public void setUser_cpf(String user_cpf) {
        this.user_cpf = user_cpf;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

}
