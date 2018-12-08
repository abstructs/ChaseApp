package com.chase.chaseapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.chase.chaseapp.point.AddPointActivity;

import com.chase.chaseapp.helper.HelperUtility;
import com.chase.chaseapp.team.TeamActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import database.AppDatabase;
import entities.Point;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private HelperUtility helperUtility;
    private AppDatabase db;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(MainActivity.this);

        db = AppDatabase.getAppDatabase(getApplicationContext());
        helperUtility = new HelperUtility(getApplicationContext());
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        setupBtns();
    }

    private void addPointToMap(Point point) {
        if(point != null) {
            LatLng markerLocation = new LatLng(point.getLatitude(), point.getLongitude());
            mMap.addMarker(
                    new MarkerOptions()
                            .position(markerLocation)
                            .title(point.getTitle())
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        populatePointsThenAddToMap();
    }

    private void populatePointsThenAddToMap() {
        class GetPoints extends AsyncTask<Void, Void, ArrayList<Point>> {
            @Override
            protected ArrayList<Point> doInBackground(Void... params) {
                return new ArrayList<>(db.pointDao().getAll());
            }

            @Override
            protected void onPostExecute(ArrayList<Point> allPoints) {
                addPointsToMap(allPoints);
            }
        }

        new GetPoints().execute();
    }

    private void addPointsToMap(Iterable<Point> points) {
        for(Point point : points) {
            addPointToMap(point);
        }
    }

    private void setupBtns() {
        FloatingActionButton addBtn = findViewById(R.id.addBtn);
        FloatingActionButton viewBtn = findViewById(R.id.viewBtn);
        FloatingActionButton teamBtn = findViewById(R.id.teamBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPointActivity.class);
                startActivity(intent);
            }
        });

        teamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TeamActivity.class);
                startActivity(intent);
            }
        });

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TeamActivity.class);
                startActivity(intent);
            }
        });
    }

    private Location getLastKnownLocation() throws SecurityException {
        return mLocationManager.getLastKnownLocation(getBestProvider());
    }

    private String getBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        return mLocationManager.getBestProvider(criteria, true);
    }

    private void moveMapCameraToLocation(Location location) {
        if(location != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15f));
    }


    private boolean grantedLocationPermissions() {
        if(Build.VERSION.SDK_INT >= 23) {
            return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        } else {
            int gpsPermission = PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
            int networkPermission = PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            return gpsPermission == PermissionChecker.PERMISSION_GRANTED || networkPermission == PermissionChecker.PERMISSION_GRANTED;
        }
    }

    private void requestLocationPermissionsIfMissing() {
        if(!grantedLocationPermissions()) {
            if(Build.VERSION.SDK_INT >= 23) {
                requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION },
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION },
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // TODO: get the callback for permissions, as is doesn't get the permission on the first run
        requestLocationPermissionsIfMissing();

        try {
            mMap.setMyLocationEnabled(true);
            populatePointsThenAddToMap();
            moveMapCameraToLocation(getLastKnownLocation());
        } catch(SecurityException e) {
            e.printStackTrace();
            helperUtility.showToast("Location permissions required to show your location.");
        }
    }
}
