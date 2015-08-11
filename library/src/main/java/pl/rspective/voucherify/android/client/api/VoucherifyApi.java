package pl.rspective.voucherify.android.client.api;

import pl.rspective.voucherify.android.client.model.VoucherResponse;
import retrofit.http.GET;
import retrofit.http.Query;

public interface VoucherifyApi {

    @GET("/validate")
    VoucherResponse validateVoucher(@Query("code") String code);

}
