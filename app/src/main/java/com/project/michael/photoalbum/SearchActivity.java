package com.project.michael.photoalbum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SearchActivity extends AppCompatActivity {

    private EditText fromDate;
    private EditText toDate;
    private EditText fromLoc;
    private EditText toLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fromDate = findViewById(R.id.timeframe_from_text);
        toDate   = findViewById(R.id.timeframe_to_text);
        fromLoc = findViewById(R.id.location_from_text);
        toLoc = findViewById(R.id.location_to_text);
    }

    public void goBack(View v) { this.finish(); }

    public void search(View v) {
        Intent i = new Intent();

        finish();
    }
}
