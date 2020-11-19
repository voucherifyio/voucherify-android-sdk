package io.voucherify.android.client.utils;

import java.io.IOException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.schedulers.Schedulers;
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
    public abstract static class DefFunc<T> implements @NonNull Supplier<ObservableSource<? extends T>> {

        @Override
        public ObservableSource<? extends T> get() throws Throwable {
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