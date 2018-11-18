package com.example.oshin.epraja;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class rv_product_items_detailed extends AppCompatActivity {

    ImageView item_image;
    EditText item_name, item_place, item_price, item_distancia;

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_product_items_detailed);

        if (getIntent().hasExtra("call_maps")) {
            getIncomingIntent();
        }
        else {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("call_maps", "doing");
            startActivity(intent);
        }

        //onMapReady();
    }

    private void getIncomingIntent(){
        if (getIntent().hasExtra("item_image") &&
                getIntent().hasExtra("item_name") &&
                getIntent().hasExtra("item_place") &&
                getIntent().hasExtra("item_price") &&
                getIntent().hasExtra("item_dist")){
            int itemImg = getIntent().getIntExtra("item_image", 1) ;
            String itemName = getIntent().getStringExtra("item_name");
            String itemPlace = getIntent().getStringExtra("item_place");
            String itemPrice = getIntent().getStringExtra("item_price");
            String itemDist = getIntent().getStringExtra("item_dist");

            setItem(itemImg, itemName, itemPlace, itemPrice, itemDist);
        }
    }

    private void setItem(int itemImg, String itemName, String itemPlace, String itemPrice, String itemDist){

        ImageView image = findViewById(R.id.item_image);
        TextView name = findViewById(R.id.item_name);
        TextView place = findViewById(R.id.item_place);
        TextView price = findViewById(R.id.item_price);
        TextView dist = findViewById(R.id.item_distancia);

        image.setImageResource(itemImg);
        name.setText(itemName);
        place.setText(itemPlace);
        price.setText(itemPrice);
        dist.setText(itemDist);
    }


}
