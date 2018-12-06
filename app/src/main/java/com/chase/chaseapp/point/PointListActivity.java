package com.chase.chaseapp.point;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chase.chaseapp.R;

import java.util.ArrayList;

import database.AppDatabase;
import entities.Point;

public class PointListActivity extends AppCompatActivity {

    private AppDatabase db;

    private Field searchField;
    private String searchQuery;

    private enum Field {
        name, tag
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);

        db = AppDatabase.getAppDatabase(getApplicationContext());
        searchField = Field.name;
        searchQuery = "";

        setupSearch();

        populatePoints();
    }

    @Override
    protected void onResume() {
        super.onResume();

        populatePoints();
    }

    private void setSearchQuery() {
        EditText searchInput = findViewById(R.id.searchInput);
        searchQuery = searchInput.getText().toString();
    }

    private void setSearchField(String field) {
        try {
            searchField = Field.valueOf(field);
        } catch(IllegalArgumentException e) {
            searchField = Field.name;
        } finally {
            populatePointsWithFilter();
        }
    }

    private void setupSearch() {
        EditText searchInput = findViewById(R.id.searchInput);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadio = findViewById(checkedId);
                String field = selectedRadio.getText().toString().toLowerCase();

                setSearchField(field);
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setSearchQuery();

                populatePointsWithFilter();
            }
        });
    }

    private void populatePointsWithFilter() {
        class GetPoints extends AsyncTask<Void, Void, ArrayList<Point>> {
            @Override
            protected ArrayList<Point> doInBackground(Void... params) {
                ArrayList<Point> points = new ArrayList<>();
                switch(searchField) {
                    case name:
                        points.addAll(db.pointDao().searchByTitle(searchQuery + "%"));
                        break;
                    case tag:
                        points.addAll(db.pointDao().searchByTag(searchQuery + "%"));
                        break;
                    default:
                        points.addAll(db.pointDao().getAll());
                        break;
                }

                return  points;
            }

            @Override
            protected void onPostExecute(ArrayList<Point> points) {
                PointListAdapter pointListAdapter = new PointListAdapter(PointListActivity.this, points);

                ListView pointList = findViewById(R.id.pointList);
                pointList.setAdapter(pointListAdapter);
            }
        }

        new GetPoints().execute();
    }

    private void populatePoints() {
        class GetPoints extends AsyncTask<Void, Void, ArrayList<Point>> {
            @Override
            protected ArrayList<Point> doInBackground(Void... params) {
                return new ArrayList<>(db.pointDao().getAll());
            }

            @Override
            protected void onPostExecute(ArrayList<Point> points) {
                PointListAdapter pointListAdapter = new PointListAdapter(PointListActivity.this, points);

                ListView pointList = findViewById(R.id.pointList);
                pointList.setAdapter(pointListAdapter);
            }
        }

        new GetPoints().execute();
    }
}
