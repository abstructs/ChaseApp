package com.chase.chaseapp.point;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chase.chaseapp.R;
import com.chase.chaseapp.task.EditTaskActivity;

import java.util.ArrayList;

import database.AppDatabase;
import entities.Task;

public class PointTaskAdapter extends BaseAdapter {

    private AppDatabase db;
    private ArrayList<Task> tasks;
    private Context context;

    PointTaskAdapter(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        db = AppDatabase.getAppDatabase(context);
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

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false);
        }

        TextView titleText = view.findViewById(R.id.titleText);
        TextView descriptionText = view.findViewById(R.id.descriptionText);

        final Task task = tasks.get(position);

        titleText.setText(task.getTitle());
        descriptionText.setText(task.getDescription());

        FloatingActionButton editFab = view.findViewById(R.id.editFab);

        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), EditTaskActivity.class);

                intent.putExtra("task", task);

                context.startActivity(intent);
            }
        });

        FloatingActionButton deleteFab = view.findViewById(R.id.deleteFab);

        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                            Toast.makeText(context, "Task has been deleted.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }

                new DeleteTask().execute();
            }
        });

        return view;
    }
}
