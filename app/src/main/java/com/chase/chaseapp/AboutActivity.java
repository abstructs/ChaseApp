package com.chase.chaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AboutActivity extends AppCompatActivity {

    private ListView devsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        devsList = findViewById(R.id.devsList);

        String[] developers = new String[]{
                "Andrew Wilson - 101069680",
                "Justin Rolnick - 100407074",
                "Veronica Cheren - 100831208",
                "Karina Gorkova - 101032995",
                "Dindyal Mursingh - 101083659"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, developers);

        devsList.setAdapter(adapter);
    }
}
