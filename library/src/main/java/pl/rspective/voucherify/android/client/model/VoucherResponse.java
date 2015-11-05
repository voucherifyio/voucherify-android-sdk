package pl.rspective.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

public class VoucherResponse {

    private boolean valid;
    private String type;
    private double discount;
	@SerializedName("tracking_id")
    private String trackingId;

    public boolean isValid() {
        return valid;
    }

    public String getType() {
        return type;
    }

    public double getDiscount() {
        return discount;
    }

    public String getTrackingId() {
        return trackingId;
    }
}