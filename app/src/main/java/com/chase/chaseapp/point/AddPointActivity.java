package com.chase.chaseapp.point;

import com.chase.chaseapp.R;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import database.AppDatabase;
import entities.Point;

public class AddPointActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);

        db = AppDatabase.getAppDatabase(getApplicationContext());

        setupSaveBtn();
    }

    private void setupSaveBtn() {
        Button saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formIsValid())
                    savePointThenFinish(getPoint());
                else
                    showErrorToast();
            }
        });
    }

    private Point getPoint() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText addressInput = findViewById(R.id.addressInput);
        Spinner tagSpinner = findViewById(R.id.tagSpinner);

        final String name = nameInput.getText().toString();
        final String address = addressInput.getText().toString();
        final String tag = tagSpinner.getSelectedItem().toString();

        Point point = new Point();

        point.setTitle(name);
        point.setRating(0);
        point.setAddress(address);
        point.setTag(tag);

        return point;
    }

    private void showErrorToast() {
        Toast.makeText(getApplicationContext(), "Please fill out the fields.",
                Toast.LENGTH_LONG).show();
    }

    private void showSuccessToast() {
        Toast.makeText(getApplicationContext(), "Point has been added.",
                Toast.LENGTH_LONG).show();
    }

    private void savePointThenFinish(final Point point) {
        class InsertPoint extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... params) {
                db.pointDao().insertOne(getPoint());
                return true;
            }

            @Override
            protected void onPostExecute(Boolean res) {
                finish();
            }
        }

        new InsertPoint().execute();
    }

    private boolean formIsValid() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText addressInput = findViewById(R.id.addressInput);
        Spinner tagSpinner = findViewById(R.id.tagSpinner);

        return tagSpinner.getSelectedItemPosition() != 0 && nameInput.getText().length() != 0 &&
                addressInput.getText().length() != 0;
    }
}
