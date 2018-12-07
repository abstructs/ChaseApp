package com.chase.chaseapp.team;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.chase.chaseapp.R;
import java.util.ArrayList;
import database.AppDatabase;
import entities.Member;

public class MemberAdapter extends BaseAdapter {

        private ArrayList<Member> members;
        private Context context;
        private AppDatabase db;

        MemberAdapter(Context context, ArrayList<Member> members) {
            this.members = members;
            this.context = context;
        }

        @Override
        public int getCount() {
            return members.size();
        }

        @Override
        public Object getItem(int position) {
            return members.get(position);
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

            final Member member = members.get(position);

            TextView name = view.findViewById(R.id.memberNameText);

            name.setText(member.getName());

            ConstraintLayout memberLayout = view.findViewById(R.id.memberLayout);

            memberLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), MemberActivity.class);
                    intent.putExtra("member", member);
                    context.startActivity(intent);
                }
            });

            return view;
        }


}
