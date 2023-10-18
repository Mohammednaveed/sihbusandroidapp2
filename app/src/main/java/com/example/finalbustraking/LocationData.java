package com.example.finalbustraking;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationData {
    private double latitude;
    private double longitude;
    private long timestamp;
    private String deviceDate;
    private String deviceTime;

    public LocationData() {
        // Default constructor
    }

    public LocationData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = System.currentTimeMillis(); // Current timestamp in milliseconds
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        this.deviceDate = dateFormat.format(new Date()); // Current device date as a string
        this.deviceTime = timeFormat.format(new Date()); // Current device time as a string
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDeviceDate() {
        return deviceDate;
    }

    public void setDeviceDate(String deviceDate) {
        this.deviceDate = deviceDate;
    }

    public String getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(String deviceTime) {
        this.deviceTime = deviceTime;
    }

    // Setter methods for latitude, longitude, and timestamp

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}