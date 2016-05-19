package pl.rspective.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

public class VoucherResponse {

    private String code;

    private boolean valid;

    private Discount discount;

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

    public String getTrackingId() {
        return trackingId;
    }
}
