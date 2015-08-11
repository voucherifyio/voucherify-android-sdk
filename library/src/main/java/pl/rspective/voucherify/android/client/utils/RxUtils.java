package pl.rspective.voucherify.android.client.utils;

import java.util.concurrent.Executor;

import pl.rspective.voucherify.android.client.callback.VoucherifyCallback;
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
     * @param <R>
     * @return
     */
    public static <T, R> VoucherifyCallback<T, R> subscribe(final Executor executor, Observable<T> observable, final VoucherifyCallback<T, R> callback) {
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
                                        callback.onFailure((R)RetrofitError.unexpectedError("", throwable));
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
            return Observable.just(method());
        }

        public abstract T method();
    }

    public static <T> Observable<T> defer(DefFunc<T> func) {
        return Observable.defer(func).subscribeOn(Schedulers.io());
    }

}