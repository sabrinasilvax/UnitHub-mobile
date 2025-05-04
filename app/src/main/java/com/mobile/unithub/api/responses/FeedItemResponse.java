package com.mobile.unithub.api.responses;

import java.util.List;

public class FeedItemResponse {
    private String eventId;
    private String title;
    private String description;
    private String dateTime;
    private String location;
    private List<String> images;

    // Getters e Setters
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
}