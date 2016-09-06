package pl.rspective.voucherify.android.client.api;

import java.util.Map;

import pl.rspective.voucherify.android.client.exception.VoucherifyError;
import pl.rspective.voucherify.android.client.model.VoucherResponse;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface VoucherifyApi {

    @GET("/client/v1/validate")
    VoucherResponse validateVoucher(@QueryMap Map<String, String> queryParams);

}
