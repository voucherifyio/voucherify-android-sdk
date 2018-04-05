package io.voucherify.android.client.api;

import java.util.Map;

import io.voucherify.android.client.model.VoucherRedemptionContext;
import io.voucherify.android.client.model.VoucherRedemptionResult;
import io.voucherify.android.client.model.VoucherResponse;
import io.voucherify.android.client.model.VouchersList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface VoucherifyApi {

    /**
     * Method used to validate a voucher identified by code
     * @param queryParams
     *          - code
     *          - amount
     *          - order items
     * @return
     */
    @GET("/client/v1/validate")
    Call<VoucherResponse> validateVoucher(@QueryMap Map<String, String> queryParams);

    /**
     * Method used to redeem a voucher identified by code
     * @param code
     *          code of the voucher
     * @param trackingId
     *          an id enabling you to correlate who has redeemed the voucher
     * @return
     */
    @POST("/client/v1/redeem")
    Call<VoucherRedemptionResult> redeemVoucher(@Query("code") String code,
                                                @Query("tracking_id") String trackingId);

    /**
     * Method used to redeem a voucher and provide a context information.
     *
     * @param code
     *          code of the voucher
     * @param redemptionContext
     *          a context in terms of which the voucher is being redeemed (e.g. customer profile)
     * @return redemption result (including redemption id)
     */
    @POST("/client/v1/redeem")
    Call<VoucherRedemptionResult> redeemVoucher(@Query("code") String code,
                                                @Body VoucherRedemptionContext redemptionContext);

    /**
     * Method used to list a vouchers
     * @param trackingId
     *          an id enabling you to correlate who has redeemed the voucher
     * @return
     */
    @GET("/client/v1/vouchers")
    Call<VouchersList> listVouchers(@Query("tracking_id") String trackingId);

    /**
     * Method used to validate promotion
     * @param queryParams
     *          - amount
     *          - order items
     *          - channel
     *          - tracking_id
     * @return
     */
    @GET("/client/v1/promotions/validation")
    Call<VoucherResponse> validatePromotion(@QueryMap Map<String, String> queryParams);

    /**
     * Method used to redeem promotion
     * @param tierId
     *          identifies your promotion campaign tier
     * @param trackingId
     *          an id enabling you to correlate who has redeemed the voucher
     * @return
     */
    @POST("/client/v1/promotions/tiers/{tierId}/redemption")
    Call<VoucherRedemptionResult> redeemPromotion(@Path("tierId") String tierId, @Query("tracking_id") String trackingId);

    /**
     * Method used to redeem promotion
     * @param tierId
     *          identifies your promotion campaign tier
     * @param redemptionContext
     *          a context in terms of which the promotion is being redeemed (e.g. customer profile)
     * @return redemption result (including redemption id)
     */
    @POST("/client/v1/promotions/tiers/{tierId}/redemption")
    Call<VoucherRedemptionResult> redeemPromotion(@Path("tierId") String tierId, @Body VoucherRedemptionContext redemptionContext);
}
