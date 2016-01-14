package pl.rspective.voucherify.android.client;

public interface Constants {

    // HTTP constants
    String SCHEME_HTTP = "http";
    String SCHEME_HTTPS = "https";
    String HTTP_HEADER_CLIENT_ID = "X-Client-Application-Id";
    String HTTP_HEADER_CLIENT_TOKEN = "X-Client-Token";
    String HTTP_HEADER_ORIGIN = "origin";
    String HTTP_HEADER_VOUCHERIFY_CHANNEL = "X-Voucherify-Channel";
    String VOUCHERIFY_CHANNEL_NAME = "Android-SDK";

    // Configuration
    String ENDPOINT_VOUCHERIFY = "voucherify-bouncer.herokuapp.com";
    String VOUCHERIFY_DEFAULT_ORIGIN = "http://voucherify-android";

}