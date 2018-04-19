package io.voucherify.android.client.module;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.voucherify.android.client.api.VoucherifyApi;
import io.voucherify.android.client.callback.VoucherifyCallback;
import io.voucherify.android.client.exception.VoucherifyError;
import io.voucherify.android.client.model.PromotionResponse;
import io.voucherify.android.client.model.ValidationContext;
import io.voucherify.android.client.model.VoucherResponse;
import io.voucherify.android.client.utils.RxUtils;

public class Validation extends AbsModule<Validation.ExtAsync, Validation.ExtRxJava> {

    private static final String CHANNEL = "android";

    public Validation(VoucherifyApi api, Scheduler scheduler, String trackingId) {
        super(api, scheduler, trackingId);
    }

    public PromotionResponse validate(ValidationContext context) throws IOException {
        Map<String, String> queryParams = createQueryParamsForContext(context);
        return api.validatePromotion(queryParams).execute().body();
    }

    public VoucherResponse validate(String code) throws IOException {
        return validate(code, null);
    }

    public VoucherResponse validate(String code, ValidationContext context) throws IOException {
        Map<String, String> queryParams = createQueryParamsForContext(context);
        queryParams.put("code", code);
        return api.validateVoucher(queryParams).execute().body();
    }

    private Map<String, String> createQueryParamsForContext(ValidationContext context) {
        Map<String, String> queryParams = new LinkedHashMap<>();

        queryParams.put("channel", CHANNEL);
        if (trackingId != null) {
            queryParams.put("tracking_id", trackingId);
        }

        if (context != null) {
            context.createQuery(queryParams);
        }

        Collection<String> values = queryParams.values();
        values.removeAll(Collections.singleton(null));
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


        public Observable<VoucherResponse> validate(final String code) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherResponse>() {
                @Override
                public VoucherResponse method() throws IOException {
                    return Validation.this.validate(code);
                }
            });
        }

        public Observable<VoucherResponse> validate(final String code,
                                                    final ValidationContext context) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherResponse>() {
                @Override
                public VoucherResponse method() throws IOException {
                    return Validation.this.validate(code, context);
                }
            });
        }

        public Observable<PromotionResponse> validate(final ValidationContext context) {
            return RxUtils.defer(new RxUtils.DefFunc<PromotionResponse>() {
                @Override
                public PromotionResponse method() throws IOException {
                    return Validation.this.validate(context);
                }
            });
        }
    }

    public class ExtAsync {

        public void validate(String code, VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validate(code), callback);
        }

        public void validate(
                String code,
                ValidationContext context,
                VoucherifyCallback<VoucherResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validate(code, context), callback);
        }


        public void validate(
                ValidationContext context,
                VoucherifyCallback<PromotionResponse, VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().validate(context), callback);
        }
    }

}
