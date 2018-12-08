package com.chase.chaseapp.point;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chase.chaseapp.R;
import com.chase.chaseapp.helper.HelperUtility;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import database.AppDatabase;
import entities.Point;


public class PointDetailActivity extends AppCompatActivity {

    private Point point;
    private AppDatabase db;
    private HelperUtility helperUtility;

    private int maxAddressResults;
    private int requestCode;
    private static final int ADD_POINT_REQUEST = 1;
//  private static final int EDIT_POINT_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);

        db = AppDatabase.getAppDatabase(getApplicationContext());
        helperUtility = new HelperUtility(PointDetailActivity.this);
        maxAddressResults = 5;
        requestCode = getIntent().getIntExtra("requestCode", 0);

        if(requestCode == ADD_POINT_REQUEST)
            setupAddActivity();
        else
            setupEditActivity();
    }

    private void setupAddActivity() {
        TextView title = findViewById(R.id.detailTitle);
        title.setText("Add Point");
        setupSaveBtn();
    }

    private void setupEditActivity() {
        point = getIntent().getParcelableExtra("point");
        populateFields();
        setupUpdateBtn();
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
                        helperUtility.showToast("Please fill out the fields.");
                } catch(IOException e) {
                    helperUtility.showToast("We couldn't find that location.");
                }
            }
        });
    }

    private void setupUpdateBtn() {
        Button saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(formIsValid())
                        updatePointThenFinish(setPoint());
                    else
                        helperUtility.showToast("Please fill out the fields.");
                } catch(IOException e) {
                    helperUtility.showToast("We couldn't find that location.");
                }
            }
        });
    }

    private int indexOf(String element, String[] items) {
        int i = 0;
        for(String item : items) {
            if(item.equals(element))
                return i;

            i++;
        }

        return -1;
    }

    private void populateFields() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText addressInput = findViewById(R.id.addressInput);
        Spinner tagSpinner = findViewById(R.id.tagSpinner);

        nameInput.setText(point.getTitle());
        addressInput.setText(point.getAddress());

        String[] tagOptions =  getResources().getStringArray(R.array.tags_array);

        tagSpinner.setSelection(indexOf(point.getTag(), tagOptions));
    }

    // TODO: potentially let the user choose which location they want
    private LatLng getLocationFromAddress(String strAddress) throws IOException {
        Geocoder geocoder = new Geocoder(PointDetailActivity.this);
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

    private Point setPoint() throws IOException {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText addressInput = findViewById(R.id.addressInput);
        Spinner tagSpinner = findViewById(R.id.tagSpinner);

        String name = nameInput.getText().toString();
        String address = addressInput.getText().toString();
        String tag = tagSpinner.getSelectedItem().toString();
        LatLng latLng = getLocationFromAddress(address);

        point.setTitle(name);
        point.setAddress(address);
        point.setTag(tag);
        point.setLatitude(latLng.latitude);
        point.setLongitude(latLng.longitude);

        return point;
    }

    private void savePointThenFinish(final Point point) {
        class InsertPoint extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... params) {
                //crash occurs here
                db.pointDao().insertOne(point);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean res) {
                helperUtility.showToast("Point has been added.");
                Intent intent = new Intent(PointDetailActivity.this, PointActivity.class);
                intent.putExtra("point", point);
                startActivity(intent);
                finish();
            }
        }

        new InsertPoint().execute();
    }

    private void updatePointThenFinish(final Point point) {
        class UpdatePoint extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... params) {
                db.pointDao().updateOne(point);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean res) {
                helperUtility.showToast("Point has been updated.");
                finish();
            }
        }

        new UpdatePoint().execute();
    }

    private boolean formIsValid() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText addressInput = findViewById(R.id.addressInput);
        Spinner tagSpinner = findViewById(R.id.tagSpinner);

        return tagSpinner.getSelectedItemPosition() != 0 && nameInput.getText().length() != 0 &&
                addressInput.getText().length() != 0;
    }
}
