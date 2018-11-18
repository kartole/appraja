package com.example.oshin.epraja;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ProductItens extends Activity {

    RecyclerView recyclerView;

    ArrayList<ModelProduct> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_product_items);

        recyclerView = findViewById(R.id.rv);

        productsList = new ArrayList<>();

    }



}
