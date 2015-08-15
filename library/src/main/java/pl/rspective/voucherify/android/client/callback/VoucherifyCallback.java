package pl.rspective.voucherify.android.client.callback;

public interface VoucherifyCallback<T, E> {

    void onSuccess(T result);

    void onFailure(E error);

}