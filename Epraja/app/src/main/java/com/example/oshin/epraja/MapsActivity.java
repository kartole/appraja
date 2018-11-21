package com.example.oshin.epraja;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import modules.DirectionFinder;
import modules.Route;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

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
    public static Double distanceRoute;
    private ProgressDialog progressDialog;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private List<LatLng> list;
    private Polyline polyline;
    private long distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_product_items_detailed);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();


        getIncomingIntent();


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


    @Override
    public void onMapReady(GoogleMap googleMap) {

        //List<Address> addressList = null;

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);

        /*String itemRua = getIntent().getStringExtra("item_rua");
        String itemNum = getIntent().getStringExtra("item_num");
        String itemBairro = getIntent().getStringExtra("item_bairro");
        String itemCidade = getIntent().getStringExtra("item_cidade");
        String itemUf = getIntent().getStringExtra("item_uf");*/

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


        /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
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
        });*/

    }





    //@Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }




    //@Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));

            //distanceRoute = Double.parseDouble(route.distance.text);
            ((TextView) findViewById(R.id.item_distancia)).setText(route.distance.text);

            //((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            //((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    private void sendRequest(){



        List<Address> addressList[] = new List[2];


        String[] latLong1 = listPoints.get(0).toString().split(",");
        Double lat1 = Double.parseDouble(latLong1[0].replace("lat/lng: (","").trim());
        Double lng1 = Double.parseDouble(latLong1[1].replace(")", "").trim());

        String[] latLong2 = listPoints.get(1).toString().split(",");
        Double lat2 = Double.parseDouble(latLong2[0].replace("lat/lng: (","").trim());
        Double lng2 = Double.parseDouble(latLong2[1].replace(")", "").trim());

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        try {
            addressList[0] = geocoder.getFromLocation(lat1, lng1, 1);
            addressList[1] = geocoder.getFromLocation(lat2, lng2, 2);
        }
        catch (IOException e) {
            e.printStackTrace();
        }



        Address addressO = addressList[0].get(0);
        Address addressD = addressList[1].get(1);


        String origin = addressO.toString();
        String destination = addressD.toString();



        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest(LatLng _origin, LatLng _destination){



        List<Address> addressList[] = new List[2];


        String[] latLong1 = _origin.toString().split(",");
        Double lat1 = Double.parseDouble(latLong1[0].replace("lat/lng: (","").trim());
        Double lng1 = Double.parseDouble(latLong1[1].replace(")", "").trim());

        String[] latLong2 = _destination.toString().split(",");
        Double lat2 = Double.parseDouble(latLong2[0].replace("lat/lng: (","").trim());
        Double lng2 = Double.parseDouble(latLong2[1].replace(")", "").trim());

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        try {
            addressList[0] = geocoder.getFromLocation(lat1, lng1, 1);
            addressList[1] = geocoder.getFromLocation(lat2, lng2, 2);
        }
        catch (IOException e) {
            e.printStackTrace();
        }



        Address addressO = addressList[0].get(0);
        Address addressD = addressList[1].get(1);


        String origin = addressO.toString();
        String destination = addressD.toString();


        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            new DirectionFinder(this, origin, destination).calcDistance();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
            /*MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(markerOptions);
*/

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
                           /* MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);

                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            mMap.addMarker(markerOptions);
                            */
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

    public void startSendRequest(LatLng origin, LatLng destination){

        sendRequest(origin, destination);

    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );

        sendRequest();

        //listPoints.add(latLng);
        /*if (listPoints.size() == 2) {
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
        }*/

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
/*
    private void setDistance (Double distance){
        DecimalFormat df = new DecimalFormat("0.00#");
        TextView dist = findViewById(R.id.item_distancia);

        dist.setText(df.format(distance)+ " Km");
    }
*/
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



    public void drawRoute(){
        PolylineOptions po;


        if(polyline == null){
            po = new PolylineOptions();

            for(int i = 0, tam = list.size(); i < tam; i++){
                po.add(list.get(i));
            }

            po.color(Color.BLACK).width(4);
            polyline = mMap.addPolyline(po);
        }
        else{
            polyline.setPoints(list);
        }
    }

    public static double distance(LatLng StartP, LatLng EndP) {
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6366000 * c;
    }


    public void getLocation(View view){
        Geocoder gc = new Geocoder(MapsActivity.this);

        List<Address> addressList;
        try {
            //addressList = gc.getFromLocation(list.get(list.size() - 1).latitude, list.get(list.size() - 1).longitude, 1);
            addressList = gc.getFromLocationName("Rua dos Guajajaras 329, Belo Horizonte, Minas Gerais, Brasil", 1);

            String address = "Rua: "+addressList.get(0).getThoroughfare()+"\n";
            address += "Cidade: "+addressList.get(0).getSubAdminArea()+"\n";
            address += "Estado: "+addressList.get(0).getAdminArea()+"\n";
            address += "País: "+addressList.get(0).getCountryName();

            LatLng ll = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());

            //Toast.makeText(MainActivity.this, "Local: "+address, Toast.LENGTH_LONG).show();
            Toast.makeText(MapsActivity.this, "LatLng: "+ll, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    /* ***************************************** ROTA ***************************************** */

    public void getRouteByGMAV2(String etO, String etD, Double latO, Double lngO, Double latD, Double lngD) throws UnsupportedEncodingException {
        //EditText etO = (EditText) findViewById(R.id.origin);
        //EditText etD = (EditText) findViewById(R.id.destination);


        String origin = URLEncoder.encode(etO, "UTF-8");
        String destination = URLEncoder.encode(etD, "UTF-8");

        getRoute(new LatLng(latO, lngO), new LatLng(latD, lngD));
    }




    // WEB CONNECTION
    //public void getRoute(final String origin, final String destination){
    public void getRoute(final LatLng origin, final LatLng destination){
        new Thread(){
            public void run(){
						/*String url= "http://maps.googleapis.com/maps/api/directions/json?origin="
								+ origin+"&destination="
								+ destination+"&sensor=false";*/
                String url= "https://maps.googleapis.com/maps/api/directions/json?origin="
                        + origin.latitude+","+origin.longitude+"&destination="
                        + destination.latitude+","+destination.longitude+"&sensor=false"
                        +"&key="+MY_API_KEY;


                HttpResponse response;
                HttpGet request;
                AndroidHttpClient client = AndroidHttpClient.newInstance("route");

                request = new HttpGet(url);
                try {
                    response = client.execute(request);
                    final String answer = EntityUtils.toString(response.getEntity());

                    runOnUiThread(new Runnable(){
                        public void run(){
                            try {
                                //Log.i("Script", answer);
                                list = buildJSONRoute(answer);
                                drawRoute();
                            }
                            catch(JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }




    // PARSER JSON
    public List<LatLng> buildJSONRoute(String json) throws JSONException {
        JSONObject result = new JSONObject(json);
        JSONArray routes = result.getJSONArray("routes");

        distance = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getInt("value");

        JSONArray steps = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        List<LatLng> lines = new ArrayList<LatLng>();

        for(int i=0; i < steps.length(); i++) {
            Log.i("Script", "STEP: LAT: "+steps.getJSONObject(i).getJSONObject("start_location").getDouble("lat")+" | LNG: "+steps.getJSONObject(i).getJSONObject("start_location").getDouble("lng"));


            String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");

            for(LatLng p : decodePolyline(polyline)) {
                lines.add(p);
            }

            Log.i("Script", "STEP: LAT: "+steps.getJSONObject(i).getJSONObject("end_location").getDouble("lat")+" | LNG: "+steps.getJSONObject(i).getJSONObject("end_location").getDouble("lng"));
        }

        return(lines);
    }




    // DECODE POLYLINE
    private List<LatLng> decodePolyline(String encoded) {

        List<LatLng> listPoints = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            Log.i("Script", "POL: LAT: "+p.latitude+" | LNG: "+p.longitude);
            listPoints.add(p);
        }
        return listPoints;
    }


/*
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
*/
/*
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
*/
/*
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
    }*/
/*
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
*/

/*
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
*/
       /* @Override
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

        }*/
    }
