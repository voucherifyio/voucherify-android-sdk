package io.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

public class VoucherResponse {

    private String code;

    private boolean valid;

    private Discount discount;

    private Gift gift;

    private String reason;

    @SerializedName("tracking_id")
    private String trackingId;

    public String getCode() {
        return code;
    }

    public boolean isValid() {
        return valid;
    }

    public Discount getDiscount() {
        return discount;
    }

    public Gift getGift() {
        return gift;
    }

    public String getReason() {
        return reason;
    }

    public String getTrackingId() {
        return trackingId;
    }
}
