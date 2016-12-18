package io.voucherify.android.client.module;

import io.reactivex.Scheduler;
import io.voucherify.android.client.api.VoucherifyApi;

/**
 * Vouchers Module to manage communication with Voucherify
 */
public final class VoucherModule {

    private Validation validation;
    private Redemption redemption;

    /**
     *
     * @param api describes Voucherif REST API
     * @param scheduler of threads for current platform
     * @param trackingId custom tracking id to track voucher consumers
     */
    public VoucherModule(VoucherifyApi api, Scheduler scheduler, String trackingId) {
        this.validation = new Validation(api, scheduler, trackingId);
        this.redemption = new Redemption(api, scheduler, trackingId);
    }

    public Validation validations() {
        return this.validation;
    }

    public Redemption redemptions() {
        return this.redemption;
    }

}
