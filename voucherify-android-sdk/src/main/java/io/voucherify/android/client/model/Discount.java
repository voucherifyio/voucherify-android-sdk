package io.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

public class Discount {

    private DiscountType type;

    @SerializedName("amount_off")
    private Integer amountOff;

    @SerializedName("percent_off")
    private Double percentOff;

    @SerializedName("unit_off")
    private Double unitOff;

    @SerializedName("unit_type")
    private String unitType;

    public DiscountType getType() {
        return type;
    }

    public void setType(DiscountType type) {
        this.type = type;
    }

    public Integer getAmountOff() {
        return amountOff;
    }

    public void setAmountOff(Integer amountOff) {
        this.amountOff = amountOff;
    }

    public Double getPercentOff() {
        return percentOff;
    }

    public void setPercentOff(Double percentOff) {
        this.percentOff = percentOff;
    }
    public Double getUnitOff() {
        return unitOff;
    }

    public void setUnitOff(Double unitOff) {
        this.unitOff = unitOff;
    }
    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
}
