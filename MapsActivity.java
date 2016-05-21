package com.example.dk.mapsviabt;
/**
 * ------------------------------------------------------------------------------
 * ------------------------------------------------------------------------------
 * The user clicks on markers to select initial and final points
 * First marker touched is the initial one and the second one is final
 * Then the app communicates with the server and draws the best path between them
 * If the user presses back button after selecting two markers everything gets reset
 * If the user presses a third marker, the first two markers get unselected and the third marker
 * becomes the initial point
 * ------------------------------------------------------------------------------
 * IS WORKING-
 * 1)LOCATION
 * 2)MARKERS AND LATLNG ARRAYLIST
 * ------------------------------------------------------------------------------
 * TO BE CHECKED-
 * 1)SERVER COMMUNICATION - CHECK IF POST REQUEST IS DOING THE RIGHT THING
 * 2)GEODESIC TRUE/FALSE
 * ------------------------------------------------------------------------------
 * TO BE ADDED-
 * EXTRA METHODS FOR BETTER PERFORMANCE UNDER CORNER CASES
 * ------------------------------------------------------------------------------
 * ------------------------------------------------------------------------------
 */
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

//Using retrofit 1.9 and not 2.0
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    /**
     * devpos is an ArrayList containing LatLng of all devices
     * devices is an ArrayList of markers at the locations in devpos
     * route is an ArrayList of LatLng containing the points which represent the shortest path
     * between initial and final points
     */
    private ArrayList<LatLng> devpos = new ArrayList();
    private ArrayList<Marker> devices = new ArrayList<Marker>();
    private ArrayList<LatLng> route = new ArrayList();
    private double inlat = 0.0, inlong = 0.0, finlat = 0.0,  finlong = 0.0;
    int state = 0, errorcode=0;
    /*
     *errorcode=1 POST failure HUE_GREEN
     *errorcode=2 GET failure HUE_YELLOW
     */
    List<Double> initialpos = Arrays.asList(inlat,inlong);
    List<Double> finalpos = Arrays.asList(finlat,finlong);
    String ROOT_URL = "http://trafficmonitoring.pythonanywhere.com/";
    PolylineOptions polylineOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    //This method is called in onMarkerClick along with drawroute()
    private void putData(){
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(ROOT_URL).build();
        HttpRequest api = restAdapter.create(HttpRequest.class);
//        api.putData(initialpos,finalpos,new Callback<ServerData>() {
//            @Override
//            public void success(ServerData serverData, Response response) {
////                if(state==1)
////                serverData.setIntialPoint(initialpos);
////                //serverData.setFinalPoint(finalpos);
////                else if(state==2){
////                    serverData.setIntialPoint(initialpos);
////                    serverData.setFinalPoint(finalpos);
////                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                if(state!=0)
//                for(Marker marker: devices){
//                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//                }
//
//
//            }
//        });
        api.putData(initialpos, finalpos, new Callback<ServerData>() {
            @Override
            public void success(ServerData serverData, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                if(state!=0)
                for(Marker marker: devices){
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                }
                errorcode=1;
            }
        });


        api.getData(new Callback<ServerData>() {
            @Override
            public void success(ServerData serverData, Response response) {
                route.add(0,new LatLng(serverData.getPoint1().get(0),serverData.getPoint1().get(1)));
                route.add(1,new LatLng(serverData.getPoint2().get(0),serverData.getPoint2().get(1)));
                route.add(2,new LatLng(serverData.getPoint3().get(0),serverData.getPoint3().get(1)));
                route.add(3,new LatLng(serverData.getPoint4().get(0),serverData.getPoint4().get(1)));
                route.add(4,new LatLng(serverData.getPoint5().get(0),serverData.getPoint5().get(1)));
                route.add(5,new LatLng(serverData.getPoint6().get(0),serverData.getPoint6().get(1)));
                route.add(6,new LatLng(serverData.getPoint7().get(0),serverData.getPoint7().get(1)));
                route.add(7,new LatLng(serverData.getPoint8().get(0),serverData.getPoint8().get(1)));
                route.add(8,new LatLng(serverData.getPoint9().get(0),serverData.getPoint9().get(1)));
                route.add(9,new LatLng(serverData.getPoint10().get(0),serverData.getPoint10().get(1)));
                route.add(10,new LatLng(serverData.getPoint11().get(0),serverData.getPoint11().get(1)));
                route.add(11,new LatLng(serverData.getPoint12().get(0),serverData.getPoint12().get(1)));
                route.add(12,new LatLng(serverData.getPoint13().get(0),serverData.getPoint13().get(1)));
                route.add(13,new LatLng(serverData.getPoint14().get(0),serverData.getPoint14().get(1)));
                route.add(14,new LatLng(serverData.getPoint15().get(0),serverData.getPoint15().get(1)));
                route.add(15,new LatLng(serverData.getPoint16().get(0),serverData.getPoint16().get(1)));
                route.add(16,new LatLng(serverData.getPoint17().get(0),serverData.getPoint17().get(1)));
                route.add(17,new LatLng(serverData.getPoint18().get(0),serverData.getPoint18().get(1)));
                route.add(18,new LatLng(serverData.getPoint19().get(0),serverData.getPoint19().get(1)));
                route.add(19,new LatLng(serverData.getPoint20().get(0),serverData.getPoint20().get(1)));
                route.add(20,new LatLng(serverData.getPoint21().get(0),serverData.getPoint21().get(1)));


            }

            @Override
            public void failure(RetrofitError error) {

                if(state!=0)
                    for(Marker marker: devices){
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }
                errorcode=2;

            }
        });

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
        mMap = googleMap;
        /**
         * Here we have initialised devpos and devices
         * later when LatLng of all devices is available the below values will be changed
         */
        {
            //Assuming maximum of 21 device are present
            devpos.add(0, new LatLng(12.9887026, 80.2293583));
            devpos.add(1, new LatLng(12.9887026, 80.2293583));
            devpos.add(2, new LatLng(12.9887026, 80.2293583));
            devpos.add(3, new LatLng(12.9887026, 80.2293583));
            devpos.add(4, new LatLng(12.9887026, 80.2293583));
            devpos.add(5, new LatLng(12.9887026, 80.2293583));
            devpos.add(6, new LatLng(12.9887026, 80.2293583));
            devpos.add(7, new LatLng(12.9887026, 80.2293583));
            devpos.add(8, new LatLng(12.9887026, 80.2293583));
            devpos.add(9, new LatLng(12.9887026, 80.2293583));
            devpos.add(10, new LatLng(12.9887026, 80.2293583));
            devpos.add(11, new LatLng(12.9887026, 80.2293583));
            devpos.add(12, new LatLng(12.9887026, 80.2293583));
            devpos.add(13, new LatLng(12.9887026, 80.2293583));
            devpos.add(14, new LatLng(12.9887026, 80.2293583));
            devpos.add(15, new LatLng(12.9887026, 80.2293583));
            devpos.add(16, new LatLng(12.9887026, 80.2293583));
            devpos.add(17, new LatLng(12.9887026, 80.2293583));
            devpos.add(18, new LatLng(12.9887026, 80.2293583));
            devpos.add(19, new LatLng(12.9887026, 80.2293583));
            devpos.add(20, new LatLng(12.9887026, 80.2293583));

            int i = 0;
            for (LatLng Location : devpos) {
                Marker marker = mMap.addMarker(new MarkerOptions().position(Location).title("Device" + i));
                devices.add(i, marker);
                i++;
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Helps user find out his current location
        googleMap.setMyLocationEnabled(true);

        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
    }
    public boolean onMarkerClick(Marker marker){
        LatLng markloc;
        markloc = marker.getPosition();
        if (state < 2) {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            if (state == 0) {
                inlat = markloc.latitude;
                inlong = markloc.longitude;
                state = 1;
                marker.setSnippet("Initial");
            } else if (state == 1) {

                finlat = markloc.latitude;
                finlong = markloc.longitude;
                state = 2;
                marker.setSnippet("Destination");
                putData();
                drawRoute();
            }
        else{//make the third marker the initial point
                state=0;
                route.clear();
                for(Marker mark:devices)
                {
                    mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    mark.setSnippet("");
                }
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                inlat = markloc.latitude;
                inlong = markloc.longitude;
                state = 1;
                marker.setSnippet("Initial");
            }

        }
        return false;
    }
    //Function sets everything to initial state
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(state!=0){
            state=0;
            for (Marker marker: devices){
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                marker.setSnippet("");
        }
            inlat=0.0;
            inlong = 0.0;
            finlat = 0.0;
            finlong = 0.0;
            route.clear();

    }}

    private void drawRoute(){
        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE);
        polylineOptions.width(5);
//      polylineOptions.geodesic(true);
        polylineOptions.addAll(route);
        mMap.addPolyline(polylineOptions);


    }
}
