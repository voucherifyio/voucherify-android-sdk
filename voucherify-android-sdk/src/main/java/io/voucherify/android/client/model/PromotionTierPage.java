package io.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromotionTierPage {

    @SerializedName("data_ref")
    private String dataRef;

    private String object;

    @SerializedName("has_more")
    private Boolean hasMore;

    private List<PromotionTier> tiers;

    public String getDataRef() {
        return dataRef;
    }

    public String getObject() {
        return object;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public List<PromotionTier> getTiers() {
        return tiers;
    }
}
