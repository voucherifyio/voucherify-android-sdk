package pl.rspective.voucherify.android.view;

import pl.rspective.voucherify.android.client.model.VoucherResponse;
import retrofit.RetrofitError;

public interface OnValidatedListener {

    void onValidated(VoucherResponse result);

    void onError(RetrofitError error);
}
