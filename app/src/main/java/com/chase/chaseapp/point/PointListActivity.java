package com.chase.chaseapp.point;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.chase.chaseapp.R;

import java.util.ArrayList;

import database.AppDatabase;
import entities.Point;

public class PointListActivity extends AppCompatActivity {

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);

        db = AppDatabase.getAppDatabase(getApplicationContext());

        // TODO: search
        // EditText searchInput = findViewById(R.id.searchInput);

        populatePoints();
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
