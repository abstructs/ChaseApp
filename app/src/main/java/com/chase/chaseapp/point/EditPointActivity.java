package com.chase.chaseapp.point;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.chase.chaseapp.R;

import database.AppDatabase;

public class EditPointActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_point);

        db = AppDatabase.getAppDatabase(getApplicationContext());

//        populateFields();

        setupSaveBtn();
    }

    private void showError() {
        Toast.makeText(getApplicationContext(), "Please fill out the fields.",
                Toast.LENGTH_LONG).show();
    }

    private void setupSaveBtn() {
        Button saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formIsValid())
                    savePointThenFinish();
                else
                    showError();
            }
        });
    }

    private void savePointThenFinish() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText addressInput = findViewById(R.id.addressInput);
        Spinner tagSpinner = findViewById(R.id.tagSpinner);

        String name = nameInput.getText().toString();
        String address = addressInput.getText().toString();
        String tag = tagSpinner.getSelectedItem().toString();


        // TODO: save into database
        System.out.println(name + " " + addressInput + " " + tag);

        finish();
    }

    private boolean formIsValid() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText addressInput = findViewById(R.id.addressInput);
        Spinner tagSpinner = findViewById(R.id.tagSpinner);

        return tagSpinner.getSelectedItemPosition() != 0 && nameInput.getText().length() != 0 &&
                addressInput.getText().length() != 0;
    }
}
