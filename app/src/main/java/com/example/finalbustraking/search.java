package com.example.finalbustraking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class search extends AppCompatActivity {
private TextView sourceTextView ,destinationTextView;
    private ImageView backicon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        String receivedSource = intent.getStringExtra("Source");
        String receivedDestination = intent.getStringExtra("Destination");
        sourceTextView = findViewById(R.id.source);
        destinationTextView = findViewById(R.id.destination);
        backicon=findViewById(R.id.back_icon);

        // Update the text properties of the TextView elements
        sourceTextView.setText(receivedSource);
        destinationTextView.setText(receivedDestination);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}