package io.voucherify.android.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.voucherify.android.client.VoucherifyAndroidClient;
import io.voucherify.android.client.VoucherifyUtils;
import io.voucherify.android.client.exception.VoucherifyError;
import io.voucherify.android.client.model.Customer;
import io.voucherify.android.client.model.Order;
import io.voucherify.android.client.model.ValidationContext;
import io.voucherify.android.client.model.VoucherRedemptionResult;
import io.voucherify.android.client.model.VoucherResponse;
import io.voucherify.android.view.OnValidatedListener;
import io.voucherify.android.view.VoucherCheckoutView;
import okhttp3.logging.HttpLoggingInterceptor;


public class MainActivity extends AppCompatActivity {

    private EditText etProductPrice;
    private TextView tvDiscount;
    private TextView tvNewPrice;
    private VoucherCheckoutView voucherCheckout;

    private VoucherifyAndroidClient voucherifyClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        voucherifyClient = new VoucherifyAndroidClient.Builder(
                "c7306167-294a-4c06-aa5b-d81ee8155a00",
                "07bca261-3bd3-49cc-a58a-8ffce2d20d1b")
                .withCustomTrackingId("demo-android")
                .setLogLevel(HttpLoggingInterceptor.Level.BODY)
                .build();

        tvDiscount = (TextView) findViewById(R.id.tv_discount);
        tvNewPrice = (TextView) findViewById(R.id.tv_new_price);
        etProductPrice = (EditText) findViewById(R.id.et_product_price);
        voucherCheckout = (VoucherCheckoutView) findViewById(R.id.voucher_checkout);
        voucherCheckout.setVoucherifyClient(voucherifyClient);
        voucherCheckout.setOnValidatedListener(new OnValidatedListener() {
            @Override
            public void onValid(final VoucherResponse result) {
                updateDiscountDetails(result);
                etProductPrice.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        updateDiscountDetails(result);
                        return false;
                    }
                });
            }

            @Override
            public void onInvalid(final VoucherResponse result) {
                tvDiscount.setText(null);
                tvNewPrice.setText(null);
                etProductPrice.setOnKeyListener(null);
                voucherCheckout.setVoucherErrorMessage(String.format(getString(R.string.errorVoucherMessage), result.getReason()));
            }

            @Override
            public void onError(VoucherifyError error) {
                voucherCheckout.setVoucherErrorMessage(error.getMessage());
            }
        });

        tryRx();
    }

    private void updateDiscountDetails(VoucherResponse result) {
        try {
            Double originalPrice = Double.valueOf(etProductPrice.getText().toString());
            BigDecimal newPrice = VoucherifyUtils.calculatePrice(BigDecimal.valueOf(originalPrice), result, null);
            BigDecimal discount = VoucherifyUtils.calculateDiscount(BigDecimal.valueOf(originalPrice), result, null);

            tvDiscount.setText(String.format(getString(R.string.discountTitle), discount.toString()));
            tvNewPrice.setText(String.format(getString(R.string.priceAfterDiscountTitle), newPrice.toString()));
        } catch (Exception e) {/* ignoring wrong etProductPrice value */}
    }

    private void tryRx(){
        Customer customer = new Customer();
        customer.setEmail("123@aasda.coco");
        customer.setName("Olo Bolo");
        voucherifyClient.validations().rx().validate("test_code_test")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<VoucherResponse>() {
                    @Override
                    public void accept(VoucherResponse voucherResponse) throws Exception {
                        Log.d(MainActivity.class.getName(), voucherResponse.toString());
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                    }
                });

        voucherifyClient.validations().rx().validate("test_code_test", new ValidationContext(customer, new Order(100, null)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<VoucherResponse>() {
                    @Override
                    public void accept(VoucherResponse voucherResponse) throws Exception {
                        Log.d(MainActivity.class.getName(), voucherResponse.toString());
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                    }
                });

        voucherifyClient.validations().rx().validate("test_code_test", ValidationContext.create(100))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<VoucherResponse>() {
                    @Override
                    public void accept(VoucherResponse voucherResponse) throws Exception {
                        Log.d(MainActivity.class.getName(), voucherResponse.toString());
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                    }
                });

        voucherifyClient.redemptions().rx().redeem("test_code_test")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<VoucherRedemptionResult>() {
                    @Override
                    public void accept(VoucherRedemptionResult voucherResponse) throws Exception {
                        Log.d(MainActivity.class.getName(), voucherResponse.toString());
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                    }
                });
    }

}
