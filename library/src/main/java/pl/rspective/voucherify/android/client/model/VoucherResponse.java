package pl.rspective.voucherify.android.client.model;

public class VoucherResponse {

    private boolean valid;
    private String type;
    private double discount;

    public boolean isValid() {
        return valid;
    }

    public String getType() {
        return type;
    }

    public double getDiscount() {
        return discount;
    }
}
