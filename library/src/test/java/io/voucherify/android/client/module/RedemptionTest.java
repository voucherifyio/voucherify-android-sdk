package io.voucherify.android.client.module;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.schedulers.Schedulers;
import io.voucherify.android.client.api.VoucherifyApi;
import io.voucherify.android.client.model.VoucherRedemptionContext;
import io.voucherify.android.client.model.VoucherRedemptionResult;
import io.voucherify.android.helper.MockSuccessCall;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedemptionTest {
    @Mock
    VoucherifyApi api;

    private Redemption redemption;

    @Before
    public void setUp() {
        redemption = new Redemption(api, Schedulers.trampoline(), "TRACKING_ID");
        MockSuccessCall<VoucherRedemptionResult> mockSuccessCall = new MockSuccessCall<>();
        when(api.redeemVoucher(anyString(), anyString()))
                .thenReturn(mockSuccessCall);
        when(api.redeemVoucher(anyString(), any(VoucherRedemptionContext.class)))
                .thenReturn(mockSuccessCall);
    }

    @Test
    public void redeem_passCode_invokeRedeemVoucherWithRightParameters() throws Exception {
        redemption.redeem("SAMPLE_CODE");
        verify(api, times(1)).redeemVoucher("SAMPLE_CODE", "TRACKING_ID");
    }

    @Test
    public void redeem_passCOdeAndContext_invokeRedeemVoucherWithRightParameters() throws Exception {
        VoucherRedemptionContext voucherRedemptionContext = new VoucherRedemptionContext(null);
        redemption.redeem("SAMPLE_CODE", voucherRedemptionContext);
        verify(api, times(1)).redeemVoucher("SAMPLE_CODE", voucherRedemptionContext);
    }
}
