    package com.example.finalbustraking;

    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import com.google.android.gms.maps.CameraUpdateFactory;
    import com.google.android.gms.maps.GoogleMap;
    import com.google.android.gms.maps.MapView;
    import com.google.android.gms.maps.OnMapReadyCallback;
    import com.google.android.gms.maps.model.LatLng;
    import com.google.android.gms.maps.model.MarkerOptions;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    public class map extends AppCompatActivity implements OnMapReadyCallback {

        private MapView mapView;
        private GoogleMap googleMap;
        private DatabaseReference locationsRef;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map);

            // Initialize the MapView
            mapView = findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);

            // Initialize Firebase Realtime Database reference
            locationsRef = FirebaseDatabase.getInstance().getReference("locations");

            Button refreshButton = findViewById(R.id.refresh_button);
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refreshLocations();
                }
            });
        }

        @Override
        public void onMapReady(GoogleMap map) {
            googleMap = map;
            // Configure Google Map settings here

            // Zoom to a default location or location of interest
            LatLng defaultLocation = new LatLng(0, 0); // Change to your default location
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));
        }

        private void refreshLocations() {
            locationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    googleMap.clear(); // Clear existing markers
                    for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                        double latitude = locationSnapshot.child("latitude").getValue(Double.class);
                        double longitude = locationSnapshot.child("longitude").getValue(Double.class);

                        // Create a marker and add it to the map
                        LatLng location = new LatLng(latitude, longitude);
                        googleMap.addMarker(new MarkerOptions().position(location));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }

        @Override
        protected void onResume() {
            super.onResume();
            mapView.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();
            mapView.onPause();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            mapView.onDestroy();
        }

        @Override
        public void onLowMemory() {
            super.onLowMemory();
            mapView.onLowMemory();
        }
    }
