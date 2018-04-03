package io.voucherify.android.client.module;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.voucherify.android.client.api.VoucherifyApi;
import io.voucherify.android.client.callback.VoucherifyCallback;
import io.voucherify.android.client.exception.VoucherifyError;
import io.voucherify.android.client.model.VouchersList;
import io.voucherify.android.client.utils.RxUtils;

public class Listing extends AbsModule<Listing.ExtAsync, Listing.ExtRxJava> {

    private final VoucherifyApi api;

    private final Scheduler scheduler;

    private final String trackingId;

    /**
     *
     * @param api describes Voucherify REST API
     * @param scheduler of threads for current platform
     * @param trackingId custom tracking id to track voucher consumers
     */
    public Listing(VoucherifyApi api, Scheduler scheduler, String trackingId) {
        super(api, scheduler);
        this.api = api;
        this.scheduler = scheduler;
        this.trackingId = trackingId;
    }

    @Override
    ExtAsync createAsyncExtension() {
        return new ExtAsync();
    }

    @Override
    ExtRxJava createRxJavaExtension() {
        return new ExtRxJava();
    }

    public VouchersList list() throws IOException {
        return api.listVouchers(trackingId).execute().body();
    }

    public class ExtRxJava {
        public Observable<VouchersList> list() {
            return RxUtils.defer(new RxUtils.DefFunc<VouchersList>() {
                @Override
                public VouchersList method() throws IOException {
                    return Listing.this.list();
                }
            });
        }
    }

    public class ExtAsync {
        public void list(VoucherifyCallback<VouchersList,
                                   VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().list(), callback);
        }
    }
}
