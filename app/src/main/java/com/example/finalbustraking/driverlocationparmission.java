package com.example.finalbustraking;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class driverlocationparmission extends AppCompatActivity {
    private View underline;
    private TextView textView1, textView2, textView3;
    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private AutoCompleteTextView autoSourceTextView,autoDesTextView;

    private int currentX = 0; // Store the current X position of the underline

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference databaseRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Handler locationUpdateHandler;
    private static final long LOCATION_UPDATE_INTERVAL = 5000; // 5 seconds
    private boolean locationPermissionGranted = false;
    private List<String>  suggestions = new ArrayList<>(); // Declare suggestions as a class-level variable

    private boolean isLocationSharing = false; // Track if location sharing is active
    private TextView rec_loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverlocationparmission);

        // Initialize Firebase Realtime Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("locations");

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        rec_loc = findViewById(R.id.receive_location_button);

        // Initialize the locationUpdateHandler
        locationUpdateHandler = new Handler(Looper.getMainLooper());

        // Check if location permission is granted, and if not, request it
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            requestLocationPermission();
        }

        rec_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationPermissionGranted) {
                    if (!isLocationSharing) {
                        // If location sharing is not active, start sharing
                        startLocationUpdates();
                        rec_loc.setText("Stop Location Sharing "); // Change the TextView text to "Stop"
                    } else {
                        // If location sharing is active, stop sharing
                        stopLocationUpdates();
                        rec_loc.setText("Start Location Sharing "); // Change the TextView text to "Start"
                    }
                    isLocationSharing = !isLocationSharing; // Toggle the location sharing state
                } else {
                    requestLocationPermission();
                }
            }
        });

        // Initialize your views and variables here
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menu_icon);
        underline = findViewById(R.id.underline_view);
        textView1 = findViewById(R.id.spot_btn);
        textView2 = findViewById(R.id.bus_stop_btn);
        textView3 = findViewById(R.id.pass_btn);

        // Handle navigation drawer item clicks
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(driverlocationparmission.this, android.R.layout.simple_dropdown_item_1line, suggestions);
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(driverlocationparmission.this, android.R.layout.simple_dropdown_item_1line, suggestions);
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
        Button searchButton = findViewById(R.id.searchButton);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.logout) {
                    // Show a confirmation dialog before logging out
                    AlertDialog.Builder builder = new AlertDialog.Builder(driverlocationparmission.this);
                    builder.setTitle("Logout");
                    builder.setMessage("Are you sure you want to logout?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferencesHelper.setLoggedIn(driverlocationparmission.this, false);

                            Intent intent = new Intent(driverlocationparmission.this, login.class);
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
                } else if (itemId == R.id.Shared_app) {
                    // Handle Shared App action
                    // ... similar to the home activity
                }

                // Close the drawer after handling the item click
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Handle menu icon click
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation if needed (e.g., first-time user)
            // You can show a dialog or provide more context here
            // Then, request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Request the permission without explanation
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location updates
                locationPermissionGranted = true;
                if (isLocationSharing) {
                    startLocationUpdates();
                }
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationUpdates() {
        if (!locationPermissionGranted) {
            return; // Do not start updates if permission is not granted
        }

        locationUpdateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLocationAndSendData();
                // Schedule the next location update after the specified interval
                locationUpdateHandler.postDelayed(this, LOCATION_UPDATE_INTERVAL);
            }
        }, LOCATION_UPDATE_INTERVAL);
    }

    private void getLocationAndSendData() {
        if (locationPermissionGranted && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Get latitude and longitude
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();

                                // Create a unique key for the location entry
                                String locationKey = databaseRef.child("locations").push().getKey();

                                // Get current device date and time as Date objects
                                Date currentDate = new Date();

                                // Format device date and time as strings
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                                String deviceDate = dateFormat.format(currentDate);
                                String deviceTime = timeFormat.format(currentDate);

                                // Build the location data
                                LocationData locationData = new LocationData(latitude, longitude);
                                locationData.setTimestamp(System.currentTimeMillis());

                                // Set the formatted device date and time strings
                                locationData.setDeviceDate(deviceDate);
                                locationData.setDeviceTime(deviceTime);

                                // Send location data to Firebase under the "locations" node with the unique key
                                databaseRef.child(locationKey).setValue(locationData);

                                Toast.makeText(driverlocationparmission.this, "Location sent to server", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    // Implement a method to stop location updates
    private void stopLocationUpdates() {
        // Remove any pending callbacks from the handler to stop location updates
        locationUpdateHandler.removeCallbacksAndMessages(null);
        Toast.makeText(driverlocationparmission.this, "Location sharing stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks from the handler to stop location updates
        locationUpdateHandler.removeCallbacksAndMessages(null);
    }
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
