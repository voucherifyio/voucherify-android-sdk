package pl.rspective.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

public class VoucherResponse {

    private boolean valid;

    private Discount discount;

    @SerializedName("tracking_id")
    private String trackingId;

    public boolean isValid() {
        return valid;
    }

    public Discount getDiscount() {
        return discount;
    }

    public String getTrackingId() {
        return trackingId;
    }
}
