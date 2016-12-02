package pl.rspective.voucherify.android.client.utils;

import pl.rspective.voucherify.android.client.callback.VoucherifyCallback;
import pl.rspective.voucherify.android.client.exception.VoucherifyError;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Utils's class used to wrap sync and async calls into RX world
 */
public final class RxUtils {

    /**
     * @param scheduler
     * @param observable
     * @param callback
     * @return
     */
    public static <T> VoucherifyCallback<T, VoucherifyError> subscribe(final Scheduler scheduler, Observable<T> observable, final VoucherifyCallback<T, VoucherifyError> callback) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(
                        new Action1<T>() {
                            @Override
                            public void call(final T t) {
                                callback.onSuccess(t);
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(final Throwable throwable) {
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
    public abstract static class DefFunc<T> implements Func0<Observable<T>> {
        @Override
        public final Observable<T> call() {
            try {
                return Observable.just(method());
            } catch (VoucherifyError error) {
                return Observable.error(error);
            }
        }

        public abstract T method();
    }

    public static <T> Observable<T> defer(DefFunc<T> func) {
        return Observable.defer(func).subscribeOn(Schedulers.io());
    }

}