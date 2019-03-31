package com.project.michael.photoalbum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        i.putExtra("STARTDATE", getDate(fromDate.getText().toString()));
        i.putExtra("ENDDATE", getDate(toDate.getText().toString()));
        i.putExtra("STARTLOC", getLocation(fromLoc.getText().toString()));
        i.putExtra("ENDLOC", getLocation(toLoc.getText().toString()));
        setResult(RESULT_OK, i);
        finish();
    }

    private String getDate(String date) {
        if (date.length() == 0) {
            return "";
        }
        StringBuilder temp = new StringBuilder();
        String[] dates = date.split("/");
        for (String date1 : dates) {
            temp.append(date1);
        }
        Log.d("Date is: ", temp.toString());
        return temp.toString();
    }

    private double[] getLocation(String loc) {
        String[] coords = loc.split(",");
        double[] locs = new double[2];
        for (int x = 0; x < coords.length; x++) {
            if (coords[x].length() != 0)
                locs[x] = Double.parseDouble(coords[x]);
            else
                locs[x] = 0;
        }
        return locs;
    }
}
