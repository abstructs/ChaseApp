package com.chase.chaseapp.point;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chase.chaseapp.R;
import com.chase.chaseapp.point.PointActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

import entities.Point;

public class PointListAdapter extends BaseAdapter {

    private ArrayList<Point> points;
    private Context context;

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

        final Point point = points.get(position);

        TextView title = view.findViewById(R.id.titleText);
        TextView address = view.findViewById(R.id.addressText);
        TextView tag = view.findViewById(R.id.tagText);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);

        title.setText(point.getTitle());
        address.setText(point.getAddress());
        tag.setText(point.getTag());
        ratingBar.setRating(point.getRating());

        ConstraintLayout pointLayout = view.findViewById(R.id.pointLayout);

        pointLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), PointActivity.class);

                intent.putExtra("point", point);

                context.startActivity(intent);
            }
        });

        return view;
    }
}
