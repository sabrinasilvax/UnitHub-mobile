package com.mobile.unithub.api.responses;

import java.util.List;

public class FeedItemResponse {
    private String eventId;
    private String title;
    private String description;
    private String dateTime;
    private String location;
    private List<String> category;

    private boolean isOfficial;
    private List<String> images;
    private int maxParticipants;



    // Getters e Setters
    public int getMaxParticipants() {
        return maxParticipants;
    }

    public List<String> getCategory() {
        return category;
    }
    
    public String getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getLocation() {
        return location;
    }

    public List<String> getImages() {
        return images;
    }

    public boolean isOfficial() {
        return isOfficial;
    }
}