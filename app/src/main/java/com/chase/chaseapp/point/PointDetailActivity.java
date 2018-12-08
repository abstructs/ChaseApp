package com.chase.chaseapp.point;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chase.chaseapp.R;
import database.AppDatabase;
import entities.Point;


public class PointDetailActivity extends AppCompatActivity {

    private Point point;
    private AppDatabase db;

    private int requestCode;
    private static final int ADD_POINT_REQUEST = 1;
    private static final int EDIT_POINT_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);

        db = AppDatabase.getAppDatabase(getApplicationContext());

        requestCode = getIntent().getIntExtra("requestCode", 0);

        if(requestCode == EDIT_POINT_REQUEST) {
            point = getIntent().getParcelableExtra("point");
            populateFields();
        } else {
            TextView title = findViewById(R.id.detailTitle);
            title.setText("Add Point");
        }

        setupSaveBtn();
    }

    private int indexOf(String element, String[] items) {
        int i = 0;
        for(String item : items) {
            if(item.equals(element))
                return i;

            i++;
        }

        return -1;
    }

    private void populateFields() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText addressInput = findViewById(R.id.addressInput);
        Spinner tagSpinner = findViewById(R.id.tagSpinner);

        nameInput.setText(point.getTitle());
        addressInput.setText(point.getAddress());

        String[] tagOptions =  getResources().getStringArray(R.array.tags_array);

        tagSpinner.setSelection(indexOf(point.getTag(), tagOptions));
    }

    private void showErrorToast() {
        Toast.makeText(getApplicationContext(), "Please fill out the fields.",
                Toast.LENGTH_LONG).show();
    }

    private void showSuccessToast() {
        Toast.makeText(getApplicationContext(), "Point has been updated.",
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
                    showErrorToast();
            }
        });
    }

    private void setPoint() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText addressInput = findViewById(R.id.addressInput);
        Spinner tagSpinner = findViewById(R.id.tagSpinner);

        final String name = nameInput.getText().toString();
        final String address = addressInput.getText().toString();
        final String tag = tagSpinner.getSelectedItem().toString();

        if (requestCode == ADD_POINT_REQUEST) {
            point = new Point();
            point.setRating(0);
        }

        point.setTitle(name);
        point.setAddress(address);
        point.setTag(tag);
    }

    private void savePointThenFinish() {
        class SavePoint extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... params) {
                setPoint();
                if (requestCode == EDIT_POINT_REQUEST)
                    db.pointDao().updateOne(point);
                else
                    db.pointDao().insertOne(point);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean res) {
                showSuccessToast(); //set message to 'point added' when requestcode is for add

                Intent intent;

                if(requestCode == ADD_POINT_REQUEST) {
                    intent = new Intent(PointDetailActivity.this, PointActivity.class);
                    intent.putExtra("point", point);
                    startActivity(intent);
                } else {
                    intent = getIntent();
                    intent.putExtra("point", point);
                }
                finish();
            }
        }

        new SavePoint().execute();
    }

    private boolean formIsValid() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText addressInput = findViewById(R.id.addressInput);
        Spinner tagSpinner = findViewById(R.id.tagSpinner);

        return tagSpinner.getSelectedItemPosition() != 0 && nameInput.getText().length() != 0 &&
                addressInput.getText().length() != 0;
    }
}
