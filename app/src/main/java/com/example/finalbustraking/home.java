package com.example.finalbustraking;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {
    private View underline;
    private TextView textView1, textView2, textView3;
    private DrawerLayout drawerLayout;
    private EditText source,Destination;
    private ImageView menuIcon;
    private Button searchButton;
    private AutoCompleteTextView autoSourceTextView,autoDesTextView;
    private int currentX = 0; // Store the current X position of the underline
    private List<String> suggestions = new ArrayList<>(); // Declare suggestions as a class-level variable

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (!SharedPreferencesHelper.isLoggedIn(this)) {
            // User is not logged in, redirect to the login screen
            Intent intent = new Intent(home.this, login.class);
            startActivity(intent);
            finish();
        }
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menu_icon);
        underline = findViewById(R.id.underline_view);
        textView1 = findViewById(R.id.spot_btn);
        textView2 = findViewById(R.id.bus_stop_btn);
        textView3 = findViewById(R.id.pass_btn);
        NavigationView navigationView = findViewById(R.id.nav_view);
        autoSourceTextView = findViewById(R.id.sourceEditText);
        db.collection("journeyDetails")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                String source = documentSnapshot.getString("source");
                                if (source != null) {
                                    suggestions.add(source);
                                }
                            }
                        }
                        // After fetching data from all documents, set up the adapter
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(home.this, android.R.layout.simple_dropdown_item_1line, suggestions);
                        autoSourceTextView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                        Log.e(TAG, "Error fetching data from Firestore", e);
                    }
                });
        autoDesTextView = findViewById(R.id.destinationEditText);
        db.collection("journeyDetails")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                String source = documentSnapshot.getString("destination");
                                if (source != null) {
                                    suggestions.add(source);
                                }
                            }
                        }
                        // After fetching data from all documents, set up the adapter
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(home.this, android.R.layout.simple_dropdown_item_1line, suggestions);
                        autoDesTextView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                        Log.e(TAG, "Error fetching data from Firestore", e);
                    }
                });
         searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Source= autoSourceTextView.getText().toString();
                String Destination = autoDesTextView.getText().toString();
                if(TextUtils.isEmpty(Source) || TextUtils.isEmpty(Destination)){
                    Toast.makeText(home.this, "Enter the field properly", Toast.LENGTH_SHORT).show();

                } else if (Source.equals(Destination)) {

                    Toast.makeText(home.this, "Both field should not be same ", Toast.LENGTH_SHORT).show();

                }
                else {
                    Intent intent = new Intent(home.this, search.class);
                    intent.putExtra("Source", Source);
                    intent.putExtra("Destination", Destination);

                    startActivity(intent);
                }
            }
        });


        // Create an array to hold the suggestions


// Initialize the underline to the initial position (textView1)

        underline.setTranslationX(currentX);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation item clicks here
                int itemId = item.getItemId();
                if (itemId == R.id.logout) {
                    // Show a confirmation dialog before logging out
                    AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                    builder.setTitle("Logout");
                    builder.setMessage("Are you sure you want to logout?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferencesHelper.setLoggedIn(home.this, false);

                            Intent intent = new Intent(home.this, login.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing, just close the dialog
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();


                } else if (itemId == R.id.Shared_app) {// Handle the click on the second item
                    Toast.makeText(home.this, "hello", Toast.LENGTH_SHORT).show();
                    // Add cases for other menu items as needed
                }

                // Close the drawer after handling the item click
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the navigation drawer when the menu_icon is clicked
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveUnderline(textView1);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveUnderline(textView2);
            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveUnderline(textView3);
            }
        });

    }

// Define a method to show the list using the provided context








    private void moveUnderline(final View targetView) {
        int startX = currentX; // Start from the current X position
        int endX = targetView.getLeft() + (targetView.getWidth() / 2) - (underline.getWidth() / 2);

        ValueAnimator animator = ValueAnimator.ofInt(startX, endX);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                underline.setTranslationX(value);
                currentX = value; // Update the current X position
            }
        });

        animator.setDuration(200);
        animator.start();
    }
}