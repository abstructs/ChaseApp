package com.chase.chaseapp.team;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.chase.chaseapp.R;
import com.chase.chaseapp.helper.HelperUtility;
import java.util.Random;
import database.AppDatabase;
import entities.Member;

import static com.chase.chaseapp.helper.HelperUtility.validateEmail;

public class AddMemberActivity extends AppCompatActivity {

    private AppDatabase db;
    private HelperUtility helperUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        helperUtility = new HelperUtility(getApplicationContext());
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

    private void saveMemberThenFinish(final Member member) {
        class InsertMember extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... params) {
                db.memberDao().insertOne(getMember());
                return true;
            }

            @Override
            protected void onPostExecute(Boolean res) {
                helperUtility.showToast("Member has been added");
                finish();
            }
        }

        new InsertMember().execute();
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
