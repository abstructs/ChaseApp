package com.chase.chaseapp.team;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chase.chaseapp.R;
import com.chase.chaseapp.helper.HelperUtility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.AppDatabase;
import entities.Member;

public class EditMemberActivity extends AppCompatActivity {

    private AppDatabase db;
    private Member member;
    private HelperUtility helperUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        db = AppDatabase.getAppDatabase(getApplicationContext());
        member = getIntent().getParcelableExtra("member");
        helperUtility = new HelperUtility(getApplicationContext());

        populateFields();
        setupSaveBtn();
    }

    private void populateFields() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText phoneNumberInput = findViewById(R.id.phoneInput);
        EditText emailInput = findViewById(R.id.emailInput);

        nameInput.setText(member.getName());
        phoneNumberInput.setText(member.getPhoneNumber());
        emailInput.setText(member.getEmail());
    }

    private void setupSaveBtn() {
        Button saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formIsValid())
                    updateMemberThenFinish();
                else
                    helperUtility.showToast("Please fill out the fields");
            }
        });
    }

    private void setMember() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText phoneNumberInput = findViewById(R.id.phoneInput);
        EditText emailInput = findViewById(R.id.emailInput);

        final String name = nameInput.getText().toString();
        final String phoneNumber = phoneNumberInput.getText().toString();
        final String email = emailInput.getText().toString();

        member.setName(name);
        member.setPhoneNumber(phoneNumber);
        member.setEmail(email);
    }

    private void updateMemberThenFinish() {
        class UpdateMember extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... params) {
                setMember();
                db.memberDao().updateOne(member);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean res) {
                helperUtility.showToast("Member has been updated");
                finish();
            }
        }

        new UpdateMember().execute();
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
