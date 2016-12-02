package pl.rspective.voucherify.android.client.api;

import java.util.Map;

import pl.rspective.voucherify.android.client.model.VoucherRedemptionContext;
import pl.rspective.voucherify.android.client.model.VoucherRedemptionResult;
import pl.rspective.voucherify.android.client.model.VoucherResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

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
    Observable<VoucherResponse> validateVoucher(@QueryMap Map<String, String> queryParams);

    /**
     * Method used to redeem a voucher identified by code
     * @param code
     *          code of the voucher
     * @param trackingId
     *          an id enabling you to correlate who has redeemed the voucher
     * @return
     */
    @POST("/client/v1/redeem")
    Observable<VoucherRedemptionResult> redeemVoucher(@Query("code") String code, @Query("tracking_id") String trackingId);

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
    Observable<VoucherRedemptionResult> redeemVoucher(@Query("code") String code, @Body VoucherRedemptionContext redemptionContext);

}
