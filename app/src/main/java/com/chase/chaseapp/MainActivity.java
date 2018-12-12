package com.chase.chaseapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;

import com.chase.chaseapp.map.MapMarkerAdapter;

import com.chase.chaseapp.helper.HelperUtility;
import com.chase.chaseapp.point.AddPointActivity;
import com.chase.chaseapp.point.PointActivity;
import com.chase.chaseapp.point.PointListActivity;
import com.chase.chaseapp.team.TeamActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import database.AppDatabase;
import entities.Point;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private HelperUtility helperUtility;
    private AppDatabase db;
    private Point point;

    private boolean intentHasPointExtra;
    private boolean isFabOpen;
    private ImageButton menuFab, listFab, addFab, teamFab, locationFab, directionsFab, infoFab;

    private String locationProvider;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMap();

        db = AppDatabase.getAppDatabase(getApplicationContext());
        helperUtility = new HelperUtility(getApplicationContext());
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(MainActivity.this);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        intentHasPointExtra = getIntent().hasExtra("point");

        clearMarkersFromMap();

        if (intentHasPointExtra) {
            getPointThenAddToMap();
        } else {
            populatePointsThenAddToMap();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isFabOpen)
            menuFab.performClick();
    }

    private void addPointToMap(Point point) {
        if(point != null) {
            LatLng markerLocation = new LatLng(point.getLatitude(), point.getLongitude());
            mMap.addMarker(
                    new MarkerOptions()
                            .position(markerLocation)
                            .title(point.getTitle()))
                    .setTag(point);
        }
    }

    private void getPointThenAddToMap() {
        class GetPoint extends AsyncTask<Void, Void, Point> {
            @Override
            protected Point doInBackground(Void... params) {
                Point point = getIntent().getParcelableExtra("point");
                return db.pointDao().getOne(point.getId());
            }
            @Override
            protected void onPostExecute(Point p) {
                point = p;
                addPointToMap(point);
                moveMapCameraToPoint();
            }
        }
        new GetPoint().execute();
    }

    private void moveMapCameraToPoint() {
        Location location = new Location(locationProvider);
        location.setLatitude(point.getLatitude());
        location.setLongitude(point.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15f));
    }

    private void clearMarkersFromMap() {
        if(mMap != null) {
            mMap.clear();
        }
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

    private void setMapMarkerOnClick() {
        if(intentHasPointExtra) {
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    finish();
                }
            });
        } else {
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Point point = (Point) marker.getTag();
                    Intent intent = new Intent(MainActivity.this, PointActivity.class);
                    intent.putExtra("point", point);
                    startActivity(intent);
                }
            });
        }
    }

    private void addPointsToMap(Iterable<Point> points) {
        for(Point point : points) {
            addPointToMap(point);
        }
    }

    private void openMainFabMenu() {
        isFabOpen = true;

        float degrees = menuFab.getRotation() + 180F;
        menuFab.animate().rotation(degrees).setInterpolator(new AccelerateDecelerateInterpolator());

        locationFab.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
        listFab.animate().translationY(-getResources().getDimension(R.dimen.standard_110));
        teamFab.animate().translationY(-getResources().getDimension(R.dimen.standard_160));
        addFab.animate().translationY(-getResources().getDimension(R.dimen.standard_210));
    }

    private void closeMainFabMenu() {
        isFabOpen = false;

        float degrees = menuFab.getRotation() + 180F;
        menuFab.animate().rotation(degrees).setInterpolator(new AccelerateDecelerateInterpolator());

        locationFab.animate().translationY(0);
        listFab.animate().translationY(0);
        teamFab.animate().translationY(0);
        addFab.animate().translationY(0);
    }

    private void openPointFabMenu() {
        isFabOpen = true;

        float degrees = menuFab.getRotation() + 180F;
        menuFab.animate().rotation(degrees).setInterpolator(new AccelerateDecelerateInterpolator());

        locationFab.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
        directionsFab.animate().translationY(-getResources().getDimension(R.dimen.standard_110));
    }

    private void closePointFabMenu() {
        isFabOpen = false;

        float degrees = menuFab.getRotation() + 180F;
        menuFab.animate().rotation(degrees).setInterpolator(new AccelerateDecelerateInterpolator());

        locationFab.animate().translationY(0);
        directionsFab.animate().translationY(0);
    }

    private void setMainFabMenuOnClick() {
        menuFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFabOpen)
                    openMainFabMenu();
                else
                    closeMainFabMenu();
            }
        });
    }

    private void setPointFabMenuOnClick() {
        menuFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFabOpen)
                    openPointFabMenu();
                else
                    closePointFabMenu();
            }
        });
    }

    private void setupFabMenu() {
        menuFab = findViewById(R.id.menuFab);
        listFab = findViewById(R.id.listFab);
        addFab = findViewById(R.id.addFab);
        teamFab = findViewById(R.id.teamFab);
        locationFab = findViewById(R.id.myLocationFab);
        directionsFab = findViewById(R.id.directionsFab);
        infoFab = findViewById(R.id.infoFab);

        infoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        listFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PointListActivity.class);
                startActivity(intent);
            }
        });

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPointActivity.class);
                startActivity(intent);
            }
        });

        teamFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TeamActivity.class);
                startActivity(intent);
            }
        });

        locationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveMapCameraToLocation(getLastKnownLocation());
            }
        });

        directionsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri directionsUri = Uri.parse("google.navigation:q=" + point.getAddress() + "&mode=w");
                Intent intent = new Intent(Intent.ACTION_VIEW, directionsUri);
                intent.setPackage("com.google.android.apps.maps");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        if(intentHasPointExtra)
            setPointFabMenuOnClick();
        else
            setMainFabMenuOnClick();
    }

    private void setupMap() throws SecurityException {
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(new MapMarkerAdapter(MainActivity.this));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        locationProvider = getBestProvider();

        if(intentHasPointExtra)
            getPointThenAddToMap();
        else
            populatePointsThenAddToMap();

        setupFabMenu();

        setMapMarkerOnClick();

        moveMapCameraToLocation(getLastKnownLocation());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(grantedLocationPermissions()) {
            try {
                setupMap();
            } catch(SecurityException e) {
                e.printStackTrace();
            }
        } else {
            requestLocationPermissions();
        }
    }

    private String getLocationProvider() {
        return locationProvider;
    }

    private Location getLastKnownLocation() throws SecurityException {
        return mLocationManager.getLastKnownLocation(getLocationProvider());
    }

    private String getBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        return mLocationManager.getBestProvider(criteria, true);
    }

    private void moveMapCameraToLocation(Location location) {
        if(location != null)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15f));
    }

    private boolean grantedLocationPermissions() {
        if(Build.VERSION.SDK_INT >= 23) {
            return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else {
            int gpsPermission = PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
            return gpsPermission == PermissionChecker.PERMISSION_GRANTED;
        }
    }

    private void requestLocationPermissions() {
        if(Build.VERSION.SDK_INT >= 23) {
            requestPermissions(getAcceptableProviders(),
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, getAcceptableProviders(),
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private String[] getAcceptableProviders() {
        return new String[]{ Manifest.permission.ACCESS_FINE_LOCATION};
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean anyGranted = false;

        for(String provider : getAcceptableProviders()) {
            int providerIndex = helperUtility.indexOf(provider, permissions);
            if(providerIndex == -1)
                return;
            if(grantResults[providerIndex] == PermissionChecker.PERMISSION_GRANTED) {
                anyGranted = true;
                break;
            }
        }

        switch(requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if(anyGranted)
                    setupMap();
                else
                    helperUtility.showToast("Location permissions required to show your location.");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String s) { }

    @Override
    public void onProviderDisabled(String s) { }

    @Override
    public void onLocationChanged(Location location) { }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) { }
}
