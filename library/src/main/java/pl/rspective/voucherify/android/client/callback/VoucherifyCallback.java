package pl.rspective.voucherify.android.client.callback;

public abstract class VoucherifyCallback<T, E> {
    private boolean cancelled;

    public abstract void onSuccess(T result);

    public void onFailure(E error) {
    }

}