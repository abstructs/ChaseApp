package com.chase.chaseapp.point;

import com.chase.chaseapp.R;
import com.google.android.gms.maps.model.LatLng;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import database.AppDatabase;
import entities.Point;

public class AddPointActivity extends AppCompatActivity {

    private AppDatabase db;
    private int maxAddressResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);

        db = AppDatabase.getAppDatabase(getApplicationContext());
        maxAddressResults = 5;

        setupSaveBtn();
    }

    private void setupSaveBtn() {
        Button saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(formIsValid())
                        savePointThenFinish(getPoint());
                    else
                        showErrorToast();
                } catch(IOException e) {
                    showErrorToast("We couldn't find that location.");
                }
            }
        });
    }

    // TODO: potentially let the user choose which location they want
    private LatLng getLocationFromAddress(String strAddress) throws IOException {
        Geocoder geocoder = new Geocoder(AddPointActivity.this);
        List<Address> addresses = geocoder.getFromLocationName(strAddress, maxAddressResults);

        if(addresses.size() == 0) {
            throw new IOException();
        }

        Address location = addresses.get(0);

        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    private Point getPoint() throws IOException {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText addressInput = findViewById(R.id.addressInput);
        Spinner tagSpinner = findViewById(R.id.tagSpinner);

        String name = nameInput.getText().toString();
        String address = addressInput.getText().toString();
        String tag = tagSpinner.getSelectedItem().toString();
        LatLng latLng = getLocationFromAddress(address);

        Point point = new Point();

        point.setTitle(name);
        point.setRating(0);
        point.setAddress(address);
        point.setTag(tag);
        point.setLatitude(latLng.latitude);
        point.setLongitude(latLng.longitude);

        return point;
    }

    private void showErrorToast() {
        Toast.makeText(getApplicationContext(), "Please fill out the fields.",
                Toast.LENGTH_LONG).show();
    }

    private void showErrorToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_LONG).show();
    }

    private void showSuccessToast() {
        Toast.makeText(getApplicationContext(), "Point has been added.",
                Toast.LENGTH_LONG).show();
    }

    private void savePointThenFinish(final Point point) {
        class InsertPoint extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... params) {
                db.pointDao().insertOne(point);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean res) {
                showSuccessToast();
                finish();
            }
        }

        new InsertPoint().execute();
    }

    private boolean formIsValid() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText addressInput = findViewById(R.id.addressInput);
        Spinner tagSpinner = findViewById(R.id.tagSpinner);

        return tagSpinner.getSelectedItemPosition() != 0 && nameInput.getText().length() != 0 &&
                addressInput.getText().length() != 0;
    }
}
