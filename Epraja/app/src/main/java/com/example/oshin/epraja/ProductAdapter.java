package com.example.oshin.epraja;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

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


        name = viewHolder.item_name;
        place = viewHolder.item_place;
        price = viewHolder.item_price;
        distancia = viewHolder.item_distancia;


        image.setImageResource(productItem.getImage());
        name.setText(productItem.getName());
        place.setText(productItem.getPlace());
        price.setText(productItem.getPrice());
        distancia.setText(productItem.getDistancia());

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(mContext, rv_product_items_detailed.class);
                intent.putExtra("item_image", productItem.getImage());
                intent.putExtra("item_name", productItem.getName());
                intent.putExtra("item_place", productItem.getPlace());
                intent.putExtra("item_price", productItem.getPrice());
                intent.putExtra("item_dist", productItem.getDistancia());
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
        TextView item_name, item_place, item_price, item_endereco, item_distancia;
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
