package pl.rspective.voucherify.android.client.api;

import pl.rspective.voucherify.android.client.model.VoucherResponse;
import retrofit.http.GET;
import retrofit.http.Query;

public interface VoucherifyApi {

    @GET("/client/v1/validate")
    VoucherResponse validateVoucher(@Query("code") String code, @Query("tracking_id") String trackingId, @Query("channel") String channel);

}
