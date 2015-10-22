package pl.rspective.voucherify.android.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import pl.rspective.voucherify.android.client.VoucherifyAndroidClient;
import pl.rspective.voucherify.android.client.callback.VoucherifyCallback;
import pl.rspective.voucherify.android.client.model.VoucherResponse;
import retrofit.RestAdapter;
import retrofit.RetrofitError;


public class MainActivity extends AppCompatActivity {

    private EditText etVoucherCode;
    private TextView tvResultLog;

    private VoucherifyAndroidClient androidClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidClient = new VoucherifyAndroidClient.Builder("YOUR-CLIENT-ID", "YOUR-PUBLIC-KEY")
                .withCustomTrackingId("YOUR-CUSTOM-TRACKING-ID")
                .withOrigin("android")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        etVoucherCode = (EditText) findViewById(R.id.et_voucher_code);
        tvResultLog = (TextView) findViewById(R.id.tv_result);

        findViewById(R.id.btn_validate_voucher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvResultLog.setText("");

                androidClient.voucher().async().validate(etVoucherCode.getText().toString(), new VoucherifyCallback<VoucherResponse, RetrofitError>() {
                    @Override
                    public void onSuccess(VoucherResponse result) {
                        tvResultLog.setText("Status: " + result.isValid());
                    }

                    @Override
                    public void onFailure(RetrofitError error) {
                        tvResultLog.setText(error.toString());
                    }
                });
            }
        });
    }

}
