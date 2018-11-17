package io.voucherify.android.client.utils;

import java.io.IOException;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.voucherify.android.client.callback.VoucherifyCallback;
import io.voucherify.android.client.exception.VoucherifyError;

/**
 * Utils's class used to wrap sync and async calls into RX world
 */
public final class RxUtils {

    private RxUtils() {
    }

    /**
     * @param scheduler
     * @param observable
     * @param callback
     * @return
     */
    public static <T> VoucherifyCallback<T, VoucherifyError> subscribe(
            final Scheduler scheduler,
            Observable<T> observable,
            final VoucherifyCallback<T, VoucherifyError> callback) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(
                        new Consumer<T>() {
                            @Override
                            public void accept(T t) throws Exception {
                                callback.onSuccess(t);
                            }

                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (throwable instanceof VoucherifyError) {
                                    callback.onFailure((VoucherifyError) throwable);
                                } else {
                                    callback.onFailure(new VoucherifyError(throwable));
                                }
                            }
                        });

        return callback;
    }

    /**
     * Convert method into observable
     * @param <T> represents return type of the wrapped method
     */
    public abstract static class DefFunc<T> implements Callable<Observable<T>> {
        @Override
        public final Observable<T> call() {
            try {
                return Observable.just(method());
            } catch (VoucherifyError error) {
                return Observable.error(error);
            } catch (IOException error) {
                return Observable.error(error);
            }
        }

        public abstract T method() throws IOException;
    }

    public static <T> Observable<T> defer(DefFunc<T> func) {
        return Observable.defer(func).subscribeOn(Schedulers.io());
    }

}