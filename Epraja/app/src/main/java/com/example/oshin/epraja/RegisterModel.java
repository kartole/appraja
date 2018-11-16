package com.example.oshin.epraja;

public class RegisterModel {

    String name;
    String cpf;
    String email;
    String userPassword;

    public RegisterModel(String name, String cpf, String email, String userPassword) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.userPassword = userPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


}
