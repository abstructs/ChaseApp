package com.chase.chaseapp.team;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.chase.chaseapp.R;
import com.chase.chaseapp.helper.HelperUtility;
import database.AppDatabase;
import entities.Member;

import static com.chase.chaseapp.helper.HelperUtility.validateEmail;

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

        if(nameInput.getText().length() == 0) {
            helperUtility.showToast("Please enter name");
            return false;
        }
        if(phoneInput.getText().length() != 10) {
            helperUtility.showToast("Please enter 10-digit phone number");
            return false;
        }
        if(!validateEmail(emailInput.getText().toString())) {
            helperUtility.showToast("Please enter valid email address");
            return false;
        }

        return true;
    }
}
