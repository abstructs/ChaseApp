package com.chase.chaseapp.point;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chase.chaseapp.R;
import com.chase.chaseapp.task.EditTaskActivity;

import java.util.ArrayList;

import entities.Task;

public class PointTaskAdapter extends BaseAdapter {

    ArrayList<Task> tasks;
    Context context;

    public PointTaskAdapter(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
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

        Task task = tasks.get(position);

        FloatingActionButton editFab = view.findViewById(R.id.editFab);

        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditTaskActivity.class);

                context.startActivity(intent);
            }
        });

        FloatingActionButton deleteFab = view.findViewById(R.id.deleteFab);

        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete and trigger re-render somehow
            }
        });

        titleText.setText(task.getTitle());
        descriptionText.setText(task.getDescription());

        return view;
    }
}
