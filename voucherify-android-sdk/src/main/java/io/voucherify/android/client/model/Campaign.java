package io.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Campaign {

    private String id;

    private String object;

    @SerializedName("start_date")
    private Date startDate;

    @SerializedName("expiration_date")
    private Date expirationDate;

    private Boolean active;

    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public Boolean getActive() {
        return active;
    }
}
