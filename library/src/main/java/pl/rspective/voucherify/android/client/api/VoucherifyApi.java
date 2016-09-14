package pl.rspective.voucherify.android.client.api;

import java.util.Map;

import pl.rspective.voucherify.android.client.model.VoucherRedemptionResult;
import pl.rspective.voucherify.android.client.model.VoucherResponse;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface VoucherifyApi {

    @GET("/client/v1/validate")
    VoucherResponse validateVoucher(@QueryMap Map<String, String> queryParams);

    @POST("/client/v1/redeem")
    VoucherRedemptionResult redeemVoucher(@Query("code") String code, @Query("tracking_id") String trackingId);

}
