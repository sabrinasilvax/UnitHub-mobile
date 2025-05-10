package com.mobile.unithub.api.responses;

import java.util.List;

public class FeedResponse {
    private List<FeedItemResponse> feedItems;
    private int page;
    private int totalPages;

    public List<FeedItemResponse> getFeedItems() {
        return feedItems;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }
}