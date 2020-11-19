package io.voucherify.android.client.module;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.voucherify.android.client.api.VoucherifyApi;
import io.voucherify.android.client.callback.VoucherifyCallback;
import io.voucherify.android.client.exception.VoucherifyError;
import io.voucherify.android.client.model.VouchersList;
import io.voucherify.android.client.utils.RxUtils;

public class Listing extends AbsModule<Listing.ExtAsync, Listing.ExtRxJava> {

    /**
     *
     * @param api describes Voucherify REST API
     * @param scheduler of threads for current platform
     * @param trackingId custom tracking id to track voucher consumers
     */
    public Listing(VoucherifyApi api, Scheduler scheduler, String trackingId) {
        super(api, scheduler, trackingId);
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
        Map<String, String> queryParams = new LinkedHashMap<>();

        queryParams.put("tracking_id", trackingId);

        return api.listVouchers(queryParams).execute().body();
    }

    public VouchersList list(String customer) throws IOException {
        Map<String, String> queryParams = new LinkedHashMap<>();

        queryParams.put("tracking_id", trackingId);
        queryParams.put("customer", customer);

        return api.listVouchers(queryParams).execute().body();
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

        public Observable<VouchersList> list(final String customer) {
            return RxUtils.defer(new RxUtils.DefFunc<VouchersList>() {
                @Override
                public VouchersList method() throws IOException {
                    return Listing.this.list(customer);
                }
            });
        }
    }

    public class ExtAsync {
        public void list(VoucherifyCallback<VouchersList,
                                   VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().list(), callback);
        }

        public void list(String customer, VoucherifyCallback<VouchersList,
                VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().list(customer), callback);
        }
    }
}
