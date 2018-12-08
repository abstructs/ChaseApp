package com.chase.chaseapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.chase.chaseapp.point.AddPointActivity;
import com.chase.chaseapp.point.PointListActivity;
import com.chase.chaseapp.team.TeamActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, PlacesActivity.class);
        startActivity(intent);
        finish();
    }
}
