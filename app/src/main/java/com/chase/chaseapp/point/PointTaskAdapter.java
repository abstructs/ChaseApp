package com.chase.chaseapp.point;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.chase.chaseapp.R;
import com.chase.chaseapp.helper.HelperUtility;
import com.chase.chaseapp.task.EditTaskActivity;

import java.util.ArrayList;

import database.AppDatabase;
import entities.Task;

public class PointTaskAdapter extends BaseAdapter {

    private AppDatabase db;
    private ArrayList<Task> tasks;
    private Context context;
    private HelperUtility helperUtility;

    PointTaskAdapter(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        this.db = AppDatabase.getAppDatabase(context);
        this.helperUtility = new HelperUtility(context);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void populateFields(View view, Task task) {
        TextView titleText = view.findViewById(R.id.titleText);
        TextView descriptionText = view.findViewById(R.id.descriptionText);
        CheckBox achievedCheckBox = view.findViewById(R.id.achieved_id);

        titleText.setText(task.getTitle());
        descriptionText.setText(task.getDescription());
        achievedCheckBox.setChecked(task.getAchieved());
    }

    private void setupEditFab(View view, final Task task) {
        FloatingActionButton editFab = view.findViewById(R.id.editFab);

        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), EditTaskActivity.class);

                intent.putExtra("task", task);

                context.startActivity(intent);
            }
        });
    }

    private void deleteTask(final Task task) {
        class DeleteTask extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... voids) {
                db.taskDao().deleteOne(task);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(context instanceof PointActivity) {
                    ((PointActivity) context).populateTasks();
                    helperUtility.showToast("Task deleted");
                }
            }
        }

        new DeleteTask().execute();
    }

    private void setupDelete(View view, final Task task) {
        FloatingActionButton deleteFab = view.findViewById(R.id.deleteFab);

        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(task);
            }
        });
    }

    private void updateTask(final Task task) {
        class MarkAchievement extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... voids) {
                db.taskDao().updateOne(task);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(task.getAchieved())
                    helperUtility.showToast("Task achieved!");
            }
        }

        new MarkAchievement().execute();
    }

    private void setupAchievement(View view, Task task) {
        CheckBox achievedCheckBox = view.findViewById(R.id.achieved_id);

        TextView titleText = view.findViewById(R.id.titleText);
        TextView descriptionText = view.findViewById(R.id.descriptionText);

        if(achievedCheckBox.isChecked()) {
            task.setAchieved(true);
            titleText.setTextColor(Color.GREEN);
            descriptionText.setTextColor(Color.GREEN);
            titleText.setTypeface(null, Typeface.BOLD);
        } else {
            task.setAchieved(false);
            titleText.setTextColor(Color.BLACK);
            descriptionText.setTextColor(Color.BLACK);
            titleText.setTypeface(null, Typeface.NORMAL);
        }
    }


    public void setupCheckBox(final View view, final Task task) {

        final CheckBox achievedCheckBox = view.findViewById(R.id.achieved_id);

        achievedCheckBox.setChecked(task.getAchieved());

        setupAchievement(view, task);

        achievedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTask(task);
                setupAchievement(view, task);
            }
        });
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false);
        }

        Task task = tasks.get(position);

        populateFields(view, task);
        setupCheckBox(view, task);
        setupEditFab(view, task);
        setupDelete(view, task);

        return view;
    }
}
