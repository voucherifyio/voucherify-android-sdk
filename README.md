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
<a href="#vouchers-api">Vouchers</a>
|
<a href="#redemptions-api">Redemptions</a>
|
</p>

---

## Setup

###### Using Gradle:

```groovy
dependencies {
    compile 'pl.rspective.voucherify.android.client:voucherify-android-sdk:1.0.0'
}
```

###### Using Maven:

```xml
<dependency>
    <groupId>pl.rspective.voucherify.android.client</groupId>
    <artifactId>voucherify-android-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

###### Proguard
```
-keepattributes Signature
-dontwarn rx.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep class com.rspective.voucherify.android.client.** { *; }
-keep class * extends com.rspective.voucherify.android.client.model.** { *; }
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }
```

NOTE:
The SDK requires at least Java 6 or Android 2.3.3 (API 10)

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

All the methods in SDK are provided directly or in asynchronous or rx version:

Every method has a corresponding asynchronous extension which can be accessed through the `async()` or `rx()` method of the vouchers module.

```java
try {
    VoucherResponse voucher = client.vouchers().validations().validateVoucher(VOUCHER_CODE);
} catch (IOExceptions e) {
    // error
}
```

or asynchronously:

```java
client.vouchers().validations().async().validateVoucher("VOUCHER_CODE", new VoucherifyCallback<VoucherResponse>() {
    @Override
    public void onSuccess(VoucherResponse result) {
    }

    @Override
    public void onFailure(IOExceptions error) {
    // error
  }
});
```

or using RxJava:

```java
client.vouchers().validations()
        .rx()
        .validateVoucher("VOUCHER_CODE")
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

## API

#### Validations API

- [Validate Voucher](#validate-voucher)

### [Validate Voucher]

```java
    client.voucher().validations().validateVoucher(String code)
```

```java
    client.voucher().validations().validateVoucher(String code, Integer amount)
```

```java
    client.voucher().validations().validateVoucher(String code, Integer amount, List<OrderItem> orderItems)
```

---

#### Redemptions API

- [Redeem Voucher](#redeem-voucher)

### [Redeem Voucher]

```java
    client.voucher().redemptions().redeem(String code)
```

```java
    client.voucher().redemptions().redeem(String code, VoucherRedemptionContext redemptionContext)
```

## Voucher Checkout View

You can use VoucherCheckoutView to quickly add a UI for discount codes validation.

![](docs/images/android-voucher-checkout.gif)

In your layout XML file add:

```xml
<pl.rspective.voucherify.android.view.VoucherCheckoutView
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
<pl.rspective.voucherify.android.view.VoucherCheckoutView
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

Bug reports and pull requests are welcome through [GitHub Issues](https://github.com/rspective/voucherify-android-sdk/issues).

## Changelog
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

MIT. See the [LICENSE](https://github.com/rspective/voucherify-android-sdk/blob/master/LICENSE) file for details.