voucherify-android-sdk
===============

###Version: 0.0.3

Android SDK for Voucherify to validate a voucher on client side.


Setup
=====

###### Using Gradle:

```groovy
dependencies {
    compile 'pl.rspective.voucherify.android.client:voucherify-android-sdk:0.0.3'
}
```

###### Using Maven:

```xml
<dependency>
    <groupId>com.github.polok.pincodepicker</groupId>
    <artifactId>voucherify-android-sdk</artifactId>
    <version>0.0.3</version>
</dependency>
```

NOTE:
The SDK requires at least Java 6 or Android 2.3.3 (API 10)


### Default Client

The Voucherify Android SDK uses [Retrofit](http://square.github.io/retrofit/) under the hood as a REST client, which detects [OkHttp](http://square.github.io/okhttp/) in your classpath and uses it if it's available, otherwise falls back to the default `HttpURLConnection`.
If you want you can also specify a custom client to be used (see javadoc).


### Proguard
```
-keepattributes Signature
-dontwarn rx.**
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keep class com.rspective.voucherify.android.client.** { *; }
-keep class * extends com.rspective.voucherify.android.client.model.** { *; }
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }
```

Usage
=====
The `VoucherifyAndroidClient` manages your interaction with the Voucherify API.

```java
VoucherifyAndroidClient client = new VoucherifyClient.Builder(YOUR-PUBLIC-CLIENT-APPLICATION-ID, YOUR-PUBLIC-CLIENT-APPLICATION-TOKEN).build();
```

We are tracking users which are validating vouchers with those who consume them, by a `trackingId`. By that we are setting up an identity for the user. If you want to provide your custom value for `trackingId`, you have to do it when creating VoucherifyAndroidClient:

```java
androidClient = new VoucherifyAndroidClient.Builder(YOUR-PUBLIC-CLIENT-APPLICATION-ID, YOUR-PUBLIC-CLIENT-APPLICATION-TOKEN)
       .withCustomTrackingId(YOUR-CUSTOM-TRACKNG-ID)
       .build();
```

Otheres additional params which can be set:
* origin
* endpoint
* log level

```java
androidClient = new VoucherifyAndroidClient.Builder(YOUR-PUBLIC-CLIENT-APPLICATION-ID, YOUR-PUBLIC-CLIENT-APPLICATION-TOKEN)
       .withCustomTrackingId(YOUR-CUSTOM-TRACKNG-ID)
       .withOrigin("android")
       .setEndpoint("10.0.3.2:8080")
       .setLogLevel(RestAdapter.LogLevel.FULL)
       .build();

```

Current list of features:
- validate voucher based on his code


Every method has a corresponding asynchronous extension which can be accessed through the `async()` or 'rx()' method of the vouchers module.

```java
try {
    VoucherResponse voucher = client.vouchers().validate(VOUCHER_CODE);
} catch (RetrofitError e) {
    // error
}
```

or asynchronously:

```java
client.vouchers().async().validate("VOUCHER_CODE", new VoucherifyCallback<VoucherResponse>() {
    @Override
    public void onSuccess(VoucherResponse result) {
    }

    @Override
    public void onFailure(RetrofitError error) {
    // error
  }
});
```

or using RxJava:

```java
client.vouchers()
        .rx()
        .validate("VOUCHER_CODE")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<VoucherResponse>() {
            @Override
            public void call(VoucherResponse voucher) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
            }
        });
```


VoucherResponse
=====

 /*

    {
        "valid": true,
        "type": "amount",
        "discount": 9.99,
        "trackingId": "generated-or-passed-tracking-id"
    }

    OR

    {
        "valid": true,
        "type": "percent",
        "discount": 15,
        "trackingId": "generated-or-passed-tracking-id"
    }

    OR

    {
        "valid": false,
        "type": null,
        "discount": null,
        "trackingId": "generated-or-passed-tracking-id"
    }

    OR

    {
        "type": "error",
        "message": "More details will be here."
        "context": "Here you will receive context of that error."
    }

 */


### Changelog

- **2015-09-11** - `0.0.3` - Updated backend URL.
- **2015-08-15** - '0.0.2' - Added tracking id functionality
- **2015-08-11** - `0.0.1` - Initial version of the SDK.