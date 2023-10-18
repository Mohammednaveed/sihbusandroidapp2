package com.example.finalbustraking;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.example.finalbustraking.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Handler locationUpdateHandler;
    private static final long LOCATION_UPDATE_INTERVAL = 10000; // 10 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationUpdateHandler = new Handler();

        // Start updating location every 10 seconds
        startLocationUpdates();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void startLocationUpdates() {
        locationUpdateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Fetch and update the location data here
                fetchAndUpdateLocationData();

                // Schedule the next location update after the specified interval
                locationUpdateHandler.postDelayed(this, LOCATION_UPDATE_INTERVAL);
            }
        }, LOCATION_UPDATE_INTERVAL);
    }

    private void fetchAndUpdateLocationData() {
        FirebaseDatabase.getInstance().getReference("locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mMap.clear(); // Clear existing markers
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        LocationData i = snapshot1.getValue(LocationData.class);
                        if (i != null) {
                            double latitude = i.getLatitude();
                            double longitude = i.getLongitude();
                            long timestamp = i.getTimestamp();

                            // Create a LatLng object for the location
                            LatLng locationLatLng = new LatLng(latitude, longitude);

                            // Format the timestamp as "yyyy-MM-dd HH:mm:ss" in Indian time zone
                            String formattedTimestamp = formatTimestamp(timestamp);

                            // Split the formatted timestamp into date and time components
                            String[] dateTimeParts = formattedTimestamp.split(" ");
                            String date = dateTimeParts[0];
                            String time = dateTimeParts[1];

                            // Add a marker to the map
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(locationLatLng)
                                    .title("Date: " + date)
                                    .snippet("Time: " + time)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                            // Move the camera to the new location
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 15));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if needed
            }
        });
    }

    // Helper method to format timestamp as "yyyy-MM-dd HH:mm:ss" in Indian time zone
    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Indian time zone
        Date date = new Date(timestamp); // Timestamp is already in milliseconds
        return sdf.format(date);
    }
}
