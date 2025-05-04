package com.mobile.unithub.api.responses;

import java.util.List;

public class FeedResponse {
    private List<FeedItemResponse> feedItems;
    private int page;
    private int pageSize;
    private int totalPages;
    private int totalElements;

    public List<FeedItemResponse> getFeedItems() {
        return feedItems;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }
}