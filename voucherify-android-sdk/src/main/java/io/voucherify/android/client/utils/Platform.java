package io.voucherify.android.client.utils;

import java.util.concurrent.Executor;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;

/**
 * Util class to get information about current system platform.
 */
public abstract class Platform {
    /**
     * Current system platfom on which client is running
     */
    private static final Platform PLATFORM = findPlatform();

    /**
     *
     * @return current system platform
     */
    public static Platform get() {
        return PLATFORM;
    }

    /**
     *
     * @return
     */
    private static Platform findPlatform() {
        try {
            Class.forName("android.os.Build");
            return new Android();
        } catch (ClassNotFoundException ignored) {
            throw new RuntimeException("SDK is for Android platform");
        }
    }

    /**
     *
     * @return
     */
    public abstract Scheduler callbackExecutor();

    private static class Android extends Platform {
        /**
         *
         * @return
         */
        @Override
        public Scheduler callbackExecutor() {
            return AndroidSchedulers.mainThread();
        }
    }

    /**
     *
     */
    static class SynchronousExecutor implements Executor {
        /**
         *
         * @param runnable
         */
        @Override
        public void execute(Runnable runnable) {
            runnable.run();
        }
    }
}