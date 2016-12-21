package io.voucherify.android.client.module;

/**
 * Vouchers Module to manage communication with Voucherify
 */
public final class VoucherModule {

    private Validation validation;
    private Redemption redemption;

    /**
     *
     * @param validation
     * @param redemption
     */
    public VoucherModule(Validation validation, Redemption redemption) {
        this.validation = validation;
        this.redemption = redemption;
    }

    public Validation validations() {
        return this.validation;
    }

    public Redemption redemptions() {
        return this.redemption;
    }

}
