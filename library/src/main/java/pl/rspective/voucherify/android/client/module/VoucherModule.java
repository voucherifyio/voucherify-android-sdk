package pl.rspective.voucherify.android.client.module;

import java.util.concurrent.Executor;

import pl.rspective.voucherify.android.client.api.VoucherifyApi;
import pl.rspective.voucherify.android.client.model.VoucherResponse;

/**
 * Vouchers Module to manage communication with Voucherify
 */
public final class VoucherModule extends BaseModule<VoucherResponse> {

    /**
     *
     * @param api describes Voucherif REST API
     * @param executor of threads for current platform
     * @param trackingId custom tracking id to track voucher consumers
     */
    public VoucherModule(VoucherifyApi api, Executor executor, String trackingId) {
        super(api, executor, trackingId);
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