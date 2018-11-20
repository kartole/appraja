package com.example.oshin.epraja;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;

import modules.Route;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<ModelProduct> mList;
    ProductAdapter(Context context, ArrayList<ModelProduct> list){
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View view = layoutInflater.inflate(R.layout.rv_product_items, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {



        final ModelProduct productItem = mList.get(position);
        final ImageView image = viewHolder.item_image;
        final TextView name, place, price, distancia;
        final String cep, rua, num, bairro, cidade, uf;


        name = viewHolder.item_name;
        place = viewHolder.item_place;
        price = viewHolder.item_price;
        distancia = viewHolder.item_distancia;


        MapsActivity mapsActivity = new MapsActivity();


        LatLng deviceLatLng = MapDeviceLocation.latLng;
        LatLng storeLatLng = mapsActivity.getStoreLocation(productItem.getRua(), productItem.getNum(), productItem.getBairro(), productItem.getCidade(), productItem.getUf());

        String[] latLong1 = deviceLatLng.toString().split(",");
        Double lat1 = Double.parseDouble(latLong1[0].replace("lat/lng: (","").trim());
        Double lng1 = Double.parseDouble(latLong1[1].replace(")", "").trim());
        String[] latLong2 = storeLatLng.toString().split(",");
        Double lat2 = Double.parseDouble(latLong2[0].replace("lat/lng: (","").trim());
        Double lng2 = Double.parseDouble(latLong2[1].replace(")", "").trim());

        mapsActivity.startSendRequest(deviceLatLng, storeLatLng);

        //Double dist = mapsActivity.distance(lat1, lng1, lat2, lng2);//mapsActivity.distanceRoute;//mapsActivity.distance(lat1, lng1, lat2, lng2);

        Route route = new Route();
        distancia.setText(route.distance.text);


        DecimalFormat df = new DecimalFormat("0.00#");
        //String distFormated = df.format(dist)+ " Km";

        //distancia.setText(distFormated);

        String priceFormated = "R$ " + df.format(Double.parseDouble(productItem.getPrice()));

        image.setImageResource(productItem.getImage());
        name.setText(productItem.getName());
        place.setText(productItem.getPlace());
        price.setText(priceFormated);






        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, MapsActivity.class);
                intent.putExtra("item_image", productItem.getImage());
                intent.putExtra("item_name", productItem.getName());
                intent.putExtra("item_place", productItem.getPlace());
                intent.putExtra("item_price", productItem.getPrice());
                intent.putExtra("item_dist", productItem.getDistancia());
                intent.putExtra("item_cep", productItem.getCep());
                intent.putExtra("item_rua", productItem.getRua());
                intent.putExtra("item_num", productItem.getNum());
                intent.putExtra("item_bairro", productItem.getBairro());
                intent.putExtra("item_cidade", productItem.getCidade());
                intent.putExtra("item_uf", productItem.getUf());
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout parentLayout;
        ImageView item_image;
        TextView item_name, item_place, item_price, item_distancia;
        public ViewHolder(View itemView){
            super(itemView);

            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);
            item_place = itemView.findViewById(R.id.item_place);
            item_price = itemView.findViewById(R.id.item_price);
            item_distancia = itemView.findViewById(R.id.item_distancia);

            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }


}
