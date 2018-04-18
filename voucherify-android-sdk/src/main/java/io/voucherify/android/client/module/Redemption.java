package io.voucherify.android.client.module;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.voucherify.android.client.api.VoucherifyApi;
import io.voucherify.android.client.callback.VoucherifyCallback;
import io.voucherify.android.client.exception.VoucherifyError;
import io.voucherify.android.client.model.PromotionTier;
import io.voucherify.android.client.model.RedemptionContext;
import io.voucherify.android.client.model.VoucherRedemptionResult;
import io.voucherify.android.client.utils.RxUtils;

public class Redemption extends AbsModule<Redemption.ExtAsync, Redemption.ExtRxJava> {
    /**
     *
     * @param api describes Voucherify REST API
     * @param scheduler of threads for current platform
     * @param trackingId custom tracking id to track voucher consumers
     */
    public Redemption(VoucherifyApi api, Scheduler scheduler, String trackingId) {
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

    public VoucherRedemptionResult redeem(PromotionTier promotionTier, RedemptionContext redemptionContext)
            throws IOException {
        return api.redeemPromotion(promotionTier.getId(), redemptionContext, trackingId).execute().body();
    }

    public VoucherRedemptionResult redeem(String code) throws IOException {
        return api.redeemVoucher(code, trackingId).execute().body();
    }

    public VoucherRedemptionResult redeem(
            String code, RedemptionContext redemptionContext) throws IOException {
        return api.redeemVoucher(code, redemptionContext, trackingId).execute().body();
    }

    public class ExtRxJava {
        public Observable<VoucherRedemptionResult> redeem(final String code) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherRedemptionResult>() {
                @Override
                public VoucherRedemptionResult method() throws IOException {
                    return Redemption.this.redeem(code);
                }
            });
        }

        public Observable<VoucherRedemptionResult> redeem(
                final String code, final RedemptionContext redemptionContext) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherRedemptionResult>() {
                @Override
                public VoucherRedemptionResult method() throws IOException {
                    return Redemption.this.redeem(code, redemptionContext);
                }
            });
        }

        public Observable<VoucherRedemptionResult> redeem(
                final PromotionTier promotionTier, final RedemptionContext redemptionContext) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherRedemptionResult>() {
                @Override
                public VoucherRedemptionResult method() throws IOException {
                    return Redemption.this.redeem(promotionTier, redemptionContext);
                }
            });
        }
    }

    public class ExtAsync {
        public void redeem(String code,
                           VoucherifyCallback<VoucherRedemptionResult,
                                   VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().redeem(code), callback);
        }

        public void redeem(String code,
                           final RedemptionContext redemptionContext,
                           VoucherifyCallback<VoucherRedemptionResult,
                                   VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().redeem(code, redemptionContext), callback);
        }

        public void redeem(PromotionTier promotionTier, final RedemptionContext redemptionContext,
                           VoucherifyCallback<VoucherRedemptionResult,
                                   VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().redeem(promotionTier, redemptionContext), callback);
        }
    }
}
