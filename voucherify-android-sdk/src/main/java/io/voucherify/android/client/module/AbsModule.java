package io.voucherify.android.client.module;

import io.reactivex.rxjava3.core.Scheduler;
import io.voucherify.android.client.api.VoucherifyApi;

abstract class AbsModule<A, R> {
    /**
     * Represents platform thread scheduler
     */
    final Scheduler scheduler;

    /**
     * Describes REST API for voucherify
     */
    final VoucherifyApi api;

    final String trackingId;

    /**
     * The type of object used to do async calls
     */
    final A extAsync;

    /**
     * The type of object used to do rx calls
     */
    final R extRxJava;

    /**
     *
     * @param api describes Voucherif REST API
     * @param scheduler of threads for current platform
     */
    AbsModule(VoucherifyApi api, Scheduler scheduler, String trackingId) {
        this.api = api;
        this.scheduler = scheduler;
        this.trackingId = trackingId;

        this.extAsync = createAsyncExtension();
        this.extRxJava = createRxJavaExtension();
    }

    abstract A createAsyncExtension();

    abstract R createRxJavaExtension();

    /**
     * Returns the asynchronous extension of this module.
     */
    public A async() {
        return extAsync;
    }

    /**
     * Returns the RxJava extension of this module.
     */
    public R rx() {
        return extRxJava;
    }
}
