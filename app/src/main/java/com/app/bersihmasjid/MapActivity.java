package com.app.bersihmasjid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

/**
 * Bare bones osmdroid example
 * created on 2/17/2018.
 *
 * @author Alex O'Ree
 */

public class MapActivity extends AppCompatActivity {
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
        desc = preferences.getString("desc", "Pusat Pemko Padang");
        FirebaseApp.initializeApp(MapActivity.this);

        referenceui = FirebaseDatabase.getInstance().getReference().child("images");
        //referencepadang = FirebaseDatabase.getInstance().getReference().child("logopadang");
        //setContentView(R.layout.activity_map); - use bindings
        //mapView = findViewById(R.id.mapview); - use bindings
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mapView = binding.mapview;




        referenceui.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (desc.equals("Pusat Pemko Padang")){
                    imagepath = "logopadang";
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
        SimpleLocationOverlay mMyLocationOverlay = new SimpleLocationOverlay(MapActivity.this);
        //this.mapView.getOverlays().add(mMyLocationOverlay);

        this.mScaleBarOverlay = new ScaleBarOverlay(mapView);
        this.mapView.getOverlays().add(mScaleBarOverlay);

      /*  resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
        MarkerInfoWindow infoWindow = new MarkerInfoWindow
        (org.osmdroid.library.R.layout.bonuspack_bubble,mapView);*/

       Marker startMarker = new Marker(mapView);

        if (!Latd.equals("")){
            Latdd = Latd;
        }else{
            Latdd ="-0.8758335";
            desc = "Pusat Pemko Padang";
        }

        if (!Lond.equals("")){
            Londd = Lond;
        }else{
            Londd = "100.3874282";
            desc = "Pusat Pemko Padang";
        }

        String url = "https://www.google.com/maps/search/?api=1&query="+Latdd+","+Londd;



       GeoPoint currentLocation = new GeoPoint( Double.parseDouble(Latdd) , Double.parseDouble(Londd), 10);


       startMarker.setIcon(getResources().getDrawable(R.drawable.home));
       startMarker.setTitle("Detail Lokasi: \n"+desc+ "\n\n Titik GPS: \n" + currentLocation);
       startMarker.setPosition(currentLocation);
       startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

       //startMarker.setInfoWindow(infoWindow);
       startMarker.showInfoWindow();
       mapView.setTileSource(TileSourceFactory.MAPNIK);
       mapView.setMultiTouchControls(true);
       mapView.getOverlays().add(startMarker);
       mapController.setZoom(18.5);
       mapController.animateTo(currentLocation);
       mScaleBarOverlay.drawLatitudeScale(true);
       mScaleBarOverlay.drawLongitudeScale(true);


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

}