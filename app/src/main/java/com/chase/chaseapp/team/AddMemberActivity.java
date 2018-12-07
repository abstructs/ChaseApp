package com.chase.chaseapp.team;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.chase.chaseapp.R;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import database.AppDatabase;
import entities.Member;

public class AddMemberActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        db = AppDatabase.getAppDatabase(getApplicationContext());
        setupSaveBtn();
    }

    private void setupSaveBtn() {
        Button saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formIsValid())
                    saveMemberThenFinish(getMember());
                else
                    showErrorToast();
            }
        });
    }

    private Member getMember() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText phoneInput = findViewById(R.id.phoneInput);
        EditText emailInput = findViewById(R.id.emailInput);
        Random rand = new Random();


        final String name = nameInput.getText().toString();
        final String phoneNumber = phoneInput.getText().toString();
        final String email = emailInput.getText().toString();
        int imageId = rand.nextInt(5) + 1;

        Member member = new Member();

        member.setName(name);
        member.setPhoneNumber(phoneNumber);
        member.setEmail(email);
        member.setImageId(imageId);

        return member;
    }

    private void showErrorToast() {
        Toast.makeText(getApplicationContext(), "Please fill out the fields.",
                Toast.LENGTH_LONG).show();
    }

    private void showSuccessToast() {
        Toast.makeText(getApplicationContext(), "Member has been added.",
                Toast.LENGTH_LONG).show();
    }

    private void saveMemberThenFinish(final Member member) {
        class InsertMember extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... params) {
                db.memberDao().insertOne(getMember());
                return true;
            }

            @Override
            protected void onPostExecute(Boolean res) {
                showSuccessToast();
                finish();
            }
        }

        new InsertMember().execute();
    }

    private boolean formIsValid() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText phoneInput = findViewById(R.id.phoneInput);
        EditText emailInput = findViewById(R.id.emailInput);

        return nameInput.getText().length() != 0
                && phoneInput.getText().length() == 10
                && validate(emailInput.getText().toString());
    }

    public static boolean validate(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }
}
