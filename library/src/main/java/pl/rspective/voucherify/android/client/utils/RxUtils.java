package pl.rspective.voucherify.android.client.utils;

import java.util.concurrent.Executor;

import pl.rspective.voucherify.android.client.callback.VoucherifyCallback;
import pl.rspective.voucherify.android.client.exception.VoucherifyError;
import retrofit.RetrofitError;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Utils's class used to wrap sync and async calls into RX world
 */
public final class RxUtils {

    /**
     *
     * @param executor
     * @param observable
     * @param callback
     * @return
     */
    public static <T> VoucherifyCallback<T, VoucherifyError> subscribe(final Executor executor, Observable<T> observable, final VoucherifyCallback<T, VoucherifyError> callback) {
        observable
                .subscribe(
                        new Action1<T>() {
                            @Override
                            public void call(final T t) {
                                executor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(t);
                                    }
                                });
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(final Throwable throwable) {
                                executor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (throwable instanceof VoucherifyError) {
                                            callback.onFailure((VoucherifyError) throwable);
                                        } else {
                                            callback.onFailure(new VoucherifyError(throwable));
                                        }
                                  }
                                });
                            }
                        });

        return callback;
    }

    /**
     * Convert method into observable
     * @param <T> represents return type of the wrapped method
     */
    public abstract static class DefFunc<T> implements Func0<Observable<T>> {
        @Override
        public final Observable<T> call() {
            try {
                return Observable.just(method());
            } catch (VoucherifyError error) {
                return Observable.error(error);
            }
        }

        public abstract T method() throws VoucherifyError;
    }

    public static <T> Observable<T> defer(DefFunc<T> func) {
        return Observable.defer(func).subscribeOn(Schedulers.io());
    }

}