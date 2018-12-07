package com.chase.chaseapp.task;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.chase.chaseapp.R;

import database.AppDatabase;
import entities.Task;

public class EditTaskActivity extends AppCompatActivity {

    private Task task;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        task = getIntent().getParcelableExtra("task");
        db = AppDatabase.getAppDatabase(getApplicationContext());

        populateFields();

        setupSaveBtn();
    }

    private void populateFields() {
        EditText titleInput = findViewById(R.id.titleInput);
        EditText descriptionInput = findViewById(R.id.descriptionInput);
        titleInput.setText(task.getTitle());
        descriptionInput.setText(task.getDescription());
    }

    private void setTask() {
        EditText titleInput = findViewById(R.id.titleInput);
        EditText descriptionInput = findViewById(R.id.descriptionInput);

        String title = titleInput.getText().toString();
        String description = descriptionInput.getText().toString();

        task.setTitle(title);
        task.setDescription(description);
    }

    private void showSuccessToast() {
        Toast.makeText(getApplicationContext(), "Task has been updated.",
                Toast.LENGTH_LONG).show();
    }

    private void showErrorToast() {
        Toast.makeText(getApplicationContext(), "Please fill out the forms.",
                Toast.LENGTH_LONG).show();
    }

    private void updateTaskThenFinish() {
        class UpdatePoint extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... params) {
                setTask();
                db.taskDao().updateOne(task);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean res) {
                showSuccessToast();
                finish();
            }
        }

        new UpdatePoint().execute();
    }

    private boolean formIsValid() {
        EditText titleInput = findViewById(R.id.titleInput);
        EditText descriptionInput = findViewById(R.id.descriptionInput);

        String title = titleInput.getText().toString();
        String description = descriptionInput.getText().toString();

        return title.length() != 0 && description.length() != 0;
    }

    private void setupSaveBtn() {
        Button saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formIsValid()) {
                    updateTaskThenFinish();
                    showSuccessToast();
                } else {
                    showErrorToast();
                }

            }
        });
    }
}
