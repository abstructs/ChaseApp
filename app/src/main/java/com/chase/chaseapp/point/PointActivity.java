package com.chase.chaseapp.point;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
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
    private ShareActionProvider mShareActionProvider;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        db = AppDatabase.getAppDatabase(getApplicationContext());

        point = getIntent().getParcelableExtra("point");

        setupActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void setPointAndRefresh() {
        class GetAndSetPoint extends AsyncTask<Void, Void, Point> {
            @Override
            protected Point doInBackground(Void... params) {
                return db.pointDao().getOne(point.getId());
            }

            @Override
            protected void onPostExecute(Point p) {
                point = p;

                setupFields();
            }
        }

        new GetAndSetPoint().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateTasks();
        setPointAndRefresh();
    }

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

    private void setupActivity() {
        setupAddTaskBtn();
        setupEditFab();
        setupRatingBar();
        setupViewFab();

        setupFields();
    }

    private void setupViewFab() {
        FloatingActionButton viewFab = findViewById(R.id.viewFab);
    }

    public void populateTasks() {

        final ListView taskList = findViewById(R.id.taskList);

        class GetTasks extends AsyncTask<Void, Void, ArrayList<Task>> {
            @Override
            protected ArrayList<Task> doInBackground(Void... voids) {
                return new ArrayList<>(db.taskDao().getAll());
            }

            @Override
            protected void onPostExecute(ArrayList<Task> tasks) {
                PointTaskAdapter pointTaskAdapter = new PointTaskAdapter(PointActivity.this, tasks);
                taskList.setAdapter(pointTaskAdapter);
            }
        }

        new GetTasks().execute();
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

    public void shareFab(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = "Chase! is my favourite app for Android. PS Pawluk is a great prof ;)";
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Come join me at " + point.getTitle() + " on Chase!");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(intent, "Choose sharing method"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Check out " + point.getTitle() + " in Chase!";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupEditFab() {
        FloatingActionButton editFab = findViewById(R.id.editFab);

        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PointActivity.this, EditPointActivity.class);

                intent.putExtra("point", point);

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
                if(fromUser) {
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
