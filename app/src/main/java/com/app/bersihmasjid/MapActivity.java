package com.app.bersihmasjid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.bersihmasjid.dashboard.Dashboard;
import com.app.bersihmasjid.databinding.ActivityMapBinding;
import com.app.bersihmasjid.model.ModelDiary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
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
    private IMapController mapController;
    private SimpleLocationOverlay mMyLocationOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    ItemizedIconOverlay<OverlayItem> currentLocationOverlay;
    SharedPreferences preferences;

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



    //DefaultResourceProxyImpl resourceProxy;



    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        //TODO check permissions

        auth = FirebaseAuth.getInstance();
        uniqueauth = auth.getUid();
        preferences = getSharedPreferences( "uisumbar",MODE_PRIVATE);
        editor = preferences.edit();
        unique = preferences.getString("unique", "");
        Latd = preferences.getString("Latd", "");
        Lond = preferences.getString("Lond", "");
        desc = preferences.getString("desc", "Pusat Pemko Padang");
        //setContentView(R.layout.activity_map);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //mapView = findViewById(R.id.mapview);
        mapView = binding.mapview;

     /*   Log.d("UNIQUELAT",unique);
        Log.d("UNIQUEID",uniqueauth);
        Log.d("LATD",Latd);
        Log.d("LOND",Lond);*/

        mapView.setTileSource(TileSourceFactory.WIKIMEDIA);
        mapView.setMultiTouchControls(true);
        mapController = this.mapView.getController();
        mapController.setZoom(17);
        this.mMyLocationOverlay = new SimpleLocationOverlay(this);
        //this.mapView.getOverlays().add(mMyLocationOverlay);

        //this.mScaleBarOverlay = new ScaleBarOverlay(this);
        //this.mapView.getOverlays().add(mScaleBarOverlay);





        //resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
        /*GeoPoint currentLocation = new GeoPoint
                ( Double.parseDouble(binding.lat.getText().toString()) ,
                        Double.parseDouble(binding.lon.getText().toString()), 10);*/

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

        GeoPoint currentLocation = new GeoPoint( Double.parseDouble(Latdd) , Double.parseDouble(Londd), 10);


        Marker startMarker = new Marker(mapView);
        startMarker.setIcon(getResources().getDrawable(R.drawable.marker_kml_point));

        //String  desc = "Mushala Pemuda KNPI Sumbar";
        //String  addr1 = "Jln. Batang Antokan No. 1 GOR H. Agus Salim";
        //String  addr2 = "Kel. Rimbo Kaluang, Kec. Pdg Barat, Padang";
        String url = "https://www.google.com/maps/search/?api=1&query="+Latdd+","+Londd;

        //startMarker.setTitle(desc +"\n" +addr1 +"\n" +addr2 +"\nTitik GPS: \n" + currentLocation);
        MarkerInfoWindow infoWindow = new MarkerInfoWindow(org.osmdroid.library.R.layout.bonuspack_bubble,mapView);

        startMarker.setTitle("Detail Lokasi: \n"+desc+ "\n Titik GPS: \n" + currentLocation);
        startMarker.setPosition(currentLocation);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        startMarker.setImage(getResources().getDrawable(R.drawable.mushala));
        startMarker.setInfoWindow(infoWindow);
        startMarker.showInfoWindow();
        mapView.getOverlays().add(startMarker);
        mapController.animateTo(currentLocation);

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MapActivity.this, Dashboard.class ));
                finish();
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