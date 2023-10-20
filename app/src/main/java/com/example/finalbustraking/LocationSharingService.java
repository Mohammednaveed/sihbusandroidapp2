package com.example.finalbustraking;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationSharingService  extends Service {
    private static final String CHANNEL_ID = "LocationTrackingServiceChannel";
    private static final int NOTIFICATION_ID = 123;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    public static final String ACTION_START = "com.example.finalbustraking.START_LOCATION";
    public static final String ACTION_STOP = "com.example.finalbustraking.STOP_LOCATION";
    private WakeLock wakeLock; // Declare the WakeLock as a class-level variable

    private boolean isLocationSharing = false; // Track if location sharing is active

    public LocationSharingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel(); // Create the notification channel

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LocationTrackingService:WakeLock");

        // Start the WakeLock when the service starts
        wakeLock.acquire();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                Location location = locationResult.getLastLocation();
                if (location != null) {
                    if (isLocationSharing) {
                        // Send the location data to Firebase Realtime Database
                        sendLocationToFirebase(location);
                    }
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_START)) {
                isLocationSharing = true;
                createNotificationChannel(); // Create the notification channel
                if (!isNotificationPermissionGranted()) {
                    // If permission is not granted, request it
                    requestNotificationPermission();
                } else {
                    startForeground(NOTIFICATION_ID, createNotification()); // Start the service as a foreground service
                    requestLocationUpdates();
                }
            } else if (intent.getAction().equals(ACTION_STOP)) {
                isLocationSharing = false;
                stopLocationUpdates();
                stopForeground(true); // Stop the service as a foreground service

            }
        }


        return START_STICKY;
    }
    private boolean isNotificationPermissionGranted() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        return notificationManager.areNotificationsEnabled();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        stopLocationUpdates();
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Location Tracking Service", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }


    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, driverlocationparmission.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Tracking Service")
                .setContentText("Running in the background")
                .setSmallIcon(R.drawable.playstore)
                .setContentIntent(pendingIntent)

                .build();
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000); // Update interval in milliseconds
        locationRequest.setFastestInterval(3000); // Fastest update interval
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void sendLocationToFirebase(Location location) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("locations");
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
        LocationData locationData = new LocationData(location.getLatitude(), location.getLongitude());
        locationData.setTimestamp(System.currentTimeMillis());
        locationData.setDeviceDate(deviceDate);
        locationData.setDeviceTime(deviceTime);

        // Send location data to Firebase under the "locations" node with the unique key
        databaseRef.child(locationKey).setValue(locationData);
    }

    private void requestNotificationPermission() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
