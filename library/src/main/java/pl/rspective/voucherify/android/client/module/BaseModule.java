package pl.rspective.voucherify.android.client.module;

import java.util.concurrent.Executor;

import pl.rspective.voucherify.android.client.api.VoucherifyApi;
import pl.rspective.voucherify.android.client.callback.VoucherifyCallback;
import pl.rspective.voucherify.android.client.model.VoucherResponse;
import pl.rspective.voucherify.android.client.utils.RxUtils;
import retrofit.RetrofitError;
import rx.Observable;

abstract class BaseModule<T> extends AbsModule<BaseModule.ExtAsync, BaseModule.ExtRxJava> {

    public BaseModule(VoucherifyApi api, Executor executor) {
        super(api, executor);
    }

    @Override
    ExtAsync createAsyncExtension() {
        return new ExtAsync();
    }

    @Override
    ExtRxJava createRxJavaExtension() {
        return new ExtRxJava();
    }

    public T validate(String code) {
        return (T) api.validateVoucher(code);
    }

    /**
     * Base Async extension.
     */
    public class ExtAsync extends Async {

        public void validate(String code, VoucherifyCallback<VoucherResponse, RetrofitError> callback) {
            RxUtils.subscribe(executor, rx().validate(code), callback);
        }

    }

    /**
     * Base RxJava extension.
     */
    public class ExtRxJava extends Rx {

        public Observable<T> validate(final String code) {
            return RxUtils.defer(new RxUtils.DefFunc<T>() {
                @Override
                public T method() {
                    BaseModule.this.validate(code);
                    return null;
                }
            });
        }
    }

    public class Rx {}

    public class Async {}

}