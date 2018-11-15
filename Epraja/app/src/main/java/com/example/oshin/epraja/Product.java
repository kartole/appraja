package com.example.oshin.epraja;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Product extends Activity {

    RecyclerView recyclerView;

    ArrayList<ModelProduct> productsList;
//aqui
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        String z = null;
        try {
            ConnectionDB connect = new ConnectionDB();
            Connection con = connect.connectionDB();
            if (con == null){
                z = "Cheque a conexão!";
            }
            else {
                String query = "select A.IMG_NOME, A.NOME, B.NOME_LOJA AS LOJA, A.PRECO, '4,5KM' AS DISTANCIA FROM TB_T_PRODUTO A INNER JOIN TB_T_LOJA B ON A.ID_LOJA = B.ID_LOJA";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if (rs.next()){
                    recyclerView = findViewById(R.id.rv);

                    productsList = new ArrayList<>();
                    int out = 0;
                    int drawableID;

                    while (out < 1){
                        drawableID = getResources().getIdentifier(rs.getString("IMG_NOME"), "drawable", getPackageName());

                        productsList.add(new ModelProduct(drawableID,
                                            rs.getString("NOME"),
                                            rs.getString("LOJA"),
                                            rs.getString("PRECO"),
                                            rs.getString("DISTANCIA"))
                        );

                        if (rs.isLast() == true){
                            out++;
                        }
                        else {
                            rs.next();
                        }
                    }



                    /*productsList.add(new ModelProduct(R.drawable.sample_1,"Cachorro 1", "C&A Modas", "R$ 25,90", "2,5km"));
                    productsList.add(new ModelProduct(R.drawable.sample_2,"Camiseta Nike", "C&A Modas", "R$ 25,90", "5,2km"));
                    productsList.add(new ModelProduct(R.drawable.sample_3,"Camiseta Nike", "C&A Modas", "R$ 25,90", "3,8km"));
                    productsList.add(new ModelProduct(R.drawable.sample_4,"Camiseta Nike", "C&A Modas", "R$ 25,90","0,2km"));*/


                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;

                    recyclerView.setLayoutManager(rvLiLayoutManager);

                    ProductAdapter adapter = new ProductAdapter(this, productsList);
                    recyclerView.setAdapter(adapter);
                }
                else {
                    z = "Invalid Query";
                }
            }
        }catch (Exception e){
            z = e.getMessage();
            Log.d("Sql Error", z);
        }




//finaliza aqui
        /*recyclerView = findViewById(R.id.rv);

        productsList = new ArrayList<>();
        productsList.add(new ModelProduct(R.drawable.tn_camiseta_tmb,"Camiseta Nike", "C&A Modas", "R$ 25,90", "4km"));
        productsList.add(new ModelProduct(R.drawable.sample_1,"Cachorro 1", "C&A Modas", "R$ 25,90", "2,5km"));
        productsList.add(new ModelProduct(R.drawable.sample_2,"Camiseta Nike", "C&A Modas", "R$ 25,90", "5,2km"));
        productsList.add(new ModelProduct(R.drawable.sample_3,"Camiseta Nike", "C&A Modas", "R$ 25,90", "3,8km"));
        productsList.add(new ModelProduct(R.drawable.sample_4,"Camiseta Nike", "C&A Modas", "R$ 25,90","0,2km"));


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;

        recyclerView.setLayoutManager(rvLiLayoutManager);

        ProductAdapter adapter = new ProductAdapter(this, productsList);
        recyclerView.setAdapter(adapter);*/
    }

}
