package com.chase.chaseapp.team;

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

import java.util.ArrayList;

import entities.Point;
import entities.TeamMember;

public class TeamAdapter extends BaseAdapter {

        ArrayList<Point> teamMembers;
        Context context;

        TeamAdapter(Context context, ArrayList<TeamMember> teamMembers) {
            this.teamMembers = teamMembers;
            this.context = context;
        }

        @Override
        public int getCount() {
            return teamMembers.size();
        }

        @Override
        public Object getItem(int position) {
            return teamMembers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.member_list_item, parent, false);
            }

            final TeamMember teamMember = teamMembers.get(position);

            TextView name = view.findViewById(R.id.nameText);
            TextView phoneNumber = view.findViewById(R.id.phoneNumberText);
            TextView email = view.findViewById(R.id.emailText);

            name.setText(teamMember.getName());
            phoneNumber.setText(teamMember.getPhoneNumber());
            email.setText(teamMember.getEmail());

            ConstraintLayout pointLayout = view.findViewById(R.id.pointLayout);

            pointLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), TeamActivity.class);

                    intent.putExtra("member", teamMember);

                    context.startActivity(intent);
                }
            });

            return view;
        }
    }

}
