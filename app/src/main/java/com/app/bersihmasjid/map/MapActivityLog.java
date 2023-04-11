package com.app.bersihmasjid.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.bersihmasjid.MainActivity;
import com.app.bersihmasjid.MarkerClusterer;
import com.app.bersihmasjid.R;
import com.app.bersihmasjid.Splash;
import com.app.bersihmasjid.StaticCluster;
import com.app.bersihmasjid.databinding.ActivityMapBinding;
import com.app.bersihmasjid.databinding.ActivityMapLoginBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
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

public class MapActivityLog extends AppCompatActivity implements LocationListener{
    private MapView mapView = null;
    private final FloatingActionButton logOut = null;
    MainActivity mainActivity;
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
    ActivityMapLoginBinding binding;
    SharedPreferences.Editor editor;
    String imagepath;

    //RadiusMarkerClusterer radiusMarkerClusterer;
    //StaticCluster staticCluster;
    MarkerClusterer markerClusterer;

    Polygon polygon;

    MyLocationNewOverlay locationOverlay;

    Intent intentThatCalled;
    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;

    String voice2text; //added


    //DefaultResourceProxyImpl resourceProxy;


    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));


        auth = FirebaseAuth.getInstance();
        uniqueauth = auth.getUid();
        preferences = getSharedPreferences("uisumbar", MODE_PRIVATE);
        editor = preferences.edit();
        unique = preferences.getString("unique", "");
        Latd = preferences.getString("Latd", "");
        Lond = preferences.getString("Lond", "");
        desc = preferences.getString("desc", "Mushala Pemuda KNPI Sumbar");
        FirebaseApp.initializeApp(MapActivityLog.this);

        //referencepadang = FirebaseDatabase.getInstance().getReference().child("logopadang");
        //setContentView(R.layout.activity_map); - use bindings
        //mapView = findViewById(R.id.mapview); - use bindings
        binding = ActivityMapLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mapView = binding.mapview;
        intentThatCalled = getIntent();
        voice2text = intentThatCalled.getStringExtra("v2txt");
        getLocation();


        //radiusMarkerClusterer = new RadiusMarkerClusterer(MapActivityLog.this);

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

        /* no need to load image for now

        referenceui = FirebaseDatabase.getInstance().getReference().child("images");

        referenceui.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (Lond.equals("100.3993412")) {
                    imagepath = "albina";
                } else {
                    imagepath = "logo";
                }

                String link = dataSnapshot.child(imagepath).getValue(
                        String.class);

                Picasso.get().load(link).into(binding.appCompatImageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
*/

        IMapController mapController = this.mapView.getController();
        SimpleLocationOverlay mMyLocationOverlay = new SimpleLocationOverlay(MapActivityLog.this);
        //this.mapView.getOverlays().add(mMyLocationOverlay);

        this.mScaleBarOverlay = new ScaleBarOverlay(mapView);
        this.mapView.getOverlays().add(mScaleBarOverlay);

      /*  resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
        MarkerInfoWindow infoWindow = new MarkerInfoWindow
        (org.osmdroid.library.R.layout.bonuspack_bubble,mapView);*/



        Marker appMarker = new Marker(mapView);
        Marker homeMarker = new Marker(mapView);
        Marker testMarker = new Marker(mapView);
        Marker mylocMarker = new Marker(mapView);

        if (!Latd.equals("")) {
            Latdd = Latd;
        } else {
            Latdd = "-0.9294895";
            desc = "Mushala Pemuda KNPI Sumbar";
        }

        if (!Lond.equals("")) {
            Londd = Lond;
        } else {
            Londd = "100.359956";
            desc = "Mushala Pemuda KNPI Sumbar";
        }
        String url = "https://www.google.com/maps/search/?api=1&query=" + Latdd + "," + Londd;


        GeoPoint apploc = new GeoPoint(Double.parseDouble(Latdd), Double.parseDouble(Londd), 0);
        GeoPoint home = new GeoPoint(-0.8917953507984866, 100.35467135562939, 0);
        GeoPoint test = new GeoPoint(-0.8917951843203211, 100.35512062059152, 0);
        GeoPoint myloc = new GeoPoint(latitude, longitude, 0);
        Log.d("TEEESSSST","TEST:"+latitude+","+longitude);


        //staticCluster = new StaticCluster(home);


        mapController.setCenter(apploc);
       /* GpsMyLocationProvider provider = new GpsMyLocationProvider (MapActivityLog.this);
        provider.addLocationSource(NETWORK_PROVIDER);
        provider.getLocationSources();
        locationOverlay = new MyLocationNewOverlay(provider, mapView);
        locationOverlay.enableFollowLocation();
        locationOverlay.enableMyLocation();
        locationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                Log.d("MyTag", String.format("First location fix: %s", locationOverlay.getLastFix()));
            }
        });*/

        //mapController.animateTo(locationOverlay.getMyLocation());


        homeMarker.setIcon(getResources().getDrawable(R.drawable.home));
        homeMarker.setTitle("Lokasi Rumah: \n Rumah" + "\n\n Titik GPS: \n" + home);
        homeMarker.setPosition(home);
        homeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        testMarker.setIcon(getResources().getDrawable(R.drawable.marker_kml_point));
        testMarker.setTitle(" Detail Lokasi: " +
                "\n Batas - Radius 50 Meter dari Rumah" + "\n\n Titik GPS: \n" + test);
        testMarker.setPosition(test);
        testMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        mylocMarker.setIcon(getResources().getDrawable(R.drawable.name));
        mylocMarker.setTitle(" Detail Lokasi: " +
                "\n Lokasi sekarang" + "\n\n Titik GPS: \n" + myloc);
        mylocMarker.setPosition(myloc);
        mylocMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);


        appMarker.setIcon(getResources().getDrawable(R.drawable.home));
        appMarker.setTitle("Detail Lokasi: \n" + desc + "\n\n Titik GPS: \n" + apploc);
        appMarker.setPosition(apploc);
        appMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        //startMarker.setInfoWindow(infoWindow);
        appMarker.showInfoWindow();
        testMarker.showInfoWindow();
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getOverlays().add(appMarker);
        mapView.getOverlays().add(homeMarker);
        mapView.getOverlays().add(mylocMarker);
        mapView.getOverlays().add(testMarker);
        mapView.getOverlayManager().add(locationOverlay);
        mapController.setZoom(18.5);
        mapController.animateTo(myloc);

        mScaleBarOverlay.drawLatitudeScale(true);
        mScaleBarOverlay.drawLongitudeScale(true);
        //staticCluster.setMarker(homeMarker);
        //staticCluster.mMarker.showInfoWindow();
        //staticCluster.getBoundingBox();
        //radiusMarkerClusterer.setMaxClusteringZoomLevel(5);
        //radiusMarkerClusterer.setRadius(10);

        Polygon oPolygon = new Polygon();
        final double radius = 50;
        ArrayList<GeoPoint> circlePoints = new ArrayList<GeoPoint>();
        for (float f = 0; f < 360; f += 1) {
            circlePoints.add(new GeoPoint(-0.8917953507984866, 100.35467135562939).destinationPoint(radius, f));
        }
        oPolygon.setPoints(circlePoints);
        mapView.getOverlays().add(oPolygon);

        Deg2UTM deg2UTMHome = new Deg2UTM(home.getLatitude(), home.getLongitude());
        Deg2UTM deg2UTMMyLoc = new Deg2UTM(latitude, longitude);
        //Deg2UTM deg2UTMTest = new Deg2UTM(test.getLatitude(), test.getLongitude());

        double deHOME = deg2UTMHome.Easting;
        double dnHOME = deg2UTMHome.Northing;
        /*double deTEST = deg2UTMTest.Easting;
        double dnTEST = deg2UTMTest.Northing;
*/      final double deMyLoc = deg2UTMMyLoc.Easting;
        final double dnMyLoc = deg2UTMMyLoc.Northing;


        if (Math.abs(deHOME - deMyLoc) >= 51) {
            Toast.makeText(MapActivityLog.this, "Anda belum berada di sekitar lokasi ",
                    Toast.LENGTH_LONG).show();
            Toast.makeText(MapActivityLog.this, "Absensi Anda Masih Gagal",
                    Toast.LENGTH_LONG).show();
        } else if (Math.abs(dnHOME - dnMyLoc) >= 51) {
            Toast.makeText(MapActivityLog.this, "Anda belum berada di sekitar lokasi ",
                    Toast.LENGTH_LONG).show();
            Toast.makeText(MapActivityLog.this, "Absensi Anda Masih Gagal",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MapActivityLog.this, "SELAMAT, Absensi Anda berhasil ",
                    Toast.LENGTH_LONG).show();
            Toast.makeText(MapActivityLog.this, "Anda sudah berada di sekitar lokasi ",
                    Toast.LENGTH_LONG).show();

        }

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapActivityLog.this, Splash.class ));
            }
        });
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
        Configuration.getInstance().load(MapActivityLog.this,
                PreferenceManager.getDefaultSharedPreferences(MapActivityLog.this));
        //add
        //locationOverlay.enableMyLocation();
        if (mapView != null) {
            mapView.onResume();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
       // locationOverlay.disableMyLocation();
        locationManager.removeUpdates(this);
        if (mapView != null) {
            mapView.onPause();
        }


    }

    private class Deg2UTM {
        double Easting;
        double Northing;
        int Zone;
        char Letter;

        private Deg2UTM(double Lat, double Lon) {
            Zone = (int) Math.floor(Lon / 6 + 31);
            if (Lat < -72)
                Letter = 'C';
            else if (Lat < -64)
                Letter = 'D';
            else if (Lat < -56)
                Letter = 'E';
            else if (Lat < -48)
                Letter = 'F';
            else if (Lat < -40)
                Letter = 'G';
            else if (Lat < -32)
                Letter = 'H';
            else if (Lat < -24)
                Letter = 'J';
            else if (Lat < -16)
                Letter = 'K';
            else if (Lat < -8)
                Letter = 'L';
            else if (Lat < 0)
                Letter = 'M';
            else if (Lat < 8)
                Letter = 'N';
            else if (Lat < 16)
                Letter = 'P';
            else if (Lat < 24)
                Letter = 'Q';
            else if (Lat < 32)
                Letter = 'R';
            else if (Lat < 40)
                Letter = 'S';
            else if (Lat < 48)
                Letter = 'T';
            else if (Lat < 56)
                Letter = 'U';
            else if (Lat < 64)
                Letter = 'V';
            else if (Lat < 72)
                Letter = 'W';
            else
                Letter = 'X';
            Easting = 0.5 * Math.log((1 + Math.cos(Lat * Math.PI / 180) * Math.sin(Lon * Math.PI / 180 - (6 * Zone - 183) * Math.PI / 180)) / (1 - Math.cos(Lat * Math.PI / 180) * Math.sin(Lon * Math.PI / 180 - (6 * Zone - 183) * Math.PI / 180))) * 0.9996 * 6399593.62 / Math.pow((1 + Math.pow(0.0820944379, 2) * Math.pow(Math.cos(Lat * Math.PI / 180), 2)), 0.5) * (1 + Math.pow(0.0820944379, 2) / 2 * Math.pow((0.5 * Math.log((1 + Math.cos(Lat * Math.PI / 180) * Math.sin(Lon * Math.PI / 180 - (6 * Zone - 183) * Math.PI / 180)) / (1 - Math.cos(Lat * Math.PI / 180) * Math.sin(Lon * Math.PI / 180 - (6 * Zone - 183) * Math.PI / 180)))), 2) * Math.pow(Math.cos(Lat * Math.PI / 180), 2) / 3) + 500000;
            Easting = Math.round(Easting * 100) * 0.01;
            Northing = (Math.atan(Math.tan(Lat * Math.PI / 180) / Math.cos((Lon * Math.PI / 180 - (6 * Zone - 183) * Math.PI / 180))) - Lat * Math.PI / 180) * 0.9996 * 6399593.625 / Math.sqrt(1 + 0.006739496742 * Math.pow(Math.cos(Lat * Math.PI / 180), 2)) * (1 + 0.006739496742 / 2 * Math.pow(0.5 * Math.log((1 + Math.cos(Lat * Math.PI / 180) * Math.sin((Lon * Math.PI / 180 - (6 * Zone - 183) * Math.PI / 180))) / (1 - Math.cos(Lat * Math.PI / 180) * Math.sin((Lon * Math.PI / 180 - (6 * Zone - 183) * Math.PI / 180)))), 2) * Math.pow(Math.cos(Lat * Math.PI / 180), 2)) + 0.9996 * 6399593.625 * (Lat * Math.PI / 180 - 0.005054622556 * (Lat * Math.PI / 180 + Math.sin(2 * Lat * Math.PI / 180) / 2) + 4.258201531e-05 * (3 * (Lat * Math.PI / 180 + Math.sin(2 * Lat * Math.PI / 180) / 2) + Math.sin(2 * Lat * Math.PI / 180) * Math.pow(Math.cos(Lat * Math.PI / 180), 2)) / 4 - 1.674057895e-07 * (5 * (3 * (Lat * Math.PI / 180 + Math.sin(2 * Lat * Math.PI / 180) / 2) + Math.sin(2 * Lat * Math.PI / 180) * Math.pow(Math.cos(Lat * Math.PI / 180), 2)) / 4 + Math.sin(2 * Lat * Math.PI / 180) * Math.pow(Math.cos(Lat * Math.PI / 180), 2) * Math.pow(Math.cos(Lat * Math.PI / 180), 2)) / 3);
            if (Letter <= 'M')
                Northing = Northing + 10000000;
            Northing = Math.round(Northing * 100) * 0.01;
        }
    }

    private class UTM2Deg {
        double latitude;
        double longitude;

        private UTM2Deg(String UTM) {
            String[] parts = UTM.split(" ");
            int Zone = Integer.parseInt(parts[0]);
            char Letter = parts[1].toUpperCase(Locale.ENGLISH).charAt(0);
            double Easting = Double.parseDouble(parts[2]);
            double Northing = Double.parseDouble(parts[3]);
            double Hem;
            if (Letter > 'M')
                Hem = 'N';
            else
                Hem = 'S';
            double north;
            if (Hem == 'S')
                north = Northing - 10000000;
            else
                north = Northing;
            latitude = (north / 6366197.724 / 0.9996 + (1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2) - 0.006739496742 * Math.sin(north / 6366197.724 / 0.9996) * Math.cos(north / 6366197.724 / 0.9996) * (Math.atan(Math.cos(Math.atan((Math.exp((Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))) * (1 - 0.006739496742 * Math.pow((Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))), 2) / 2 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2) / 3)) - Math.exp(-(Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))) * (1 - 0.006739496742 * Math.pow((Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))), 2) / 2 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2) / 3))) / 2 / Math.cos((north - 0.9996 * 6399593.625 * (north / 6366197.724 / 0.9996 - 0.006739496742 * 3 / 4 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.pow(0.006739496742 * 3 / 4, 2) * 5 / 3 * (3 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 4 - Math.pow(0.006739496742 * 3 / 4, 3) * 35 / 27 * (5 * (3 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 4 + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 3)) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))) * (1 - 0.006739496742 * Math.pow((Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))), 2) / 2 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) + north / 6366197.724 / 0.9996))) * Math.tan((north - 0.9996 * 6399593.625 * (north / 6366197.724 / 0.9996 - 0.006739496742 * 3 / 4 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.pow(0.006739496742 * 3 / 4, 2) * 5 / 3 * (3 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 4 - Math.pow(0.006739496742 * 3 / 4, 3) * 35 / 27 * (5 * (3 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 4 + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 3)) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))) * (1 - 0.006739496742 * Math.pow((Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))), 2) / 2 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) + north / 6366197.724 / 0.9996)) - north / 6366197.724 / 0.9996) * 3 / 2) * (Math.atan(Math.cos(Math.atan((Math.exp((Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))) * (1 - 0.006739496742 * Math.pow((Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))), 2) / 2 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2) / 3)) - Math.exp(-(Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))) * (1 - 0.006739496742 * Math.pow((Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))), 2) / 2 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2) / 3))) / 2 / Math.cos((north - 0.9996 * 6399593.625 * (north / 6366197.724 / 0.9996 - 0.006739496742 * 3 / 4 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.pow(0.006739496742 * 3 / 4, 2) * 5 / 3 * (3 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 4 - Math.pow(0.006739496742 * 3 / 4, 3) * 35 / 27 * (5 * (3 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 4 + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 3)) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))) * (1 - 0.006739496742 * Math.pow((Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))), 2) / 2 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) + north / 6366197.724 / 0.9996))) * Math.tan((north - 0.9996 * 6399593.625 * (north / 6366197.724 / 0.9996 - 0.006739496742 * 3 / 4 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.pow(0.006739496742 * 3 / 4, 2) * 5 / 3 * (3 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 4 - Math.pow(0.006739496742 * 3 / 4, 3) * 35 / 27 * (5 * (3 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 4 + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 3)) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))) * (1 - 0.006739496742 * Math.pow((Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))), 2) / 2 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) + north / 6366197.724 / 0.9996)) - north / 6366197.724 / 0.9996)) * 180 / Math.PI;
            latitude = Math.round(latitude * 10000000);
            latitude = latitude / 10000000;
            longitude = Math.atan((Math.exp((Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))) * (1 - 0.006739496742 * Math.pow((Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))), 2) / 2 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2) / 3)) - Math.exp(-(Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))) * (1 - 0.006739496742 * Math.pow((Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))), 2) / 2 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2) / 3))) / 2 / Math.cos((north - 0.9996 * 6399593.625 * (north / 6366197.724 / 0.9996 - 0.006739496742 * 3 / 4 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.pow(0.006739496742 * 3 / 4, 2) * 5 / 3 * (3 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 4 - Math.pow(0.006739496742 * 3 / 4, 3) * 35 / 27 * (5 * (3 * (north / 6366197.724 / 0.9996 + Math.sin(2 * north / 6366197.724 / 0.9996) / 2) + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 4 + Math.sin(2 * north / 6366197.724 / 0.9996) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2) * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) / 3)) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))) * (1 - 0.006739496742 * Math.pow((Easting - 500000) / (0.9996 * 6399593.625 / Math.sqrt((1 + 0.006739496742 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)))), 2) / 2 * Math.pow(Math.cos(north / 6366197.724 / 0.9996), 2)) + north / 6366197.724 / 0.9996)) * 180 / Math.PI + Zone * 6 - 183;
            longitude = Math.round(longitude * 10000000);
            longitude = longitude / 10000000;
        }
    }
    public static boolean isLocationEnabled(Context context) {
        //...............
        return true;
    }

    protected void getLocation() {
        if (isLocationEnabled(this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //You can still do this if you like, you might get lucky:
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                /*Toast.makeText(MapActivityLog.this,
                        "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();*/
                searchNearestPlace(voice2text);
            } else {
                //This is what you need:
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
        }
        else
        {
            //prompt user to enable location....
            //.................
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        //Hey, a non null location! Sweet!

        //remove location callback:
        locationManager.removeUpdates(this);

        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Toast.makeText(MapActivityLog.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
        searchNearestPlace(voice2text);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void searchNearestPlace(String v2txt) {
        //.....
    }

}