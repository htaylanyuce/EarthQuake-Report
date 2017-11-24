package com.rapuncat.earthquakereport;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    Bundle b;

    private String location;
    private double mLatitude;
    private double mLongitude;
    private int mSelectedStyleId = R.string.style_label_default;


    private GoogleMap mMap;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

         b = getIntent().getExtras();
         location = b.getString("location");
         mLatitude =  b.getDouble("latitude");
         mLongitude = b.getDouble("longitude");

         SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();

        uiSettings.setCompassEnabled(false);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);

         LatLng loc = new LatLng(mLatitude, mLongitude);
        mMap.addMarker(new MarkerOptions().position(loc).title(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.styled_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MapStyleOptions style;

        switch (item.getItemId()) {
            case R.id.style_default:
                style = null;
                mMap.setMapStyle(style);
                return true;

            case R.id.style_retro:
                style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_retro);
                mMap.setMapStyle(style);
                return true;



            case R.id.style_night:
                style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_night);
                mMap.setMapStyle(style);
                return true;

            case R.id.style_grayscale:
                style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_grayscale);
                mMap.setMapStyle(style);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
