package pl.rspective.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

public class VoucherResponse {

    private boolean valid;

    @SerializedName("discount_type")
    private DiscountType discountType;

    private Integer discount;

    @SerializedName("tracking_id")
    private String trackingId;

    public boolean isValid() {
        return valid;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public Integer getDiscount() {
        return discount;
    }

    public String getTrackingId() {
        return trackingId;
    }
}
