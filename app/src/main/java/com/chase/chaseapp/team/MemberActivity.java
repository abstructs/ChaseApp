package com.chase.chaseapp.team;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import com.chase.chaseapp.R;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chase.chaseapp.helper.HelperUtility;

import database.AppDatabase;
import entities.Member;

public class MemberActivity extends AppCompatActivity {

    private Member member;
    private AppDatabase db;
    private HelperUtility helperUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        db = AppDatabase.getAppDatabase(getApplicationContext());
        member = getIntent().getParcelableExtra("member");
        helperUtility = new HelperUtility(MemberActivity.this);

        setupBtns();
        setupFields();
        setupSendSms();
        setupSendEmail();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshMember();
    }

    private void setupBtns() {
        setupDeleteBtn();
        setupEditBtn();
        setupSendSms();
    }


    private void setupEditBtn() {
        Button editButton = findViewById(R.id.edit_btn);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberActivity.this, EditMemberActivity.class);
                intent.putExtra("member", member);
                startActivity(intent);
            }
        });
    }

    private void setupDeleteBtn() {
        Button deleteButton = findViewById(R.id.delete_btn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
    }

    private void setupFields() {
        TextView nameText = findViewById(R.id.nameText);
        TextView phoneNumberText = findViewById(R.id.phoneNumberText);
        TextView emailText = findViewById(R.id.emailText);
        ImageView bannerImage = findViewById(R.id.banner_img);
        Resources resources = getApplicationContext().getResources();
        int imageId = resources.getIdentifier("member_banner_" + String.valueOf(member.getImageId()),
                "drawable", getApplicationContext().getPackageName());

        nameText.setText(member.getName());
        phoneNumberText.setText(member.getPhoneNumber());
        emailText.setText(member.getEmail());
        bannerImage.setImageResource(imageId);
    }

    private void setupSendSms() {
        Button smsButton = findViewById(R.id.phoneBtn);
        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:" + member.getPhoneNumber());
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "Hey " + member.getName()
                        + "! \nLet's continue achieving our tasks on Chase™!");
                startActivity(intent);
            }
        });
    }

    private void setupSendEmail() {
        Button emailButton = findViewById(R.id.emailBtn);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("mailto:" + member.getEmail());
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Help me with our Chase™ tasks!");
                intent.putExtra(Intent.EXTRA_TEXT, "Hey " + member.getName()
                        + "! \nLet's continue achieving our tasks on Chase™!");
                startActivity(intent);
            }
        });
    }

    private DialogInterface.OnClickListener getDialogListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                class DeleteMember extends AsyncTask<Void, Void, Boolean> {
                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        db.memberDao().deleteOne(member);
                        return true;
                    }
                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        helperUtility.showToast("Member deleted");
                        finish();
                    }
                }

                new DeleteMember().execute();
            }
        };
    }

    private void showDeleteDialog() {
        AlertDialog.Builder dialogBuilder = helperUtility.buildDialog("Delete Member",
                "Are you sure you wish to delete member?");
        dialogBuilder.setPositiveButton("Confirm", getDialogListener());
        dialogBuilder.create().show();
    }

    private void refreshMember() {
        class GetAndSetMember extends AsyncTask<Void, Void, Member> {
            @Override
            protected Member doInBackground(Void... params) {
                return db.memberDao().getOne(member.getId());
            }

            @Override
            protected void onPostExecute(Member m) {
                member = m;

                setupFields();
            }
        }

        new GetAndSetMember().execute();
    }
}