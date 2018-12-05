package com.chase.chaseapp.point;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;

import com.chase.chaseapp.R;
import com.chase.chaseapp.task.AddTaskActivity;

import java.util.ArrayList;

import entities.Task;

public class PointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        FloatingActionButton viewFab = findViewById(R.id.viewFab);
        FloatingActionButton editFab = findViewById(R.id.editFab);
        FloatingActionButton shareFab = findViewById(R.id.shareFab);

        RatingBar ratingBar = findViewById(R.id.ratingBar);

        Button addTaskBtn = findViewById(R.id.addTaskBtn);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                System.out.print("Rated: ");
                System.out.println(rating);
                ratingBar.setRating(rating);
            }
        });

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PointActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PointActivity.this, EditPointActivity.class);
                startActivity(intent);
            }
        });


        ListView taskList = findViewById(R.id.taskList);

        ArrayList<Task> tasks = new ArrayList<>();

        Task task = new Task();

        task.setId(1);
        task.setTitle("Take a photo");
        task.setDescription("Some description");

        tasks.add(task);
        tasks.add(task);
        tasks.add(task);
        tasks.add(task);

        PointTaskAdapter pointTaskAdapter = new PointTaskAdapter(PointActivity.this, tasks);

        taskList.setAdapter(pointTaskAdapter);
    }
}
