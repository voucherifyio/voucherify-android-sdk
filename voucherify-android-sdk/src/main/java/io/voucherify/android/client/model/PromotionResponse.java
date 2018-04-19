package io.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromotionResponse {


    private boolean valid;

    private List<Promotion> promotions;

    @SerializedName("tracking_id")
    private String trackingId;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }
}
