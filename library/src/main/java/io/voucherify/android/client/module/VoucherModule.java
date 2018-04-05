package io.voucherify.android.client.module;

/**
 * Vouchers Module to manage communication with Voucherify
 */
public final class VoucherModule {

    private Validation validation;
    private Redemption redemption;
    private Listing listing;

    /**
     *
     * @param validation
     * @param redemption
     * @param listing
     */
    public VoucherModule(Validation validation, Redemption redemption, Listing listing) {
        this.validation = validation;
        this.redemption = redemption;
        this.listing = listing;
    }

    public Validation validations() {
        return this.validation;
    }

    public Redemption redemptions() {
        return this.redemption;
    }

    public Listing listing() {
        return this.listing;
    }

}
