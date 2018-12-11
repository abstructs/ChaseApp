package com.chase.chaseapp.team;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;

import com.chase.chaseapp.MainActivity;
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

public class MemberActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Member member;
    private AppDatabase db;
    private HelperUtility helperUtility;
    private final int MY_PERMISSIONS_REQUEST_PHONE = 2;

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
        setupPhoneBtn();
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
        setupPhoneBtn();
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


    private boolean grantedPhonePermissions() {
        if(Build.VERSION.SDK_INT >= 23) {
            return checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
        } else {
            int phonePermission = PermissionChecker.checkSelfPermission(MemberActivity.this, Manifest.permission.CALL_PHONE);
            return phonePermission == PermissionChecker.PERMISSION_GRANTED;
        }
    }

    private void requestPhonePermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{ Manifest.permission.CALL_PHONE },
                    MY_PERMISSIONS_REQUEST_PHONE);
        } else {
            ActivityCompat.requestPermissions(MemberActivity.this, new String[]{ Manifest.permission.CALL_PHONE },
                    MY_PERMISSIONS_REQUEST_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int providerIndex = helperUtility.indexOf(Manifest.permission.CALL_PHONE, permissions);

        if(providerIndex == -1)
            return;

        boolean permissionGranted = grantResults[providerIndex] == PermissionChecker.PERMISSION_GRANTED;

        switch(requestCode) {
            case MY_PERMISSIONS_REQUEST_PHONE:
                if(permissionGranted)
                    callMember();
                else
                    helperUtility.showToast("Phone permission is required to make a call.");
                break;
        }
    }

    private void callMember() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + member.getPhoneNumber()));
            startActivity(intent);
        } catch(SecurityException e) {
            e.printStackTrace();
            helperUtility.showToast("Something went wrong, try again.");
        }
    }

    private void setupPhoneBtn() {
        Button smsButton = findViewById(R.id.phoneBtn);
        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(grantedPhonePermissions()) {
                    callMember();
                } else {
                    requestPhonePermission();
                }
            }
        });
    }

    private void setupSendSms() {
        Button smsButton = findViewById(R.id.smsBtn);
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
                "Are you sure you wish to delete " + member.getName() + "?");
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