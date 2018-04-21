package io.voucherify.android.client.model;

public class PromotionTier {

    public static PromotionTier create(String id) {
        return new PromotionTier(id);
    }
    private String id;

    public PromotionTier(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
