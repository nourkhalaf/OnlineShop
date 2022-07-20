package com.example.onlineshop;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.onlineshop.Buyers.HomeActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MapNewActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String userLocation;
    private Button confirmLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_new);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        confirmLocation = findViewById(R.id.confirm_location_btn);
        confirmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(userLocation))
                {
                    Toast.makeText(MapNewActivity.this,"Please select your Location!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(MapNewActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
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



        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (ActivityCompat.checkSelfPermission(MapNewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapNewActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mMap.setMyLocationEnabled(true);

                        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {

                            @Override
                            public boolean onMyLocationButtonClick() {
                                if (ActivityCompat.checkSelfPermission(MapNewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapNewActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                    return false;
                                }
                                LocationManager locationManager = (LocationManager)
                                        getSystemService(Context.LOCATION_SERVICE);
                                Criteria criteria = new Criteria();

                                Location location = locationManager.getLastKnownLocation(locationManager
                                        .getBestProvider(criteria, false));

                                if (location!=null){LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                                    MarkerOptions marker = new MarkerOptions().position(latLng);



                                    marker.title("My Location");
                                    mMap.clear();
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng , 15));

                                    mMap.addMarker(marker);

                                    userLocation = latLng.toString();

                                }
                                else
                                {
                                    Toast.makeText(MapNewActivity.this,"Permission was denied",Toast.LENGTH_SHORT).show();
                                }

                                return true;

                            }
                        });

                        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                            @Override
                            public void onMapClick(LatLng latLng) {

                                // Creating a marker
                                MarkerOptions markerOptions = new MarkerOptions();

                                // Setting the position for the marker
                                markerOptions.position(latLng);

                                // Setting the title for the marker.
                                // This will be displayed on taping the marker
                                //  markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                                markerOptions.title("My Location");

                                // Clears the previously touched position
                                mMap.clear();

                                // Animating to the touched position
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

                                // Placing a marker on the touched position
                                mMap.addMarker(markerOptions);
                                userLocation = latLng.toString();
                            }
                        });

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MapNewActivity.this,"Permission"+permissionDeniedResponse.getPermissionName()+""+
                                " was denied",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                })
                .check();


    }
}