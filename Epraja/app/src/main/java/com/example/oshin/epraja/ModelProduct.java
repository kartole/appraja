package com.example.oshin.epraja;

public class ModelProduct {

    private int image;
    private String name;
    private String place;
    private String price;
    private String distancia;

    private String cep;
    private String rua;
    private String num;
    private String bairro;
    private String cidade;
    private String uf;

    public ModelProduct(Integer image, String name, String place, String price, String distancia, String cep, String rua, String num, String bairro, String cidade, String uf) {
        if (image != null){
            this.image = image;
        }
        if (name != null){
            this.name = name;
        }
        if (place != null){
            this.place = place;
        }
        if (price != null){
            this.price = price;
        }
        if (distancia != null){
            this.distancia = distancia;
        }
        if (cep != null){
            this.cep = cep;
        }
        if (rua != null){
            this.rua = rua;
        }
        if (num != null){
            this.num = num;
        }
        if (bairro != null){
            this.bairro = bairro;
        }
        if (cidade != null){
            this.cidade = cidade;
        }
        if (uf != null){
            this.uf = uf;
        }

    }


    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public int getImage() {

        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }




    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
