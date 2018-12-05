package com.chase.chaseapp.point;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.chase.chaseapp.R;

public class EditPointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_point);

        Button saveBtn = findViewById(R.id.saveBtn);
        EditText nameInput = findViewById(R.id.nameInput);
        EditText addressInput = findViewById(R.id.addressInput);
        Spinner tagSpinner = findViewById(R.id.tagSpinner);
    }
}
