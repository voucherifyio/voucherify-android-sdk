package pl.rspective.voucherify.android.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;

import pl.rspective.voucherify.android.client.VoucherifyAndroidClient;
import pl.rspective.voucherify.android.client.VoucherifyUtils;
import pl.rspective.voucherify.android.client.callback.VoucherifyCallback;
import pl.rspective.voucherify.android.client.model.VoucherResponse;
import pl.rspective.voucherify.android.view.OnValidatedListener;
import pl.rspective.voucherify.android.view.VoucherCheckoutView;
import retrofit.RestAdapter;
import retrofit.RetrofitError;


public class MainActivity extends AppCompatActivity {

    private EditText etProductPrice;
    private TextView tvResultLog;
    private TextView tvDiscount;
    private TextView tvNewPrice;
    private VoucherCheckoutView voucherCheckout;

    private VoucherifyAndroidClient voucherifyClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        voucherifyClient = new VoucherifyAndroidClient.Builder("011240bf-d5fc-4ef1-9e82-11eb68c43bf5", "9e2230c5-71fb-460a-91c6-fbee64707a20")
                .withCustomTrackingId("demo-android")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        tvResultLog = (TextView) findViewById(R.id.tv_result);
        tvDiscount = (TextView) findViewById(R.id.tv_discount);
        tvNewPrice = (TextView) findViewById(R.id.tv_new_price);
        etProductPrice = (EditText) findViewById(R.id.et_product_price);
        voucherCheckout = (VoucherCheckoutView) findViewById(R.id.voucher_checkout);
        voucherCheckout.setVoucherifyClient(voucherifyClient);
        voucherCheckout.setOnValidatedListener(new OnValidatedListener() {
            @Override
            public void onValid(final VoucherResponse result) {
                tvResultLog.setText("Voucher is valid.");
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
                tvResultLog.setText("Invalid voucher");
                tvDiscount.setText(null);
                tvNewPrice.setText(null);
                etProductPrice.setOnKeyListener(null);
            }

            @Override
            public void onError(RetrofitError error) {
                tvResultLog.setText(error.toString());
            }
        });
    }

    private void updateDiscountDetails(VoucherResponse result) {
        try {
            Double originalPrice = Double.valueOf(etProductPrice.getText().toString());
            BigDecimal newPrice = VoucherifyUtils.calculatePrice(BigDecimal.valueOf(originalPrice), result, null);
            BigDecimal discount = VoucherifyUtils.calculateDiscount(BigDecimal.valueOf(originalPrice), result, null);

            tvDiscount.setText("Discount: " + discount.toString());
            tvNewPrice.setText("Price after discount: " + newPrice.toString());
        } catch(Exception e) {/* ignoring wrong etProductPrice value */}
    }

}
