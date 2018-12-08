package com.chase.chaseapp.team;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.chase.chaseapp.R;
import com.chase.chaseapp.team.*;
import java.util.ArrayList;
import database.AppDatabase;
import entities.Member;

public class TeamActivity extends AppCompatActivity {

    private Member member;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        db = AppDatabase.getAppDatabase(getApplicationContext());
        member = getIntent().getParcelableExtra("member");

        setupActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateMembers();
    }

//    private void setupFields() {
//        TextView nameText = findViewById(R.id.memberNameText);
//        nameText.setText(member.getName());
//    }

    private void setupActivity() {
        setupAddMemberBtn();
//        setupFields();
        populateMembers();
    }

    private void populateMembers() {
        class GetMembers extends AsyncTask<Void, Void, ArrayList<Member>> {
            @Override
            protected ArrayList<Member> doInBackground(Void... params) {
                return new ArrayList<>(db.memberDao().getAll());
            }

            @Override
            protected void onPostExecute(ArrayList<Member> members) {
                MemberAdapter memberAdapter = new MemberAdapter(TeamActivity.this, members);

                ListView memberList = findViewById(R.id.memberList);
                memberList.setAdapter(memberAdapter);
            }
        }
        new GetMembers().execute();
    }

    private void setupAddMemberBtn() {
        Button addMemberBtn = findViewById(R.id.addMemberBtn);

        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamActivity.this, AddMemberActivity.class);
                startActivity(intent);
            }
        });
    }
}

