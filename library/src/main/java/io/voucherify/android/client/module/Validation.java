package io.voucherify.android.client.module;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.voucherify.android.client.api.VoucherifyApi;
import io.voucherify.android.client.callback.VoucherifyCallback;
import io.voucherify.android.client.exception.VoucherifyError;
import io.voucherify.android.client.model.OrderItem;
import io.voucherify.android.client.model.VoucherResponse;
import io.voucherify.android.client.utils.RxUtils;

public class Validation extends AbsModule<Validation.ExtAsync, Validation.ExtRxJava> {
    private final VoucherifyApi api;

    private static final String CHANNEL = "android";

    private final String trackingId;

    private final Scheduler scheduler;

    /**
     *
     * @param api describes Voucherif REST API
     * @param scheduler of threads for current platform
     * @param trackingId custom tracking id to track voucher consumers
     */
    public Validation(VoucherifyApi api, Scheduler scheduler, String trackingId) {
        super(api, scheduler);
        this.api = api;
        this.scheduler = scheduler;
        this.trackingId = trackingId;
    }

    public VoucherResponse validateVoucher(String code) throws IOException {
        return validateVoucher(code, null);
    }

    public VoucherResponse validateVoucher(String code, Integer amount) throws IOException {
        return validateVoucher(code, amount, null);
    }

    public VoucherResponse validateVoucher(String code, Integer amount, List<OrderItem> orderItems) throws IOException {
        Map<String, String> queryParams = new LinkedHashMap<>();
        queryParams.put("code", code);
        if (trackingId != null) {
            queryParams.put("tracking_id", trackingId);
        }
        if (amount != null) {
            queryParams.put("amount", amount.toString());
        }
        if (orderItems != null) {
            int index = 0;
            for (OrderItem item : orderItems) {
                queryParams.put("item[" + index + "][product_id]", item.getProductId());
                queryParams.put("item[" + index + "][sku_id]", item.getSkuId());
                queryParams.put("item[" + index + "][quantity]",
                        item.getQuantity() != null ? item.getQuantity().toString() : null);
                index++;
            }
        }
        queryParams.put("channel", CHANNEL);
        return api.validateVoucher(queryParams).execute().body();
    }

    @Override
    ExtAsync createAsyncExtension() {
        return new ExtAsync();
    }

    @Override
    ExtRxJava createRxJavaExtension() {
        return new ExtRxJava();
    }

    public class ExtRxJava {
        public Observable<VoucherResponse> validateVoucher(final String code) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherResponse>() {
                @Override
                public VoucherResponse method() throws IOException {
                    return Validation.this.validateVoucher(code);
                }
            });
        }

        public Observable<VoucherResponse> validateVoucher(final String code, final Integer amount) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherResponse>() {
                @Override
                public VoucherResponse method() throws IOException {
                    return Validation.this.validateVoucher(code, amount);
                }
            });
        }

        public Observable<VoucherResponse> validateVoucher(final String code,
                                                           final Integer amount,
                                                           final List<OrderItem> orderItems) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherResponse>() {
                @Override
                public VoucherResponse method() throws IOException {
                    return Validation.this.validateVoucher(code, amount, orderItems);
                }
            });
        }
    }

    public class ExtAsync {
        public void validateVoucher(String code, VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validateVoucher(code), callback);
        }

        public void validateVoucher(String code,
                                    Integer amount,
                                    VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validateVoucher(code, amount), callback);
        }

        public void validateVoucher(String code,
                                    Integer amount,
                                    List<OrderItem> orderItems,
                                    VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validateVoucher(code, amount, orderItems), callback);
        }
    }

}
