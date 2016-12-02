package pl.rspective.voucherify.android.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.rspective.voucherify.android.client.api.VoucherifyApi;
import pl.rspective.voucherify.android.client.module.VoucherModule;
import pl.rspective.voucherify.android.client.utils.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;

public class VoucherifyAndroidClient {

    private final String httpScheme;

    private VoucherModule voucherModule;

    private VoucherifyApi voucherifyApi;

    private Scheduler scheduler;

    private VoucherifyAndroidClient(Builder builder) {
        if (builder.clientToken.isEmpty() || builder.clientId.isEmpty() ) {
            throw new IllegalArgumentException("Client token and client id must be defined.");
        }

        this.httpScheme = createHttpScheme(builder);
        this.scheduler = createCallbackExecutor();

        this.voucherifyApi = createRetrofitService(builder);

        this.voucherModule = new VoucherModule(voucherifyApi, scheduler, builder.trackingId);
    }

    /**
     * Returns the Vouchers module.
     */
    public VoucherModule voucher() {
        return voucherModule;
    }

    /**
     *
     * @return system thread executor
     */
    private Scheduler createCallbackExecutor() {
        return Platform.get().callbackExecutor();
    }

    /**
     *
     * @return
     */
    private Gson createGson() {
        return new GsonBuilder().create();
    }

    /**
     *
     * @param builder
     * @return
     */
    private String createHttpScheme(Builder builder) {
        if (builder.secure) {
            return Constants.SCHEME_HTTPS;
        } else {
            return Constants.SCHEME_HTTP;
        }
    }

    /**
     *
     * @param builder
     * @return
     */
    private OkHttpClient createOkHttpClient(Builder builder) {
        return new OkHttpClient.Builder()
                .addInterceptor(createHeaderInterceptor(builder.clientId, builder.clientToken, builder.origin))
                .addInterceptor(createLoggerInterceptor(builder.logLevel))
                .build();
    }

    /**
     *
     * @param builder
     * @return
     */
    private VoucherifyApi createRetrofitService(Builder builder) {
        String endpoint;

        if (builder.endpoint == null) {
            endpoint = Constants.ENDPOINT_VOUCHERIFY;
        } else {
            endpoint = builder.endpoint;
        }

        Retrofit.Builder restBuilder =
                new Retrofit.Builder()
                        .baseUrl(String.format("%s://%s", httpScheme, endpoint))
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(createOkHttpClient(builder));
        return restBuilder.build().create(VoucherifyApi.class);
    }

    /**
     *
     * @param clientId
     * @param clientToken
     * @return
     */
    private Interceptor createHeaderInterceptor(final String clientId, final String clientToken, final String origin) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader(Constants.HTTP_HEADER_CLIENT_ID, clientId);
                builder.addHeader(Constants.HTTP_HEADER_CLIENT_TOKEN, clientToken);
                builder.addHeader(Constants.HTTP_HEADER_ORIGIN, origin == null || origin.isEmpty() ? Constants.VOUCHERIFY_DEFAULT_ORIGIN : origin);
                builder.addHeader(Constants.HTTP_HEADER_VOUCHERIFY_CHANNEL, Constants.VOUCHERIFY_CHANNEL_NAME);
                return chain.proceed(builder.build());
            }
        };
    }

    /**
     *
     * @param logLevel
     * @return
     */
    private HttpLoggingInterceptor createLoggerInterceptor(HttpLoggingInterceptor.Level logLevel) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (logLevel != null) {
            interceptor.setLevel(logLevel);
        }
        return interceptor;
    }

    public static class Builder {
        String clientToken;
        String clientId;
        String trackingId;

        String origin;
        String endpoint;

        boolean secure;

        HttpLoggingInterceptor.Level logLevel;

        public Builder(String clientId, String clientToken) {
            if (clientId == null || clientToken == null) {
                throw new IllegalArgumentException("ClientToken or clientId is null");
            }

            this.clientToken = clientToken;
            this.clientId = clientId;
            this.secure = true;
        }

        public Builder setLogLevel(HttpLoggingInterceptor.Level logLevel) {
            if (logLevel == null) {
                throw new IllegalArgumentException("Cannot call setLogLevel() with null.");
            }

            this.logLevel = logLevel;
            return this;
        }

        public Builder withOrigin(String origin) {
            this.origin = origin;
            return this;
        }

        public Builder withSSL() {
            this.secure = true;
            return this;
        }

        public Builder withoutSSL() {
            this.secure = false;
            return this;
        }

        public Builder withCustomTrackingId(String trackingId) {
            this.trackingId = trackingId;
            return this;
        }

        public VoucherifyAndroidClient build() {
            return new VoucherifyAndroidClient(this);
        }

    }

}
