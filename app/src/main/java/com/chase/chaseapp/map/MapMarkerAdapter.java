package com.chase.chaseapp.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import com.chase.chaseapp.R;

import entities.Point;

public class MapMarkerAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public MapMarkerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
//        Button details, directions;
        TextView title, address;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.map_marker_tooltip, null);

        if(marker != null) {
            title = view.findViewById(R.id.titleText);
            address = view.findViewById(R.id.addressText);
//            details = view.findViewById(R.id.btn_details);
//            directions = view.findViewById(R.id.btn_directions);

            Point point = (Point) marker.getTag();

            if (point != null) {
                title.setText(point.getTitle());
                address.setText(point.getAddress());
            }

        }
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
