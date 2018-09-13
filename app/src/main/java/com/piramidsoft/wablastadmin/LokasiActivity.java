package com.piramidsoft.wablastadmin;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LokasiActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageButton btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        btBack = (ImageButton) findViewById(R.id.btBack);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
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

        String loc = getIntent().getStringExtra("lokasi");
        String nama = getIntent().getStringExtra("nama");

        double lat = -7.373247;
        double lon = 112.649582;
        if (loc.contains(",")) {
            String[] l = loc.split(",");
            lat = Double.parseDouble(l[0].trim());
            lon = Double.parseDouble(l[1].trim());
        }
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng mark = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(mark).title(nama)).showInfoWindow();

//        mMap.addMarker(new MarkerOptions().position(mark).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, 17));
    }
}
