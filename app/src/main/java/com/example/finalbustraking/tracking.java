package com.example.finalbustraking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class tracking extends AppCompatActivity {
private TextView busnumberTextView;
    private TextView busnameTextView;

    private TextView starttimeTextView;

    private TextView endtimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        Intent intent = getIntent();
        String busnumber = intent.getStringExtra("busnumber");
        String busname = intent.getStringExtra("busname");
        String starttime = intent.getStringExtra("starttime");
        String endtime = intent.getStringExtra("endtime");

        busnumberTextView = findViewById(R.id.busnumber);
        busnameTextView = findViewById(R.id.busname);
        starttimeTextView = findViewById(R.id.arrival_time);
        endtimeTextView = findViewById(R.id.endtime);

        busnumberTextView.setText(busnumber);
        busnameTextView.setText(busname);
        starttimeTextView.setText(starttime);
        endtimeTextView.setText(endtime);




    }
}