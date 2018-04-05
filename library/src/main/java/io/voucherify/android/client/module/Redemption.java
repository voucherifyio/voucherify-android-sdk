package io.voucherify.android.client.module;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.voucherify.android.client.api.VoucherifyApi;
import io.voucherify.android.client.callback.VoucherifyCallback;
import io.voucherify.android.client.exception.VoucherifyError;
import io.voucherify.android.client.model.Tier;
import io.voucherify.android.client.model.VoucherRedemptionContext;
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

    public VoucherRedemptionResult redeem(Tier tier) throws IOException {
        return api.redeemPromotion(tier.getId(), trackingId).execute().body();
    }

    public VoucherRedemptionResult redeem(Tier tier, VoucherRedemptionContext redemptionContext) throws IOException {
        return api.redeemPromotion(tier.getId(), redemptionContext).execute().body();
    }

    public VoucherRedemptionResult redeem(String code) throws IOException {
        return api.redeemVoucher(code, trackingId).execute().body();
    }

    public VoucherRedemptionResult redeem(String code,
                                          VoucherRedemptionContext redemptionContext) throws IOException {
        return api.redeemVoucher(code, redemptionContext).execute().body();
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

        public Observable<VoucherRedemptionResult> redeem(final String code,
                                                          final VoucherRedemptionContext redemptionContext) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherRedemptionResult>() {
                @Override
                public VoucherRedemptionResult method() throws IOException {
                    return Redemption.this.redeem(code, redemptionContext);
                }
            });
        }

        public Observable<VoucherRedemptionResult> redeem(final Tier tier) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherRedemptionResult>() {
                @Override
                public VoucherRedemptionResult method() throws IOException {
                    return Redemption.this.redeem(tier);
                }
            });
        }

        public Observable<VoucherRedemptionResult> redeem(final Tier tier, final VoucherRedemptionContext redemptionContext) {
            return RxUtils.defer(new RxUtils.DefFunc<VoucherRedemptionResult>() {
                @Override
                public VoucherRedemptionResult method() throws IOException {
                    return Redemption.this.redeem(tier, redemptionContext);
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
                           final VoucherRedemptionContext redemptionContext,
                           VoucherifyCallback<VoucherRedemptionResult,
                                   VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().redeem(code, redemptionContext), callback);
        }

        public void redeem(Tier tier, VoucherifyCallback<VoucherRedemptionResult,
                                   VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().redeem(tier), callback);
        }

        public void redeem(Tier tier, final VoucherRedemptionContext redemptionContext,
                           VoucherifyCallback<VoucherRedemptionResult,
                                   VoucherifyError> callback) {
            RxUtils.subscribe(scheduler, rx().redeem(tier, redemptionContext), callback);
        }
    }
}
