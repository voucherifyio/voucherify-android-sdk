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

    private static final String CHANNEL = "android";

    /**
     *
     * @param api describes Voucherify REST API
     * @param scheduler of threads for current platform
     * @param trackingId custom tracking id to track voucher consumers
     */
    public Validation(VoucherifyApi api, Scheduler scheduler, String trackingId) {
        super(api, scheduler, trackingId);
    }

    public VoucherResponse validate(Integer amount) throws IOException {
        return validate(amount, null);
    }

    public VoucherResponse validate(Integer amount, List<OrderItem> orderItems) throws IOException {
        Map<String, String> queryParams = createQueryParamsForOrderItems(amount, orderItems);
        return api.validatePromotion(queryParams).execute().body();
    }

    public VoucherResponse validate(String code) throws IOException {
        return validate(code, null);
    }

    public VoucherResponse validate(String code, Integer amount) throws IOException {
        return validate(code, amount, null);
    }

    public VoucherResponse validate(String code, Integer amount, List<OrderItem> orderItems) throws IOException {
        Map<String, String> queryParams = createQueryParamsForOrderItems(amount, orderItems);
        queryParams.put("code", code);
        return api.validateVoucher(queryParams).execute().body();
    }

    @Deprecated
    public VoucherResponse validateVoucher(String code) throws IOException {
        return validate(code, null);
    }

    @Deprecated
    public VoucherResponse validateVoucher(String code, Integer amount) throws IOException {
        return validate(code, amount, null);
    }

    @Deprecated
    public VoucherResponse validateVoucher(String code, Integer amount, List<OrderItem> orderItems) throws IOException {
        Map<String, String> queryParams = createQueryParamsForOrderItems(amount, orderItems);
        queryParams.put("code", code);
        return api.validateVoucher(queryParams).execute().body();
    }

    private Map<String, String> createQueryParamsForOrderItems(Integer amount, List<OrderItem> orderItems) {
        Map<String, String> queryParams = new LinkedHashMap<>();
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
                queryParams.put("item[" + index + "][price]",
                        item.getPrice() != null ? item.getPrice().toString() : null);
                queryParams.put("item[" + index + "][quantity]",
                        item.getQuantity() != null ? item.getQuantity().toString() : null);
                index++;
            }
        }
        queryParams.put("channel", CHANNEL);

        return queryParams;
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

        @Deprecated
        public Observable<VoucherResponse> validateVoucher(final String code) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherResponse>() {
                @Override
                public VoucherResponse method() throws IOException {
                    return Validation.this.validate(code);
                }
            });
        }

        @Deprecated
        public Observable<VoucherResponse> validateVoucher(final String code, final Integer amount) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherResponse>() {
                @Override
                public VoucherResponse method() throws IOException {
                    return Validation.this.validate(code, amount);
                }
            });
        }

        @Deprecated
        public Observable<VoucherResponse> validateVoucher(final String code,
                                                    final Integer amount,
                                                    final List<OrderItem> orderItems) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherResponse>() {
                @Override
                public VoucherResponse method() throws IOException {
                    return Validation.this.validate(code, amount, orderItems);
                }
            });
        }

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

        public Observable<VoucherResponse> validate(final String code,
                                                           final Integer amount,
                                                           final List<OrderItem> orderItems) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherResponse>() {
                @Override
                public VoucherResponse method() throws IOException {
                    return Validation.this.validate(code, amount, orderItems);
                }
            });
        }

        public Observable<VoucherResponse> validate(final Integer amount) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherResponse>() {
                @Override
                public VoucherResponse method() throws IOException {
                    return Validation.this.validate(amount);
                }
            });
        }

        public Observable<VoucherResponse> validate(final Integer amount,
                                                    final List<OrderItem> orderItems) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherResponse>() {
                @Override
                public VoucherResponse method() throws IOException {
                    return Validation.this.validate(amount, orderItems);
                }
            });
        }
    }

    public class ExtAsync {
        @Deprecated
        public void validateVoucher(String code, VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validate(code), callback);
        }

        @Deprecated
        public void validateVoucher(String code,
                             Integer amount,
                             VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validate(code, amount), callback);
        }

        @Deprecated
        public void validateVoucher(String code,
                             Integer amount,
                             List<OrderItem> orderItems,
                             VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validate(code, amount, orderItems), callback);
        }

        public void validate(String code, VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validate(code), callback);
        }

        public void validate(String code,
                                    Integer amount,
                                    VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validate(code, amount), callback);
        }

        public void validate(String code,
                                    Integer amount,
                                    List<OrderItem> orderItems,
                                    VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validate(code, amount, orderItems), callback);
        }

        public void validate(
                Integer amount,
                VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validate(amount), callback);
        }

        public void validate(
                             Integer amount,
                             List<OrderItem> orderItems,
                             VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validate(amount, orderItems), callback);
        }
    }

}
