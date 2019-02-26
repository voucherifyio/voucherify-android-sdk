package io.voucherify.android.client.module;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.voucherify.android.client.api.VoucherifyApi;
import io.voucherify.android.client.callback.VoucherifyCallback;
import io.voucherify.android.client.exception.VoucherifyError;
import io.voucherify.android.client.model.PromotionTierPage;
import io.voucherify.android.client.utils.RxUtils;

public class Promotion extends AbsModule<Promotion.ExtAsync, Promotion.ExtRxJava> {

    /**
     *
     * @param api describes Voucherify REST API
     * @param scheduler of threads for current platform
     * @param trackingId custom tracking id to track voucher consumers
     */
    public Promotion(VoucherifyApi api, Scheduler scheduler, String trackingId) {
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

    public PromotionTierPage list(Boolean isAvailable,
                                  int limit,
                                  int page) throws IOException {

        Map<String, String> queryParams = new LinkedHashMap<>();
        queryParams.put("is_available", String.valueOf(isAvailable));
        queryParams.put("limit", String.valueOf(limit));
        queryParams.put("page", String.valueOf(page));

        return api.listPromotions(queryParams).execute().body();
    }

    public class ExtRxJava {
        public Observable<PromotionTierPage> list(final Boolean isAvailable,
                                                  final int limit,
                                                  final int page) {
            return RxUtils.defer(new RxUtils.DefFunc<PromotionTierPage>() {
                @Override
                public PromotionTierPage method() throws IOException {
                    return Promotion.this.list(isAvailable, limit, page);
                }
            });
        }
    }

    public class ExtAsync {
        public void list(Boolean isAvailable,
                         int limit,
                         int page,
                         VoucherifyCallback<PromotionTierPage, VoucherifyError> callback) {

            RxUtils.subscribe(scheduler, rx().list(isAvailable, limit, page), callback);
        }
    }
}
