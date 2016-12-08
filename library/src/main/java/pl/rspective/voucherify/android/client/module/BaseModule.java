package pl.rspective.voucherify.android.client.module;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.rspective.voucherify.android.client.api.VoucherifyApi;
import pl.rspective.voucherify.android.client.callback.VoucherifyCallback;
import pl.rspective.voucherify.android.client.exception.VoucherifyError;
import pl.rspective.voucherify.android.client.model.OrderItem;
import pl.rspective.voucherify.android.client.model.VoucherRedemptionContext;
import pl.rspective.voucherify.android.client.model.VoucherRedemptionResult;
import pl.rspective.voucherify.android.client.model.VoucherResponse;
import pl.rspective.voucherify.android.client.utils.RxUtils;
import rx.Observable;
import rx.Scheduler;

abstract class BaseModule<T, U> extends AbsModule<BaseModule.ExtAsync, BaseModule.ExtRxJava> {

    private static final String CHANNEL = "android";

    private final String trackingId;

    public BaseModule(VoucherifyApi api, Scheduler scheduler, String trackingId) {
        super(api, scheduler);

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

    public T validate(String code) throws IOException {
        return validate(code, null);
    }

    public T validate(String code, Integer amount) throws IOException {
        return validate(code, amount, null);
    }

    public T validate(String code, Integer amount, List<OrderItem> orderItems) throws IOException {
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
        return (T) api.validateVoucher(queryParams).execute().body();
    }

    public U redeemVoucher(String code) throws IOException {
        return (U) api.redeemVoucher(code, trackingId).execute().body();
    }

    public U redeemVoucher(String code, VoucherRedemptionContext redemptionContext) throws IOException {
        return (U) api.redeemVoucher(code, redemptionContext).execute().body();
    }

    /**
     * Base Async extension.
     */
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

        public void redeemVoucher(String code, VoucherifyCallback<VoucherRedemptionResult, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().redeemVoucher(code), callback);
        }

        public void redeemVoucher(String code, final VoucherRedemptionContext redemptionContext, VoucherifyCallback<VoucherRedemptionResult,VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().redeemVoucher(code, redemptionContext), callback);
        }

    }

    public class ExtRxJava extends Rx {

        public Observable<T> validate(final String code) {
            return RxUtils.defer(new RxUtils.DefFunc<T>() {
                @Override
                public T method() throws IOException {
                    return BaseModule.this.validate(code);
                }
            });
        }

        public Observable<T> validate(final String code, final Integer amount) {
            return RxUtils.defer(new RxUtils.DefFunc<T>() {
                @Override
                public T method() throws IOException {
                    return BaseModule.this.validate(code, amount);
                }
            });
        }

        public Observable<T> validate(final String code, final Integer amount, final List<OrderItem> orderItems) {
            return RxUtils.defer(new RxUtils.DefFunc<T>() {
                @Override
                public T method() throws IOException {
                    return BaseModule.this.validate(code, amount, orderItems);
                }
            });
        }

        public Observable<U> redeemVoucher(final String code) {
            return RxUtils.defer(new RxUtils.DefFunc<U>() {
                @Override
                public U method() throws IOException {
                    return BaseModule.this.redeemVoucher(code);
                }
            });
        }

        public Observable<U> redeemVoucher(final String code, final VoucherRedemptionContext redemptionContext) {
            return RxUtils.defer(new RxUtils.DefFunc<U>() {
                @Override
                public U method() throws IOException {
                    return BaseModule.this.redeemVoucher(code, redemptionContext);
                }
            });
        }

    }

    public class Rx {}

    public class Async {}

}