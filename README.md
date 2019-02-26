<p align="center">
  <img src="./docs/images/voucherify-android-sdk.png"/>
</p>

<h3 align="center">Official <a href="http://voucherify.io?utm_source=github&utm_medium=sdk&utm_campaign=acq">Voucherify</a> SDK for Android</h3>

<p align="center">
<b><a href="#setup">Setup</a></b>
|
<b><a href="#synchronous-rx-or-async">Synchronous, Rx or Async?</a></b>
|
<b><a href="#voucher-checkout-view">Voucher Checkout View</a></b>
|
<b><a href="#contributing">Contributing</a></b>
|
<b><a href="#changelog">Changelog</a></b>
|
<b><a href="#license">License</a></b>
|
</p>

<p align="center">
API:
<a href="#validations-api">Validations</a>
|
<a href="#redemptions-api">Redemptions</a>
|
<a href="#listing-api">Voucher Listing</a>
|
<a href="#promotions">Promotions</a>
</p>

---

 [ ![Download](https://api.bintray.com/packages/rspective/io.voucherify.android.client/voucherify-android-sdk/images/download.svg?version=1.1.0) ](https://bintray.com/rspective/io.voucherify.android.client/voucherify-android-sdk/1.1.0/link)
[![Build Status](https://travis-ci.org/voucherifyio/voucherify-android-sdk.svg?branch=develop)](https://travis-ci.org/voucherifyio/voucherify-android-sdk)

## Setup

###### Using Gradle:

```groovy
dependencies {
    compile 'io.voucherify.android.client:voucherify-android-sdk:2.1.0'
}
```

###### Using Maven:

```xml
<dependency>
    <groupId>io.voucherify.android.client</groupId>
    <artifactId>voucherify-android-sdk</artifactId>
    <version>2.1.0</version>
</dependency>
```

###### Proguard
```
-keepattributes Signature
-dontwarn rx.**
-dontwarn io.reactivex.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep class io.voucherify.android.client.** { *; }
-keep class * extends io.voucherify.android.client.model.** { *; }
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }
```

NOTE:
The SDK requires at least Java 6 or Android 4.1 (API 16)

### Configuration
The `VoucherifyAndroidClient` manages your interaction with the Voucherify API.

```java
VoucherifyAndroidClient client = new VoucherifyClient.Builder(YOUR-PUBLIC-CLIENT-APPLICATION-ID, YOUR-PUBLIC-CLIENT-APPLICATION-TOKEN).build();
```

We are tracking users which are validating vouchers with those who consume them, by a `tracking_id`. By that we are setting up an identity for the user. If you want to provide your custom value for `tracking_id`, you have to do it when creating VoucherifyAndroidClient:

```java
androidClient = new VoucherifyAndroidClient.Builder(YOUR-PUBLIC-CLIENT-APPLICATION-ID, YOUR-PUBLIC-CLIENT-APPLICATION-TOKEN)
       .withCustomTrackingId(YOUR-CUSTOM-TRACKNG-ID)
       .build();
```

Other additional params which can be set:
* origin
* endpoint
* log level

```java
androidClient = new VoucherifyAndroidClient.Builder(YOUR-PUBLIC-CLIENT-APPLICATION-ID, YOUR-PUBLIC-CLIENT-APPLICATION-TOKEN)
       .withCustomTrackingId(YOUR-CUSTOM-TRACKNG-ID)
       .withOrigin("http://my-android-origin")
       .setEndpoint("10.0.3.2:8080")
       .setLogLevel(HttpLoggingInterceptor.Level.BODY)
       .build();

```


## Synchronous, Rx or Async?

All the methods in SDK are provided directly or in asynchronous or rx (RxJava2) version:

Every method has a corresponding asynchronous extension which can be accessed through the `async()` or `rx()` method of the vouchers module.

__If used directly, methods must be run in separate thread to avoid NetworkOnMainThreadException__
```java
try {
    VoucherResponse voucher = client.validations().validateVoucher(VOUCHER_CODE);
} catch (IOExceptions e) {
    // error
}
```

or asynchronously:

```java
client.validations().async().validateVoucher("VOUCHER_CODE", new VoucherifyCallback<VoucherResponse>() {
    @Override
    public void onSuccess(VoucherResponse result) {
    }

    @Override
    public void onFailure(IOExceptions error) {
    // error
  }
});
```

or using RxJava (RxJava2):

```java
client.validations()
        .rx()
        .validateVoucher("VOUCHER_CODE")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<VoucherResponse>() {
            @Override
            public void accept(VoucherResponse voucher) {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
            }
        });
```

## API

#### Validations API

### [Validate Voucher]

```java
    client.validations().validate(String code)
```

```java
    client.validations().validate(String code, ValidationContext context)
```

### [Validate Promotions]

```java
    client.validations().validate(ValidationContext context)
```

#### Redemptions API

### [Redeem Voucher]

```java
    client.redemptions().redeem(String code)
```

```java
    client.redemptions().redeem(String code, RedemptionContext context)
```

### [Redeem Promotions]

```java
    client.redemptions().redeem(PromotionTier promotionTier, RedemptionContext context)
```

#### Listing vouchers API

### [List Vouchers]

```java
    client.listing().list()
```

```java
    client.listing().list(String customer)
```

#### Promotions

### [List Promotion Tiers]

```java
    client.promotions().list(Boolean isAvailable, int limit, int page)
```

## Voucher Checkout View

You can use VoucherCheckoutView to quickly add a UI for discount codes validation.

![](docs/images/android-voucher-checkout.gif)

In your layout XML file add:

```xml
<io.voucherify.android.view.VoucherCheckoutView
    android:id="@+id/voucher_checkout"/>
```

Then in your activity init the VoucherCheckoutView with the VoucherifyAndroidClient.

```
VoucherifyAndroidClient voucherifyClient = new VoucherifyAndroidClient.Builder(
            YOUR-PUBLIC-CLIENT-APPLICATION-ID,
            YOUR-PUBLIC-CLIENT-APPLICATION-TOKEN)
       .withCustomTrackingId(YOUR-CUSTOM-TRACKNG-ID)
       .build();

VoucherCheckoutView voucherCheckout = (VoucherCheckoutView) findViewById(R.id.voucher_checkout);
```

You will also likely want to get validation results. You can achieve that by adding OnValidatedListener:

```
voucherCheckout.setOnValidatedListener(new OnValidatedListener() {
    @Override
    public void onValid(final VoucherResponse result) {
    }

    @Override
    public void onInvalid(final VoucherResponse result) {
    }

    @Override
    public void onError(VoucherifyError error) {
    }
});
```

#### Customization

The component is highly customizable. You can set following attributes:

- `validateButtonText` - text displayed on the button
- `voucherCodeHint` - label attached to the voucher code input
- `voucherIcon` - icon appearing on the right
- `validVoucherIcon` - icon appearing on the right after validation when provided code was valid
- `invalidVoucherIcon` - icon appearing on the right after validation when provided code was invalid 

You can disable any of the 3 icons by specifying them as `@android:color/transparent`.

Example:

```xml
<io.voucherify.android.view.VoucherCheckoutView
    android:id="@+id/voucher_checkout"/>
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:validateButtonText="Apply"
    app:voucherCodeHint="Coupon Code"
    app:voucherIcon="@android:color/transparent"
    app:validVoucherIcon="@android:color/transparent"
    app:invalidVoucherIcon="@android:color/transparent"/>
```

You can override animations by placing `valid.xml` and `invalid.xml` in `res/anim`.

You can also set your own colors and other visual properties by defining styles (in `res\values\styles.xml`):

- VoucherCodeLabel
- VoucherCodeEditText
- VoucherValidateButton

For example to set the button background color to light green:

```
    <style name="VoucherValidateButton">
        <item name="android:background">#8BC34A</item>
    </style>
```

## Contributing

Bug reports and pull requests are welcome through [GitHub Issues](https://github.com/voucherifyio/voucherify-android-sdk/issues).

## Changelog
- **2018-11-17** - `2.1.0` - Increased minSdkVersion to 16 and updated external dependencies
- **2018-04-16** - `2.0.0` - Adjusted API for Validation and Redemption
- **2018-04-05** - `1.1.0` - Added API for Promotions and Vouchers Listing
- **2017-01-02** - `1.0.0` - Unify API with other voucherify SDKs.
- **2016-09-20** - `0.6.0` - Redeem a voucher.
- **2016-09-06** - `0.5.0` - Added order items.
- **2016-06-23** - `0.4.0` - Added support for gift vouchers.
- **2016-05-30** - `0.3.1` - Enabled to show an error message below the code input.
- **2016-05-20** - `0.3.0` - Voucher checkout view.
- **2016-05-19** - `0.2.0` - Custom error handling.
- **2016-04-04** - `0.1.3` - Updated API URL, HTTPS enabled by default.
- **2016-01-14** - `0.1.2` - Default value for `origin` header.
- **2015-12-14** - `0.1.0` - New discount model, new discount type: UNIT.
- **2015-11-23** - `0.0.9` - added `X-Voucherify-Channel` header.
- **2015-11-09** - `0.0.6` - Changed discount type from double to integer.
- **2015-11-05** - `0.0.5` - Renamed trackingId to tracking_id.
- **2015-10-22** - `0.0.4` - New backend URL.
- **2015-09-01** - `0.0.3` - Updated backend URL.
- **2015-08-15** - `0.0.2` - Added tracking id functionality.
- **2015-08-11** - `0.0.1` - Initial version of the SDK.

## License

MIT. See the [LICENSE](https://github.com/voucherifyio/voucherify-android-sdk/blob/master/LICENSE) file for details.

[Validate Voucher]: https://docs.voucherify.io/reference#vouchers-validate
[Redeem Voucher]: https://docs.voucherify.io/reference#redeem-voucher-client-side
[List Vouchers]: https://docs.voucherify.io/reference
[Validate Promotions]:  https://docs.voucherify.io/reference
[Redeem Promotions]:  https://docs.voucherify.io/reference
[List Promotion Tiers]: https://docs.voucherify.io/v2018-08-01/reference#list-promotion-tiers-client-side
