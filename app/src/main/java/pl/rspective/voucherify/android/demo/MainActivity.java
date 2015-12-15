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
import retrofit.RestAdapter;
import retrofit.RetrofitError;


public class MainActivity extends AppCompatActivity {

    private EditText etVoucherCode;
    private EditText etProductPrice;
    private TextView tvResultLog;
    private TextView tvDiscount;
    private TextView tvNewPrice;

    private VoucherifyAndroidClient androidClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidClient = new VoucherifyAndroidClient.Builder("011240bf-d5fc-4ef1-9e82-11eb68c43bf5", "9e2230c5-71fb-460a-91c6-fbee64707a20")
                .withCustomTrackingId("demo-android")
                .withOrigin("android")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        etVoucherCode = (EditText) findViewById(R.id.et_voucher_code);
        tvResultLog = (TextView) findViewById(R.id.tv_result);
        tvDiscount = (TextView) findViewById(R.id.tv_discount);
        tvNewPrice = (TextView) findViewById(R.id.tv_new_price);
        etProductPrice = (EditText) findViewById(R.id.et_product_price);


        findViewById(R.id.btn_validate_voucher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvResultLog.setText("");

                androidClient.voucher().async().validate(etVoucherCode.getText().toString(), new VoucherifyCallback<VoucherResponse, RetrofitError>() {
                    @Override
                    public void onSuccess(final VoucherResponse result) {
                        tvResultLog.setText("Is voucher valid: " + result.isValid() + "");
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
                    public void onFailure(RetrofitError error) {
                        tvResultLog.setText(error.toString());
                    }
                });
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
