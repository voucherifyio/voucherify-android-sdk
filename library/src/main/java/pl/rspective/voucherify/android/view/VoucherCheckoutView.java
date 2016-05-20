package pl.rspective.voucherify.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import pl.rspective.voucherify.android.client.R;
import pl.rspective.voucherify.android.client.VoucherifyAndroidClient;
import pl.rspective.voucherify.android.client.callback.VoucherifyCallback;
import pl.rspective.voucherify.android.client.exception.VoucherifyError;
import pl.rspective.voucherify.android.client.model.VoucherResponse;

/**
 * VoucherCheckoutView is a UI component that makes it easier to validate discount codes in
 * <a href="http://voucherify.io">Voucherify</a>. The view consists of a text field to input
 * voucher code and a button to send the code for validation. You can get the validation result by
 * attaching an @{link OnValidatedListener}.
 */
public class VoucherCheckoutView extends RelativeLayout {

    private VoucherifyAndroidClient voucherifyClient;
    private OnValidatedListener onValidatedListener;
    private TextInputLayout voucherCodeLabel;
    private EditText voucherCodeEditText;
    private Button validateButton;
    private Animation validAnimation;
    private Animation invalidAnimation;
    private Drawable voucherIcon;
    private Drawable validVoucherIcon;
    private Drawable invalidVoucherIcon;

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
        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        View inflatedView = inflate(context, R.layout.voucher_checkout_view, this);
        voucherCodeLabel = (TextInputLayout) inflatedView.findViewById(R.id.til_voucher_code);
        voucherCodeEditText = (EditText) inflatedView.findViewById(R.id.et_voucher_code);
        voucherCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateButton.setEnabled(!s.toString().trim().isEmpty());
                voucherCodeEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, voucherIcon, null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        validateButton = (Button) inflatedView.findViewById(R.id.btn_validate);
        validateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doValidate();
            }
        });

        invalidAnimation = AnimationUtils.loadAnimation(context, R.anim.invalid);
        validAnimation = AnimationUtils.loadAnimation(context, R.anim.valid);
        applyCustomAttributes(context, attrs, defStyle);
    }

    private void applyCustomAttributes(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VoucherCheckoutView, defStyle, 0);

        String voucherCodeHint = a.getString(R.styleable.VoucherCheckoutView_voucherCodeHint);
        if (voucherCodeHint != null) {
            voucherCodeLabel.setHint(voucherCodeHint);
        }

        String validateButtonText = a.getString(R.styleable.VoucherCheckoutView_validateButtonText);
        if (validateButtonText != null) {
            validateButton.setText(validateButtonText);
        }

        voucherIcon = a.getDrawable(R.styleable.VoucherCheckoutView_voucherIcon);
        if (voucherIcon == null) {
            voucherIcon = ContextCompat.getDrawable(context, R.drawable.ic_voucherify_logo_white_24dp);
        }
        voucherCodeEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, voucherIcon, null);

        validVoucherIcon = a.getDrawable(R.styleable.VoucherCheckoutView_validVoucherIcon);
        if (validVoucherIcon == null) {
            validVoucherIcon = ContextCompat.getDrawable(context, R.drawable.ic_check_circle_white_24dp);
        }
        invalidVoucherIcon = a.getDrawable(R.styleable.VoucherCheckoutView_invalidVoucherIcon);
        if (invalidVoucherIcon == null) {
            invalidVoucherIcon = ContextCompat.getDrawable(context, R.drawable.ic_error_white_24dp);
        }

        a.recycle();
    }

    public static Drawable getTintedDrawable(Context ctx, @DrawableRes int drawableId, @ColorRes int color) {
        Drawable drawable = ContextCompat.getDrawable(ctx, drawableId);
        int tintColor = ContextCompat.getColor(ctx, color);
        DrawableCompat.setTint(drawable, tintColor);
        return drawable;
    }


    private void doValidate() {
        if (voucherifyClient == null) {
            throw new IllegalStateException("VoucherifyClient not provided.");
        }

        String voucherCode = voucherCodeEditText.getText().toString();

        voucherifyClient.voucher().async().validate(voucherCode, new VoucherifyCallback<VoucherResponse, VoucherifyError>() {
            @Override
            public void onSuccess(final VoucherResponse result) {
                if (onValidatedListener != null) {
                    if (result.isValid()) {
                        voucherCodeEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, validVoucherIcon, null);
                        voucherCodeEditText.startAnimation(validAnimation);
                        onValidatedListener.onValid(result);
                    } else {
                        voucherCodeEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, invalidVoucherIcon, null);
                        voucherCodeEditText.startAnimation(invalidAnimation);
                        onValidatedListener.onInvalid(result);
                    }
                }
            }

            @Override
            public void onFailure(VoucherifyError error) {
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
