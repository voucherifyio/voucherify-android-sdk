package pl.rspective.voucherify.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import pl.rspective.voucherify.android.client.R;
import pl.rspective.voucherify.android.client.VoucherifyAndroidClient;
import pl.rspective.voucherify.android.client.callback.VoucherifyCallback;
import pl.rspective.voucherify.android.client.model.VoucherResponse;
import retrofit.RetrofitError;

/**
 * VoucherCheckoutView is a UI component that makes it easier to validate discount codes in
 * <a href="http://voucherify.io">Voucherify</a>. The view consists of a text field to input
 * voucher code and a button to send the code for validation. You can get the validation result by
 * attaching an @{link OnValidatedListener}.
 */
public class VoucherCheckoutView extends LinearLayout {

    private VoucherifyAndroidClient voucherifyClient;
    private OnValidatedListener onValidatedListener;
    private EditText voucherCodeEditText;
    private Button validateButton;
    private String validateButtonText;

    public VoucherCheckoutView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public VoucherCheckoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public VoucherCheckoutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        View inflatedView = inflate(context, R.layout.voucher_checkout_view, this);
        voucherCodeEditText = (EditText) inflatedView.findViewById(R.id.et_voucher_code);
        validateButton = (Button) inflatedView.findViewById(R.id.btn_validate);
        validateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doValidate();
            }
        });

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.VoucherCheckoutView, defStyle, 0);

        validateButtonText = a.getString(R.styleable.VoucherCheckoutView_validateButtonText) ;
        if (validateButtonText != null) {
            validateButton.setText(validateButtonText);
        }

        a.recycle();
    }

    private void doValidate() {
        if (voucherifyClient == null) {
            throw new IllegalStateException("VoucherifyClient not provided.");
        }

        String voucherCode = voucherCodeEditText.getText().toString();

        voucherifyClient.voucher().async().validate(voucherCode, new VoucherifyCallback<VoucherResponse, RetrofitError>() {
            @Override
            public void onSuccess(final VoucherResponse result) {
                if (onValidatedListener != null) {
                    onValidatedListener.onValidated(result);
                }
            }

            @Override
            public void onFailure(RetrofitError error) {
                if (onValidatedListener != null) {
                    onValidatedListener.onError(error);
                }
            }
        });

    }

    /**
     * Sets {@link VoucherifyAndroidClient} - required for making validation requests to the Voucherify API.
     *
     * @param voucherifyClient
     */
    public void setVoucherifyClient(VoucherifyAndroidClient voucherifyClient) {
        this.voucherifyClient = voucherifyClient;
    }

    /**
     * A listener that gets invoked when a validation result has been received.
     *
     * @param listener
     */
    public void setOnValidatedListener(OnValidatedListener listener){
        this.onValidatedListener = listener;
    }
}
