package com.example.oshin.epraja;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.location.FusedLocationProviderClient;

import javax.xml.parsers.ParserConfigurationException;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Boolean mLocationPermissionsGranted = true;
    private static final String TAG = "MapActivity";
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> listPoints;
    private static final String MY_API_KEY = "AIzaSyDVK6dbadK5nD9qs5edwmC3_LauatJ4TnU";
    CircularProgressButton circularProgressButton;
    Double distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_product_items_detailed);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();


        getIncomingIntent();

       /* final NestedScrollView scroll = findViewById(R.id.scrolling);
        //final ScrollView scroll = (ScrollView) findViewById(R.id.scroll);
        ImageView transparent = (ImageView)findViewById(R.id.imagetrans);

        transparent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scroll.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scroll.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scroll.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });*/

        circularProgressButton = findViewById(R.id.btn_reserve);
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String, String, String> getReserve = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "reservado";
                    }

                    protected void onPostExecute(String s) {
                        if (s.equals("reservado")) {
                            Toast.makeText(getApplicationContext(), "Reservado com sucesso", Toast.LENGTH_SHORT).show();
                            circularProgressButton.doneLoadingAnimation(Color.parseColor("#886A16"), BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
                        }
                    }

                };
                circularProgressButton.startAnimation();
                getReserve.execute();
            }
        });


    }


    private void getIncomingIntent() {
        if (getIntent().hasExtra("item_image") &&
                getIntent().hasExtra("item_name") &&
                getIntent().hasExtra("item_place") &&
                getIntent().hasExtra("item_price") &&
                getIntent().hasExtra("item_dist") &&
                getIntent().hasExtra("item_cep") &&
                getIntent().hasExtra("item_rua") &&
                getIntent().hasExtra("item_num") &&
                getIntent().hasExtra("item_bairro") &&
                getIntent().hasExtra("item_cidade") &&
                getIntent().hasExtra("item_uf")) {
            int itemImg = getIntent().getIntExtra("item_image", 1);
            String itemName = getIntent().getStringExtra("item_name");
            String itemPlace = getIntent().getStringExtra("item_place");
            String itemPrice = getIntent().getStringExtra("item_price");
            String itemDist = getIntent().getStringExtra("item_dist");
            String itemCep = getIntent().getStringExtra("item_cep");
            String itemRua = getIntent().getStringExtra("item_rua");
            String itemNum = getIntent().getStringExtra("item_num");
            String itemBairro = getIntent().getStringExtra("item_bairro");
            String itemCidade = getIntent().getStringExtra("item_cidade");
            String itemUf = getIntent().getStringExtra("item_uf");

            setItem(itemImg, itemName, itemPlace, itemPrice, itemDist, itemCep, itemRua, itemNum, itemBairro, itemCidade, itemUf);
        }
    }

    private void setItem(int itemImg, String itemName, String itemPlace, String itemPrice, String itemDist,
                         String itemCep, String itemRua, String itemNum, String itemBairro, String itemCidade, String itemUf) {

        ImageView image = findViewById(R.id.item_image);
        TextView name = findViewById(R.id.item_name);
        TextView place = findViewById(R.id.item_place);
        TextView price = findViewById(R.id.item_price);
        //TextView dist = findViewById(R.id.item_distancia);
        TextView endereco = findViewById(R.id.txtEndereco);
        TextView bairro = findViewById(R.id.txtBairro);
        TextView cidadeUf = findViewById(R.id.txtCidadeUF);


        itemCep = itemCep.substring(0, 5) + "-" + itemCep.substring(5);
        DecimalFormat df = new DecimalFormat("0.00##");
        itemPrice = "R$ " + df.format(Double.parseDouble(itemPrice));

        image.setImageResource(itemImg);
        name.setText(itemName);
        place.setText(itemPlace);
        price.setText(itemPrice);
        //dist.setText(itemDist);
        endereco.setText(itemRua + ", " + itemNum + ", " + itemCep);
        bairro.setText(itemBairro);
        cidadeUf.setText(itemCidade + " - " + itemUf);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        List<Address> addressList = null;

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);

        String itemRua = getIntent().getStringExtra("item_rua");
        String itemNum = getIntent().getStringExtra("item_num");
        String itemBairro = getIntent().getStringExtra("item_bairro");
        String itemCidade = getIntent().getStringExtra("item_cidade");
        String itemUf = getIntent().getStringExtra("item_uf");

        getDeviceLocation();
        getStoreLocation();


        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
        /*String location = "Rua dos Guajajaras, 329 - Bairro Centro, Belo Horizonte - MG";

        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        Toast.makeText(getApplicationContext(), ""+ latLng +"", Toast.LENGTH_SHORT).show();
        */


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (listPoints.size() == 2) {
                    listPoints.clear();
                    mMap.clear();
                }
                listPoints.add(latLng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                if (listPoints.size() == 1) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }
                mMap.addMarker(markerOptions);

                if (listPoints.size() == 2) {
                    String url = getRequestUrl(listPoints.get(0), listPoints.get(1));
                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                    taskRequestDirections.execute(url);
                }
            }
        });

    }

    public LatLng getStoreLocation(String itemRua, String itemNum, String itemBairro, String itemCidade, String itemUf) {

        //String itemCep = getIntent().getStringExtra("item_cep");


        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        //mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Log.d(TAG, "onComplete: found location!");
        List<Address> addressList = null;

        String location = itemRua + ", " + itemNum + " - " + itemBairro + ", " + itemCidade + " - " + itemUf;
        //String location = "Rua dos Goitacazes, 182 - Centro, Belo Horizonte - MG, 30190-050";
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        return latLng;
    }

    public void getStoreLocation() {

        //String itemCep = getIntent().getStringExtra("item_cep");
        String itemRua = getIntent().getStringExtra("item_rua");
        String itemNum = getIntent().getStringExtra("item_num");
        String itemBairro = getIntent().getStringExtra("item_bairro");
        String itemCidade = getIntent().getStringExtra("item_cidade");
        String itemUf = getIntent().getStringExtra("item_uf");

        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            Log.d(TAG, "onComplete: found location!");
            List<Address> addressList = null;

            String location = itemRua + ", " + itemNum + " - " + itemBairro + ", " + itemCidade + " - " + itemUf;
            //String location = "Rua dos Goitacazes, 182 - Centro, Belo Horizonte - MG, 30190-050";
            Geocoder geocoder = new Geocoder(MapsActivity.this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            listPoints.add(latLng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(markerOptions);


        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }


    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {

            if (mLocationPermissionsGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            listPoints.add(latLng);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);

                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            mMap.addMarker(markerOptions);
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        } finally {
            final Task location = mFusedLocationProviderClient.getLastLocation();
            if (location.isSuccessful()) {
                Location currentLocation = (Location) location.getResult();
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                listPoints.add(latLng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
            }

        }

    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );

        //listPoints.add(latLng);
        if (listPoints.size() == 2) {
            String[] latLong1 = listPoints.get(0).toString().split(",");
            Double lat1 = Double.parseDouble(latLong1[0].replace("lat/lng: (","").trim());
            Double lng1 = Double.parseDouble(latLong1[1].replace(")", "").trim());
            String[] latLong2 = listPoints.get(1).toString().split(",");
            Double lat2 = Double.parseDouble(latLong2[0].replace("lat/lng: (","").trim());
            Double lng2 = Double.parseDouble(latLong2[1].replace(")", "").trim());
            setDistance(distance(lat1, lng1, lat2, lng2));



            String url = getRequestUrl(listPoints.get(0), listPoints.get(1));
            TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
            taskRequestDirections.execute(url);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void setDistance (Double distance){
        DecimalFormat df = new DecimalFormat("0.00#");
        TextView dist = findViewById(R.id.item_distancia);

        dist.setText(df.format(distance)+ " Km");
    }

    public double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private String getRequestUrl(LatLng origin, LatLng dest) {
        //Value of origin
        String str_org = "origin=" + origin.latitude +","+origin.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude+","+dest.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode=driving";
        //Build the full param
        String param = str_org +"&" + str_dest + "&" +sensor+"&" +mode;
        //Output format
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + "&key=" + MY_API_KEY;
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!=null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
