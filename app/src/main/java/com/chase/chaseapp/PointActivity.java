package com.chase.chaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import entities.Task;

public class PointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        ListView taskList = findViewById(R.id.taskList);

        ArrayList<Task> tasks = new ArrayList<>();

        Task task = new Task();

        task.setId(1);
        task.setTitle("Take a photo");
        task.setDescription("");

        tasks.add(task);
        tasks.add(task);
        tasks.add(task);
        tasks.add(task);

        PointTaskAdapter pointTaskAdapter = new PointTaskAdapter(PointActivity.this, tasks);

        taskList.setAdapter(pointTaskAdapter);
    }
}
