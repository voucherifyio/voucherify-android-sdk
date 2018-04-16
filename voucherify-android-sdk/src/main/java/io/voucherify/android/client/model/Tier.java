package io.voucherify.android.client.model;

public class Tier {

    public static Tier create(String id) {
        return new Tier(id);
    }
    private String id;

    public Tier(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
