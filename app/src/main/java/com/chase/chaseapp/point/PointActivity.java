package com.chase.chaseapp.point;

import android.arch.persistence.room.Update;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chase.chaseapp.R;
import com.chase.chaseapp.task.AddTaskActivity;

import java.util.ArrayList;

import database.AppDatabase;
import entities.Point;
import entities.Task;

public class PointActivity extends AppCompatActivity {

    private Point point;

    private AppDatabase db;

    static final int EDIT_POINT_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        db = AppDatabase.getAppDatabase(getApplicationContext());

        point = getIntent().getParcelableExtra("point");

        setupActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_POINT_REQUEST) {
                point = data.getParcelableExtra("point");
                setupFields();
            }
        }
    }

    //gets point by id from db, sets point variable, calls setupActivity
    private void setPointAndRefresh() {
        class GetAndSetPoint extends AsyncTask<Void, Void, Point> {
            @Override
            protected Point doInBackground(Void... params) {
                return db.pointDao().getOne(point.getId());
            }

            @Override
            protected void onPostExecute(Point p) {
                point = p;

                setupActivity();
            }
        }

        new GetAndSetPoint().execute();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        setPointAndRefresh();
//    }

    private void setupFields() {
        TextView placeText = findViewById(R.id.placeText);
        TextView addressText = findViewById(R.id.addressText);
        TextView tagText = findViewById(R.id.tagText);

        placeText.setText(point.getTitle());
        addressText.setText(point.getAddress());
        tagText.setText(point.getTag());
    }

    private String getRatingText() {
        return point.getRating() == 0 ? "0 people rated this" : "1 person rated this";
    }

    //calls setup functions for buttons and rating bar, fills in text fields with point data, calls populate tasks
    private void setupActivity() {
        setupAddFab();
        setupAddTaskBtn();
        setupEditFab();
        setupRatingBar();
        setupShareFab();
        setupViewFab();

        setupFields();

        populateTasks();
    }

    private void setupViewFab() {
        FloatingActionButton viewFab = findViewById(R.id.viewFab);
    }

    private void setupShareFab() {
        FloatingActionButton shareFab = findViewById(R.id.shareFab);
    }

    //adds default tasks to the point with a pointTaskAdapter
    private void populateTasks() {
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

    private void setupAddTaskBtn() {
        Button addTaskBtn = findViewById(R.id.addTaskBtn);

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PointActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupAddFab() {

    }

    //sets edit button onclick to putExtra(point)
    //starts edit point activity for result
    private void setupEditFab() {
        FloatingActionButton editFab = findViewById(R.id.editFab);

        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PointActivity.this, PointDetailActivity.class);
                intent.putExtra("point", point);
                intent.putExtra("requestCode", EDIT_POINT_REQUEST);
                startActivity(intent);
            }
        });
    }

    private void setupRatingBar() {
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        ratingBar.setRating(point.getRating());

        setupRatingText();

        ratingBar.setOnRatingBarChangeListener(getRatingListener());
    }

    private void setupRatingText() {
        TextView ratingText = findViewById(R.id.ratingText);
        ratingText.setText(getRatingText());
    }

    private RatingBar.OnRatingBarChangeListener getRatingListener() {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, final float rating, boolean fromUser) {
                if (fromUser) {
                    class UpdateRating extends AsyncTask<Void, Void, Boolean> {
                        @Override
                        protected Boolean doInBackground(Void... params) {
                            db.pointDao().updateRating(point.getId(), (int)rating);
                            return true;
                        }

                        @Override
                        protected void onPostExecute(Boolean res) {
                            point.setRating((int) rating);
                            ratingBar.setRating(rating);
                            setupRatingText();
                        }
                    }

                    new UpdateRating().execute();
                }
            }
        };
    }
}
