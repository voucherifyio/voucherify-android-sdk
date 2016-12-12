package io.voucherify.android.client.module;

import io.reactivex.Scheduler;
import io.voucherify.android.client.api.VoucherifyApi;
import io.voucherify.android.client.model.VoucherRedemptionResult;
import io.voucherify.android.client.model.VoucherResponse;

/**
 * Vouchers Module to manage communication with Voucherify
 */
public final class VoucherModule extends BaseModule<VoucherResponse, VoucherRedemptionResult> {

    /**
     *
     * @param api describes Voucherif REST API
     * @param scheduler of threads for current platform
     * @param trackingId custom tracking id to track voucher consumers
     */
    public VoucherModule(VoucherifyApi api, Scheduler scheduler, String trackingId) {
        super(api, scheduler, trackingId);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public ExtAsync async() {
        return extAsync;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public ExtRxJava rx() {
        return extRxJava;
    }

}