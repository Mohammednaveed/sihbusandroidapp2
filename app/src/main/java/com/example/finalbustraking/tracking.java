package com.example.finalbustraking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class tracking extends AppCompatActivity {
    private TextView busnumberTextView;
    private TextView Source;
    private TextView Destination;
    private TextView busnameTextView;
    private TextView starttimeTextView;
    private TextView endtimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        Intent intent = getIntent();
        String busNumber = intent.getStringExtra("busnumber");
        String busname = intent.getStringExtra("busname");
        String starttime = intent.getStringExtra("starttime");
//        String endtime = intent.getStringExtra("endtime");

        String shipSource = intent.getStringExtra("Source");
        String shipDestination = intent.getStringExtra("Destination");
        View includedLayout = findViewById(R.id.steekLayout);

        // Find and modify views within the included layout
        TextView sourceTextView = includedLayout.findViewById(R.id.Source);
        TextView destinationTextView = includedLayout.findViewById(R.id.des);
        TextView arrival_timeTextView = includedLayout.findViewById(R.id.arrival_time);

        // ... Find and modify other views as needed

        // Example: Modify the text of the source and destination TextViews
        sourceTextView.setText(shipSource);
        destinationTextView.setText("New Destination Text");

        arrival_timeTextView.setText(starttime);
        busnumberTextView = findViewById(R.id.busnumber);
        busnameTextView = findViewById(R.id.busname);
//        starttimeTextView = findViewById(R.id.arrival_time);
//        endtimeTextView = findViewById(R.id.endtime);

        busnumberTextView.setText(busNumber);
        busnameTextView.setText(busname);
//        starttimeTextView.setText(starttime);
//        endtimeTextView.setText(endtime);
        // Access elements from the included layout (your_included_layout.xml)





    }
}
