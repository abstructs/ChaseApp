package com.chase.chaseapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.chase.chaseapp.point.PointDetailActivity;
import com.chase.chaseapp.point.PointListActivity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;

import com.chase.chaseapp.team.TeamActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class PlacesActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private static final int LOCATION_CODE = 1;

    static final int ADD_POINT_REQUEST = 1;

    private boolean isFabOpen;

    ImageButton fab_menu, fab_list, fab_add, fab_team, fab_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        setupFabMenu();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0, 0, this);
    }

    private void showFabMenu() {
        isFabOpen = true;

        fab_location.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
        fab_list.animate().translationY(-getResources().getDimension(R.dimen.standard_110));
        fab_team.animate().translationY(-getResources().getDimension(R.dimen.standard_160));
        fab_add.animate().translationY(-getResources().getDimension(R.dimen.standard_210));
    }

    private void closeFabMenu() {
        isFabOpen = false;

        fab_location.animate().translationY(0);
        fab_list.animate().translationY(0);
        fab_team.animate().translationY(0);
        fab_add.animate().translationY(0);
    }

    private void setupFabMenu() {
        fab_menu = findViewById(R.id.fab_menu);
        fab_list = findViewById(R.id.fab_list);
        fab_add = findViewById(R.id.fab_add);
        fab_team = findViewById(R.id.fab_team);
        fab_location = findViewById(R.id.fab_location);

        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float degrees = fab_menu.getRotation() + 180F;
                fab_menu.animate().rotation(degrees).setInterpolator(new AccelerateDecelerateInterpolator());

                if (!isFabOpen) {
                    showFabMenu();
                } else {
                    closeFabMenu();
                }
            }
        });

        fab_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlacesActivity.this, PointListActivity.class);
                startActivity(intent);
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlacesActivity.this, PointDetailActivity.class);
                intent.putExtra("requestCode", ADD_POINT_REQUEST);
                startActivity(intent);
            }
        });

        fab_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlacesActivity.this, TeamActivity.class);
                startActivity(intent);
            }
        });

        fab_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
//        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }
}
