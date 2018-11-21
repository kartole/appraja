package com.example.oshin.epraja;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import java.sql.Connection;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RingProgressBar progressBar1;
    Handler myHandler;
    private ProgressDialog progressDialog;
    int progress = 0;
    Intent launchNextActivity;
    Button btnRegistrar;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);*/
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.nome_usuario);
        TextView nav_mail = (TextView)hView.findViewById(R.id.email_usuario);


        //nav_user.setText("Washington Elvira Filho");
        //nav_mail.setText("washin.elvira@gmail.com");



        nav_user.setText(UsuarioDAO.userName);
        nav_mail.setText(UsuarioDAO.userMail);


        btnRegistrar = findViewById(R.id.button);
        progressBar1 = findViewById(R.id.progrss1);

        if (getIntent().hasExtra("backPressed")){
            btnRegistrar.setVisibility(View.VISIBLE);
            progressBar1.setVisibility(View.GONE);
        }
        /*progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);*/
        else {






        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDeviceLocation);

        MapDeviceLocation mapDeviceLocation = new MapDeviceLocation();

        mapDeviceLocation.init(mapFragment, this, this);

        }

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String,String,String> demoDownload = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... voids) {
                        try {

                            Thread.sleep(3000);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        return "done";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if (s.equals("done")){



                            Toast.makeText(MainActivity.this, "Sucesso!", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(MainActivity.this, Product.class);
                            myIntent.putExtra("back", 1);
                            startActivity(myIntent);
                            finish();
                            //ringProgress();
                            //circularProgressButton.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
                            //Intent intent = new Intent(Login.this, MainActivity.class);
                            //startActivity(intent);

                            /*launchNextActivity = new Intent(MainActivity.this, Product.class);
                            //launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            launchNextActivity.putExtra("go", 1);
                            startActivity(launchNextActivity);*/

                        }
                    }
                };
                btnRegistrar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.VISIBLE);


                ringProgress();
                demoDownload.execute();
            }
        });




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_reservas) {

        } else if (id == R.id.nav_lojas) {

        } else if (id == R.id.nav_exit) {
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void ringProgress(){
        progressBar1.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {
                //Toast.makeText(getApplicationContext(), "Pronto!", Toast.LENGTH_SHORT).show();
                return;
            }
        });

//Looper.prepare();

myHandler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what==0){
            if (progress<100){
                progress = progress + 10;
                progressBar1.setProgress(progress);
            }
        }
        return false;
    }
});



        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<94; i++){
                    try {
                        Thread.sleep(100);
                        myHandler.sendEmptyMessage(0);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void btnProcurar_OnClick(View v){

        //progressDialog = ProgressDialog.show(this, "Please wait.",
                //"Finding direction..!", true);

        //progressDialog.show();


        //progressDialog.dismiss();
    }
}
