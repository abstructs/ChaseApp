package com.chase.chaseapp.task;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chase.chaseapp.R;

import database.AppDatabase;
import entities.Task;

public class AddTaskActivity extends AppCompatActivity {

    private AppDatabase db;

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        db = AppDatabase.getAppDatabase(getApplicationContext());

        task = new Task();

        setupSaveBtn();
    }

    private void setupSaveBtn() {
        Button saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTask();
                saveTaskAndFinish();
            }
        });
    }

    private void setTask() {
        EditText titleInput = findViewById(R.id.titleInput);
        EditText descriptionInput = findViewById(R.id.descriptionInput);

        String title = titleInput.getText().toString();
        String description = descriptionInput.getText().toString();

        task.setTitle(title);
        task.setDescription(description);
        task.setAchieved(false);
    }

    private void showErrorToast() {
        Toast.makeText(getApplicationContext(), "Please fill out the forms.",
                Toast.LENGTH_LONG).show();
    }

    private void showSuccessToast() {
        Toast.makeText(getApplicationContext(), "Task has been added.",
                Toast.LENGTH_LONG).show();
    }

    private boolean formIsValid() {
        EditText titleInput = findViewById(R.id.titleInput);
        EditText descriptionInput = findViewById(R.id.descriptionInput);

        String title = titleInput.getText().toString();
        String description = descriptionInput.getText().toString();

        return title.length() != 0 && description.length() != 0;
    }

    private void saveTaskAndFinish() {
        class InsertTask extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... voids) {
                db.taskDao().insertOne(task);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(formIsValid()) {
                    showSuccessToast();
                    finish();
                } else {
                    showErrorToast();
                }
            }
        }

        new InsertTask().execute();
    }
}
