Voucherify Android SDK
======================

###Version: 0.6.0

[Voucherify](http://voucherify.io?utm_source=github&utm_medium=sdk&utm_campaign=acq) is an API-first platform for software developers who are dissatisfied with high-maintenance custom coupon software. Our product is a coupon infrastructure through API that provides a quicker way to build coupon generation, distribution and tracking. Unlike legacy coupon software we have:

* an API-first SaaS platform that enables customisation of every aspect of coupon campaigns
* a management console that helps cut down maintenance and reporting overhead
* an infrastructure to scale up coupon activity in no time

You can find full documentation on [voucherify.readme.io](https://voucherify.readme.io).

Try a demo app from Google Play:

<a href='https://play.google.com/store/apps/details?id=pl.rspective.voucherify.android.demo'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width="250"/></a>

Setup
=====

###### Using Gradle:

```groovy
dependencies {
    compile 'pl.rspective.voucherify.android.client:voucherify-android-sdk:0.6.0'
}
```

###### Using Maven:

```xml
<dependency>
    <groupId>pl.rspective.voucherify.android.client</groupId>
    <artifactId>voucherify-android-sdk</artifactId>
    <version>0.6.0</version>
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

We are tracking users which are validating vouchers with those who consume them, by a `tracking_id`. By that we are setting up an identity for the user. If you want to provide your custom value for `tracking_id`, you have to do it when creating VoucherifyAndroidClient:

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
       .withOrigin("http://my-android-origin")
       .setEndpoint("10.0.3.2:8080")
       .setLogLevel(RestAdapter.LogLevel.FULL)
       .build();

```

Current list of features:
- validate a voucher based on its code and optionally order amount (required for gift vouchers)
- redeem a voucher

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

### Gift vouchers

Validationg a gift voucher requires to pass an amount that is intended to be withdrawn from the voucher.
Order amount have to be expressed in cents, as an integer. For example $22.50 should be provided as 2250:

```java
    VoucherResponse voucher = client.vouchers().validate("VOUCHER_CODE", 2250);
```

### Validation rules

When validating vouchers with validation rules concerning products or variants (SKUs) it's required to pass order items.

```java
    VoucherResponse voucher = client.vouchers().validate("VOUCHER_CODE", 2250, Arrays.asList(
       new OrderItem("prod_6wY2Vvc6FrfrwX", "sku_y7WxIymNSCR138", 1),
       new OrderItem("prod_r04XQ00xz6EVRi", "sku_XnmQ3d0jV3x3Uy", 2))
   ));
```

### Redeem a voucher

* Just by code

```java
     VoucherRedemptionResult redemptionResult = client.voucher().redeemVoucher("test");
```

* With customer profile

```java
     Customer customer = new Customer.Builder()
                .withSourceId("alice.morgan")
                .withName("Alice Morgan")
                .withEmail("alice@morgan.com")
                .withDescription("")
                .addMetadata("locale", "en-GB")
                .addMetadata("shoeSize", 5)
                .addMetadata("favouriteBrands", new String[]{"Armani", "L'Autre Chose", "Vicini"})
                .build();

        client.voucher().redeemVoucher("test", new VoucherRedemptionContext(customer));
```

* With customer id

If you already created a customer profile in Voucherify's database, you can identify your customer in following redemptions by a generated id (starting with `cust_`).

```java
   Customer customer = new Customer.Builder()
                .withId("cust_C9qJ3xKgZFqkpMw7b21MF2ow")
                .build();

   client.voucher().redeemVoucher("test", new VoucherRedemptionContext(customer));
```

* With order amount

If you want to redeem a gift voucher you have to provide an amount that you wish take. You can pass the amount in VoucherRedemptionContext.order.amount:

```java
    client.voucher().redeemVoucher("test", new VoucherRedemptionContext(customer, Order.amount(5000)))
```


* With validation rules

If your voucher includes some validation rules regarding customer (segments) then you have to supply customer (by id, source id or tracking id) when redeeming the voucher. When redeeming vouchers with validation rules concerning products or variants (SKUs) it's required to pass order items.

```java
    VoucherRedemptionContext redemptionContext = new VoucherRedemptionContext(
        new Customer.Builder()
                .withSourceId("alice.morgan")
                .build(),
        new Order(1250, Arrays.asList(
                    new OrderItem("prod_6wY2Vvc6FrfrwX", "sku_y7WxIymNSCR138", 1),
                    new OrderItem("prod_r04XQ00xz6EVRi", "sku_XnmQ3d0jV3x3Uy", 2))));

        client.voucher().redeemVoucher("VoucherWithValidationRules", redemptionContext);
```

VoucherResponse
=====

Valid amount discount response:

    {
        "code": "VOUCHER_CODE",
        "valid": true,
        "discount": {
            "type": "AMOUNT",
            "amount_off": 999,
        },
        "tracking_id": "generated-or-passed-tracking-id"
    }

Valid percentage discount response:

    {
        "code": "VOUCHER_CODE",
        "valid": true,
        "discount": {
            "type": "PERCENT",
            "percent_off": 15.0,
        },
        "tracking_id": "generated-or-passed-tracking-id"
    }
    
Valid unit discount response:
    
    {
        "code": "VOUCHER_CODE",
        "valid": true,
        "discount": {
            "type": "UNIT",
            "unit_off": 1.0,
        },
        "tracking_id": "generated-or-passed-tracking-id"
    }

Valid gift voucher response:
    
    
    ```javascript
    {
        "code": "VOUCHER_CODE",
        "valid": true,
        "gift": {
            "amount": 10000
        }
        "tracking_id": "generated-or-passed-tracking-id"
    }
    ```

Redeem voucher response:

{
    "id": "r_yRmanaA6EgSE9uDYvMQ5Evfp",
    "object": "redemption",
    "date": "2016-04-25T10:34:57Z",
    "customer_id": null,
    "tracking_id": "(tracking_id not set)",
    "voucher": {
        "code": "v1GiJYuuS",
        "campaign": "vip",
        "discount": {
            "percent_off": 10.0,
            "type": "PERCENT"
        },
        "expiration_date": "2016-12-31T23:59:59Z",
        "redemption": {
            "quantity": 3,
            "redeemed_quantity": 2,
            "redemption_entries": [
                {
                    "id": "r_gQzOnTwmhn2nTLwW4sZslNKY",
                    "object": "redemption",
                    "date": "2016-04-24T06:03:35Z",
                    "customer_id": null,
                    "tracking_id": "(tracking_id not set)"
                },
                {
                    "id": "r_yRmanaA6EgSE9uDYvMQ5Evfp",
                    "object": "redemption",
                    "date": "2016-04-25T10:34:57Z",
                    "customer_id": null,
                    "tracking_id": "(tracking_id not set)"
                }
            ]
        },
        "active": true,
        "additional_info": ""
    }
}

Invalid voucher response:

    {
        "code": "VOUCHER_CODE",
        "valid": false,
        "reason": "voucher expired",
        "tracking_id": "generated-or-passed-tracking-id"
    }

There are several reasons why validation may fail (`valid: false` response). 
You can find the actual cause in the `reason` field:

- `voucher is disabled`
- `voucher not active yet`
- `voucher expired`
- `quantity exceeded`
- `gift amount exceeded`

### Voucher Checkout View

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

License
=====

MIT. See the [LICENSE](https://github.com/rspective/voucherify-android-sdk/blob/master/LICENSE) file for details.