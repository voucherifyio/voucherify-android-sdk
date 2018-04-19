package io.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

/**
 * Class represents a voucher
 */
public class Promotion {

    /**
     * The unique voucher's id
     */
    private String id;

    /**
     * The name of voucher's campaign
     */
    private String campaign;

    /**
     * Discount definition - present if type is DISCOUNT_VOUCHER
     */
    private Discount discount;

    /**
     * Gift definition (amount) - present if type is GIFT_VOUCHER
     */
    private Gift gift;

    /**
     * Voucher's start date
     */
    @SerializedName("start_date")
    private Date startDate;

    /**
     * Voucher's expiration date
     */
    @SerializedName("expiration_date")
    private Date expirationDate;

    /**
     * Disables a voucher when set to false even if it's within a activity period (start date - expiration date).
     */
    private boolean active;

    private String banner;

    @SerializedName("discount_amount")
    private Integer discountAmount;

    /**
     * Additional voucher's information
     */
    @SerializedName("additional_info")
    private String additionalInfo;

    private Map<String, Object> metadata;

    public String getId() {
        return id;
    }

    public String getCampaign() {
        return campaign;
    }

    public Discount getDiscount() {
        return discount;
    }

    public Gift getGift() {
        return gift;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public boolean isActive() {
        return active;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public String getBanner() {
        return banner;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }
}