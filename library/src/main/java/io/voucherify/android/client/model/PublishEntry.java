package io.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

public class PublishEntry {

    private String customer;
    private String channel;
    
    @SerializedName("published_at")
    private Date publishedAt;

    private Map<String, Object> metadata;

    public String getCustomer() {
        return customer;
    }

    public String getChannel() {
        return channel;
    }

    
    public Date getPublishedAt() {
        return publishedAt;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
}