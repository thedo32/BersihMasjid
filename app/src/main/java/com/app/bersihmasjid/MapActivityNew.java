package com.app.bersihmasjid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.app.bersihmasjid.databinding.ActivityMapBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.SimpleLocationOverlay;
import org.osmdroid.views.overlay.Polygon;



import java.util.ArrayList;
import java.util.Locale;

/**
 * Bare bones osmdroid example
 * created on 2/17/2018.
 *
 * @author Alex O'Ree
 */

public class MapActivityNew extends AppCompatActivity {
    private MapView mapView = null;
    private final FloatingActionButton logOut = null;
    private ScaleBarOverlay mScaleBarOverlay;
    ItemizedIconOverlay<OverlayItem> currentLocationOverlay;
    SharedPreferences preferences;
    DatabaseReference referenceui;
    String unique;
    String uniqueauth;
    FirebaseAuth auth;

    String Latd;
    String Lond;
    String desc;

    String Latdd;
    String Londd;
    ActivityMapBinding binding;
    SharedPreferences.Editor editor;
    String imagepath;

    RadiusMarkerClusterer radiusMarkerClusterer;
    StaticCluster staticCluster;
    MarkerClusterer markerClusterer;

    Polygon polygon;




    //DefaultResourceProxyImpl resourceProxy;



    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));


        auth = FirebaseAuth.getInstance();
        uniqueauth = auth.getUid();
        preferences = getSharedPreferences( "uisumbar",MODE_PRIVATE);
        editor = preferences.edit();
        unique = preferences.getString("unique", "");
        Latd = preferences.getString("Latd", "");
        Lond = preferences.getString("Lond", "");
        desc = preferences.getString("desc", "Mushala Pemuda KNPI Sumbar");
        FirebaseApp.initializeApp(MapActivityNew.this);

        referenceui = FirebaseDatabase.getInstance().getReference().child("images");
        //referencepadang = FirebaseDatabase.getInstance().getReference().child("logopadang");
        //setContentView(R.layout.activity_map); - use bindings
        //mapView = findViewById(R.id.mapview); - use bindings
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mapView = binding.mapview;

        radiusMarkerClusterer = new RadiusMarkerClusterer(MapActivityNew.this);

        markerClusterer = new MarkerClusterer() {
            @Override
            public ArrayList<StaticCluster> clusterer(MapView mapView) {
                return null;
            }

            @Override
            public Marker buildClusterMarker(StaticCluster cluster, MapView mapView) {
                return null;
            }

            @Override
            public void renderer(ArrayList<StaticCluster> clusters, Canvas canvas, MapView mapView) {

            }
        };




        referenceui.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (Lond.equals("100.3993412")) {
                    imagepath = "albina";
                }else{
                    imagepath = "logo";
                }

                String link = dataSnapshot.child(imagepath).getValue(
                        String.class);

                Picasso.get().load(link).into(binding.appCompatImageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        IMapController mapController = this.mapView.getController();
        SimpleLocationOverlay mMyLocationOverlay = new SimpleLocationOverlay(MapActivityNew.this);
        //this.mapView.getOverlays().add(mMyLocationOverlay);

        this.mScaleBarOverlay = new ScaleBarOverlay(mapView);
        this.mapView.getOverlays().add(mScaleBarOverlay);

      /*  resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
        MarkerInfoWindow infoWindow = new MarkerInfoWindow
        (org.osmdroid.library.R.layout.bonuspack_bubble,mapView);*/

        Marker appMarker = new Marker(mapView);
        Marker homeMarker = new Marker(mapView);
        Marker testMarker = new Marker(mapView);

        if (!Latd.equals("")){
            Latdd = Latd;
        }else{
            Latdd ="-0.9294895";
            desc = "Mushala Pemuda KNPI Sumbar";
        }

        if (!Lond.equals("")){
            Londd = Lond;
        }else{
            Londd = "100.359956";
            desc = "Mushala Pemuda KNPI Sumbar";
        }
        String url = "https://www.google.com/maps/search/?api=1&query="+Latdd+","+Londd;


        GeoPoint currentLocation = new GeoPoint( Double.parseDouble(Latdd) , Double.parseDouble(Londd), 0);
        GeoPoint home = new GeoPoint(-0.8917953507984866, 100.35467135562939,0);
        GeoPoint test = new GeoPoint(-0.8917951510180687, 100.35521047357388,0);

        staticCluster = new StaticCluster(home);

        homeMarker.setIcon(getResources().getDrawable(R.drawable.home));
        homeMarker.setTitle("Lokasi Rumah: Rumah"+ "\n\n Titik GPS: \n" + home);
        homeMarker.setPosition(home);
        homeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        testMarker.setIcon(getResources().getDrawable(R.drawable.email));
        testMarker.setTitle("10 Meter dari Lokasi Rumah: Rumah"+ "\n\n Titik GPS: \n" + test);
        testMarker.setPosition(test);
        testMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);



        appMarker.setIcon(getResources().getDrawable(R.drawable.home));
        appMarker.setTitle("Detail Lokasi: \n"+desc+ "\n\n Titik GPS: \n" + currentLocation);
        appMarker.setPosition(currentLocation);
        appMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        //startMarker.setInfoWindow(infoWindow);
        appMarker.showInfoWindow();
        testMarker.showInfoWindow();
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getOverlays().add(appMarker);
        mapView.getOverlays().add(homeMarker);
        mapView.getOverlays().add(testMarker);
        mapController.setZoom(18.5);
        mapController.animateTo(home);
        mScaleBarOverlay.drawLatitudeScale(true);
        mScaleBarOverlay.drawLongitudeScale(true);
        staticCluster.setMarker(homeMarker);
        staticCluster.mMarker.showInfoWindow();
        staticCluster.getBoundingBox();

        radiusMarkerClusterer.setMaxClusteringZoomLevel(5);
        radiusMarkerClusterer.setRadius(10);

        Polygon oPolygon = new Polygon();
        final double radius = 50;
        ArrayList<GeoPoint> circlePoints = new ArrayList<GeoPoint>();
        for (float f = 0; f < 360; f += 1){
            circlePoints.add(new GeoPoint(-0.891793 , 100.354672).destinationPoint(radius, f));
        }
        oPolygon.setPoints(circlePoints);
        mapView.getOverlays().add(oPolygon);

        Deg2UTM deg2UTMHome = new Deg2UTM(home.getLatitude(),home.getLongitude());
        Deg2UTM deg2UTMTest = new Deg2UTM(test.getLatitude(),test.getLongitude());

        double deHOME = deg2UTMHome.Easting;
        double dnHOME = deg2UTMHome.Northing;
        double deTEST = deg2UTMTest.Easting;
        double dnTEST = deg2UTMTest.Northing;

      if  (Math.abs(deHOME-deTEST) > 50 ) {
          Log.d("ABSENSI", "Anda belum berada di sekitar lokasi ");
          return;
      }else if (Math.abs(dnHOME - dnTEST) > 50) {
          Log.d("ABSENSI", "Anda belum berada di sekitar lokasi ");
      } else {
          Log.d("ABSENSI", "Anda sudah berada di sekitar lokasi " );
      }



        binding.maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


    }



    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onPause();
        }



    }

    private class Deg2UTM
    {
        double Easting;
        double Northing;
        int Zone;
        char Letter;
        private  Deg2UTM(double Lat,double Lon)
        {
            Zone= (int) Math.floor(Lon/6+31);
            if (Lat<-72)
                Letter='C';
            else if (Lat<-64)
                Letter='D';
            else if (Lat<-56)
                Letter='E';
            else if (Lat<-48)
                Letter='F';
            else if (Lat<-40)
                Letter='G';
            else if (Lat<-32)
                Letter='H';
            else if (Lat<-24)
                Letter='J';
            else if (Lat<-16)
                Letter='K';
            else if (Lat<-8)
                Letter='L';
            else if (Lat<0)
                Letter='M';
            else if (Lat<8)
                Letter='N';
            else if (Lat<16)
                Letter='P';
            else if (Lat<24)
                Letter='Q';
            else if (Lat<32)
                Letter='R';
            else if (Lat<40)
                Letter='S';
            else if (Lat<48)
                Letter='T';
            else if (Lat<56)
                Letter='U';
            else if (Lat<64)
                Letter='V';
            else if (Lat<72)
                Letter='W';
            else
                Letter='X';
            Easting=0.5*Math.log((1+Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180))/(1-Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180)))*0.9996*6399593.62/Math.pow((1+Math.pow(0.0820944379, 2)*Math.pow(Math.cos(Lat*Math.PI/180), 2)), 0.5)*(1+ Math.pow(0.0820944379,2)/2*Math.pow((0.5*Math.log((1+Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180))/(1-Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180)))),2)*Math.pow(Math.cos(Lat*Math.PI/180),2)/3)+500000;
            Easting=Math.round(Easting*100)*0.01;
            Northing = (Math.atan(Math.tan(Lat*Math.PI/180)/Math.cos((Lon*Math.PI/180-(6*Zone -183)*Math.PI/180)))-Lat*Math.PI/180)*0.9996*6399593.625/Math.sqrt(1+0.006739496742*Math.pow(Math.cos(Lat*Math.PI/180),2))*(1+0.006739496742/2*Math.pow(0.5*Math.log((1+Math.cos(Lat*Math.PI/180)*Math.sin((Lon*Math.PI/180-(6*Zone -183)*Math.PI/180)))/(1-Math.cos(Lat*Math.PI/180)*Math.sin((Lon*Math.PI/180-(6*Zone -183)*Math.PI/180)))),2)*Math.pow(Math.cos(Lat*Math.PI/180),2))+0.9996*6399593.625*(Lat*Math.PI/180-0.005054622556*(Lat*Math.PI/180+Math.sin(2*Lat*Math.PI/180)/2)+4.258201531e-05*(3*(Lat*Math.PI/180+Math.sin(2*Lat*Math.PI/180)/2)+Math.sin(2*Lat*Math.PI/180)*Math.pow(Math.cos(Lat*Math.PI/180),2))/4-1.674057895e-07*(5*(3*(Lat*Math.PI/180+Math.sin(2*Lat*Math.PI/180)/2)+Math.sin(2*Lat*Math.PI/180)*Math.pow(Math.cos(Lat*Math.PI/180),2))/4+Math.sin(2*Lat*Math.PI/180)*Math.pow(Math.cos(Lat*Math.PI/180),2)*Math.pow(Math.cos(Lat*Math.PI/180),2))/3);
            if (Letter<='M')
                Northing = Northing + 10000000;
            Northing=Math.round(Northing*100)*0.01;
        }
    }

    private class UTM2Deg
    {
        double latitude;
        double longitude;
        private  UTM2Deg(String UTM)
        {
            String[] parts=UTM.split(" ");
            int Zone=Integer.parseInt(parts[0]);
            char Letter=parts[1].toUpperCase(Locale.ENGLISH).charAt(0);
            double Easting=Double.parseDouble(parts[2]);
            double Northing=Double.parseDouble(parts[3]);
            double Hem;
            if (Letter>'M')
                Hem='N';
            else
                Hem='S';
            double north;
            if (Hem == 'S')
                north = Northing - 10000000;
            else
                north = Northing;
            latitude = (north/6366197.724/0.9996+(1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)-0.006739496742*Math.sin(north/6366197.724/0.9996)*Math.cos(north/6366197.724/0.9996)*(Math.atan(Math.cos(Math.atan(( Math.exp((Easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*( 1 -  0.006739496742*Math.pow((Easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996 )/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996)))*Math.tan((north-0.9996*6399593.625*(north/6366197.724/0.9996 - 0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996 )*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))-north/6366197.724/0.9996)*3/2)*(Math.atan(Math.cos(Math.atan((Math.exp((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996)))*Math.tan((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))-north/6366197.724/0.9996))*180/Math.PI;
            latitude=Math.round(latitude*10000000);
            latitude=latitude/10000000;
            longitude =Math.atan((Math.exp((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*( north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2* north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3)) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))*180/Math.PI+Zone*6-183;
            longitude=Math.round(longitude*10000000);
            longitude=longitude/10000000;
        }
    }

}