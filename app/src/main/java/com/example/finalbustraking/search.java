package com.example.finalbustraking;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class search extends AppCompatActivity {
    private TextView sourceTextView, destinationTextView;
    private ImageView backicon;
    private AutoCompleteTextView autoCompleteTextView;
    private RadioButton selectedOption; // Store the selected option

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set the adapter
        BusAdapter adapter = new BusAdapter();
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String receivedSource = intent.getStringExtra("Source");
        String receivedDestination = intent.getStringExtra("Destination");
        sourceTextView = findViewById(R.id.source);
        destinationTextView = findViewById(R.id.destination);
        backicon = findViewById(R.id.back_icon);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);



        autoCompleteTextView.setOnClickListener(view -> {
            // Create a custom dialog
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog_layout);

            // Initialize dialog components
            RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
            RadioButton option1 = dialog.findViewById(R.id.option1);
            RadioButton option2 = dialog.findViewById(R.id.option2);
            RadioButton option3 = dialog.findViewById(R.id.option3);
            RadioButton option4 = dialog.findViewById(R.id.option4);

            // Initialize the selected option to the last selected option
            if (selectedOption != null) {
                selectedOption.setChecked(true);
            }

            // Attach click listeners to the radio buttons
            option1.setOnClickListener(v -> {
                selectedOption = option1; // Update the selected option
                setRadioState(option1, true);
                setRadioState(option2, false);
                setRadioState(option3, false);
                autoCompleteTextView.setText("All Dates");
                dialog.dismiss();
            });

            option2.setOnClickListener(v -> {
                selectedOption = option2; // Update the selected option
                setRadioState(option1, false);
                setRadioState(option2, true);
                setRadioState(option3, false);
                autoCompleteTextView.setText("Today");
                dialog.dismiss();
            });

            option3.setOnClickListener(v -> {
                selectedOption = option3; // Update the selected option
                setRadioState(option1, false);
                setRadioState(option2, false);
                setRadioState(option3, true);
                autoCompleteTextView.setText("Tomorrow");
                dialog.dismiss();
            });

            dialog.show();
        });

        // Update the text properties of the TextView elements
        sourceTextView.setText(receivedSource);
        destinationTextView.setText(receivedDestination);
        backicon.setOnClickListener(view -> finish());

        // Find the bus numbers based on source and destination
        CollectionReference journeyCollection = db.collection("journeyDetails");
        Query journeyQuery = journeyCollection
                .whereEqualTo("source", receivedSource)
                .whereEqualTo("destination", receivedDestination);

        journeyQuery.get().addOnSuccessListener(journeyQueryDocumentSnapshots -> {
            List<String> busNumbers = new ArrayList<>();
            for (QueryDocumentSnapshot journeyDocument : journeyQueryDocumentSnapshots) {
                String busNumber = journeyDocument.getString("busnumber");
                busNumbers.add(busNumber);
            }

            // Fetch bus details based on the found bus numbers
            fetchBusDetails(db.collection("busDetails"), adapter, busNumbers);
        });
    }

    private void setRadioState(RadioButton radioButton, boolean checked) {
        radioButton.setChecked(checked);
    }

    private void fetchBusDetails(CollectionReference busCollection, BusAdapter adapter, List<String> busNumbers) {
        List<Bus> busList = new ArrayList<>();
        AtomicInteger queryCounter = new AtomicInteger(0);

        for (String busNumber : busNumbers) {
            Query busQuery = busCollection.whereEqualTo("busnumber", busNumber);

            busQuery.get().addOnSuccessListener(busQueryDocumentSnapshots -> {
                for (QueryDocumentSnapshot busDocument : busQueryDocumentSnapshots) {
                    Bus bus = busDocument.toObject(Bus.class);
                    busList.add(bus);
                }

                queryCounter.incrementAndGet();

                if (queryCounter.get() == busNumbers.size()) {
                    adapter.setBusList(busList);
                }
            });
        }
    }
}
