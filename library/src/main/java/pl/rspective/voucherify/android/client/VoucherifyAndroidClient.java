package pl.rspective.voucherify.android.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;

import pl.rspective.voucherify.android.client.api.VoucherifyApi;
import pl.rspective.voucherify.android.client.module.VoucherModule;
import pl.rspective.voucherify.android.client.utils.Platform;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.converter.GsonConverter;

public class VoucherifyAndroidClient {

    private final String httpScheme;

    private Gson gson;

    private VoucherModule voucherModule;

    private VoucherifyApi voucherifyApi;

    private Executor executor;

    private VoucherifyAndroidClient(Builder builder) {
        if (builder.clientToken.isEmpty() || builder.clientId.isEmpty() ) {
            throw new IllegalArgumentException("Client token and client id must be defined.");
        }

        this.httpScheme = createHttpScheme(builder);
        this.executor = createCallbackExecutor();

        this.gson = createGson();
        this.voucherifyApi = createRetrofitService(builder);

        this.voucherModule = new VoucherModule(voucherifyApi, executor, builder.trackingId);
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
    private Executor createCallbackExecutor() {
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
    private VoucherifyApi createRetrofitService(Builder builder) {
        RestAdapter.Builder restBuilder =
                new RestAdapter.Builder()
                        .setConverter(new GsonConverter(gson))
                        .setRequestInterceptor(createInterceptor(builder.clientId, builder.clientToken, builder.origin));

        setEndPoint(builder, restBuilder);
        setClientProvider(builder, restBuilder);
        setLogLevel(builder, restBuilder);

        return restBuilder.build().create(VoucherifyApi.class);
    }

    /**
     *
     * @param clientId
     * @param clientToken
     * @return
     */
    private RequestInterceptor createInterceptor(final String clientId, final String clientToken, final String origin) {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader(Constants.HTTP_HEADER_CLIENT_ID, clientId);
                request.addHeader(Constants.HTTP_HEADER_CLIENT_TOKEN, clientToken);
                request.addHeader(Constants.HTTP_HEADER_ORIGIN, origin == null || origin.isEmpty() ? Constants.VOUCHERIFY_DEFAULT_ORIGIN : origin);
                request.addHeader(Constants.HTTP_HEADER_VOUCHERIFY_CHANNEL, Constants.VOUCHERIFY_CHANNEL_NAME);
            }
        };
    }

    /**
     *
     * @param builder
     * @param restBuilder
     */
    private void setLogLevel(Builder builder, RestAdapter.Builder restBuilder) {
        if (builder.logLevel != null) {
            restBuilder.setLogLevel(builder.logLevel);
        }
    }

    /**
     *
     * @param builder
     * @param restBuilder
     */
    private void setClientProvider(Builder builder, RestAdapter.Builder restBuilder) {
        if (builder.clientProvider != null) {
            restBuilder.setClient(builder.clientProvider);
        }
    }

    /**
     *
     * @param builder
     * @param restBuilder
     */
    private void setEndPoint(Builder builder, RestAdapter.Builder restBuilder) {
        String endpoint;

        if (builder.endpoint == null) {
            endpoint = Constants.ENDPOINT_VOUCHERIFY;
        } else {
            endpoint = builder.endpoint;
        }

        restBuilder.setEndpoint(String.format("%s://%s", httpScheme, endpoint));
    }

    public static class Builder {
        String clientToken;
        String clientId;
        String trackingId;

        String origin;
        String endpoint;

        boolean secure;

        RestAdapter.LogLevel logLevel;

        Client.Provider clientProvider;

        public Builder(String clientId, String clientToken) {
            if (clientId == null || clientToken == null) {
                throw new IllegalArgumentException("ClientToken or clientId is null");
            }

            this.clientToken = clientToken;
            this.clientId = clientId;
            this.secure = false;
        }

        public Builder setClient(final Client client) {
            if (client == null) {
                throw new IllegalArgumentException("Cannot call setClient() with null.");
            }

            return setClientProvider(new Client.Provider() {
                @Override
                public Client get() {
                    return client;
                }
            });
        }

        public Builder setEndpoint(String remoteUrl) {
            if (remoteUrl == null) {
                throw new IllegalArgumentException("Cannot call setEndpoint() with null.");
            }
            this.endpoint = remoteUrl;
            return this;
        }

        public Builder setClientProvider(Client.Provider clientProvider) {
            if (clientProvider == null) {
                throw new IllegalArgumentException("Cannot call setClientProvider() with null.");
            }

            this.clientProvider = clientProvider;
            return this;
        }

        public Builder setLogLevel(RestAdapter.LogLevel logLevel) {
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

        public Builder withCustomTrackingId(String trackingId) {
            this.trackingId = trackingId;
            return this;
        }

        public VoucherifyAndroidClient build() {
            return new VoucherifyAndroidClient(this);
        }

    }

}
