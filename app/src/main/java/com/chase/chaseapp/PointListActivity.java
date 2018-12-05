package com.chase.chaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import entities.Point;

public class PointListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);

        ListView pointList = findViewById(R.id.pointList);

        ArrayList<Point> points = new ArrayList<>();
        Point point = new Point();

        point.setId(1);
        point.setAddress("160 Kendal Ave");
        point.setRating(5);
        point.setTitle("George Brown College");

        points.add(point);
        points.add(point);
        points.add(point);

        PointListAdapter pointListAdapter = new PointListAdapter(PointListActivity.this, points);

        pointList.setAdapter(pointListAdapter);
    }
}
