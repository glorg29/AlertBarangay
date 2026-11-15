package com.example.alertbarangay;

public class Report {

    private final String id;
    private final String emergencyType;
    private final String location;
    private final String details;
    private final boolean hasPhoto;

    public Report(
            String id,
            String emergencyType,
            String location,
            String details,
            boolean hasPhoto
    ) {
        this.id = id;
        this.emergencyType = emergencyType;
        this.location = location;
        this.details = details;
        this.hasPhoto = hasPhoto;
    }

    public String getId() {
        return id;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public String getLocation() {
        return location;
    }

    public String getDetails() {
        return details;
    }

    public boolean hasPhoto() {
        return hasPhoto;
    }
}