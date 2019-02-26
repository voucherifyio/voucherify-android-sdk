package io.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Condition {

    private String id;

    @SerializedName("created_at")
    private Date createdAt;

    public String getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
