package com.example.oshin.epraja;

public class ModelProduct {

    private int image;
    private String name;
    private String place;
    private String price;
    private String distancia;

    public ModelProduct(int image, String name, String place, String price, String distancia) {
        this.image = image;
        this.name = name;
        this.place = place;
        this.price = price;
        this.distancia = distancia;
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
}
