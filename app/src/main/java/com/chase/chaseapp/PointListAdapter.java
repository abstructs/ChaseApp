package com.chase.chaseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import entities.Point;

public class PointListAdapter extends BaseAdapter {

    ArrayList<Point> points;
    Context context;

    PointListAdapter(Context context, ArrayList<Point> points) {
        this.points = points;
        this.context = context;
    }

    @Override
    public int getCount() {
        return points.size();
    }

    @Override
    public Object getItem(int position) {
        return points.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.point_list_item, parent, false);
        }

        TextView title = view.findViewById(R.id.titleText);
        TextView address = view.findViewById(R.id.addressText);

        return view;
    }
}
