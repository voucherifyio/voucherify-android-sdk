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

    Validation(VoucherifyApi api, Scheduler scheduler, String trackingId) {
        super(api, scheduler);
        this.api = api;
        this.scheduler = scheduler;
        this.trackingId = trackingId;
    }

    public VoucherResponse validate(String code) throws IOException {
        return validate(code, null);
    }

    public VoucherResponse validate(String code, Integer amount) throws IOException {
        return validate(code, amount, null);
    }

    public VoucherResponse validate(String code, Integer amount, List<OrderItem> orderItems) throws IOException {
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
                queryParams.put("item[" + index + "][quantity]", item.getQuantity() != null ? item.getQuantity().toString() : null);
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

    public class ExtAsync extends Async {

        public void validate(String code, VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validate(code), callback);
        }

        public void validate(String code, Integer amount, VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validate(code, amount), callback);
        }

        public void validate(String code, Integer amount, List<OrderItem> orderItems, VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validate(code, amount, orderItems), callback);
        }

    }

    class ExtRxJava extends Rx {

        public Observable<VoucherResponse> validate(final String code) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherResponse>() {
                @Override
                public VoucherResponse method() throws IOException {
                    return Validation.this.validate(code);
                }
            });
        }

        public Observable<VoucherResponse> validate(final String code, final Integer amount) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherResponse>() {
                @Override
                public VoucherResponse method() throws IOException {
                    return Validation.this.validate(code, amount);
                }
            });
        }

        public Observable<VoucherResponse> validate(final String code, final Integer amount, final List<OrderItem> orderItems) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherResponse>() {
                @Override
                public VoucherResponse method() throws IOException {
                    return Validation.this.validate(code, amount, orderItems);
                }
            });
        }

    }

    class Rx {}

    class Async {}

}
