package io.voucherify.android.client.module;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;
import io.voucherify.android.client.api.VoucherifyApi;
import io.voucherify.android.client.model.OrderItem;
import io.voucherify.android.client.model.VoucherResponse;
import io.voucherify.android.helper.MockSuccessCall;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidationTest {
    @Mock
    VoucherifyApi api;

    private Validation validation;

    @Before
    public void setUp() {
        validation = new Validation(api, Schedulers.trampoline(), null);
        MockSuccessCall<VoucherResponse> mockSuccessCall = new MockSuccessCall<>();
        when(api.validateVoucher(ArgumentMatchers.<String, String>anyMap()))
                .thenReturn(mockSuccessCall);
    }

    @Test
    public void validateVoucher_passCode_invokeApiValidateVoucherWithRightQuery() throws Exception {
        validation.validateVoucher("SAMPLE_CODE");
        Map<String, String> queryParams = new LinkedHashMap<>();
        queryParams.put("channel", "android");
        queryParams.put("code", "SAMPLE_CODE");

        verify(api, times(1)).validateVoucher(queryParams);
    }

    @Test
    public void validateVoucher_passCodeAndAmount_invokeApiValidateVoucherWithRightQuery() throws Exception {
        validation.validateVoucher("SAMPLE_CODE", 100);
        Map<String, String> queryParams = new LinkedHashMap<>();
        queryParams.put("channel", "android");
        queryParams.put("code", "SAMPLE_CODE");
        queryParams.put("amount", "100");

        verify(api, times(1)).validateVoucher(queryParams);
    }

    @Test
    public void validateVoucher_passCodeAndAmountAndOrderItem_invokeApiValidateVoucherWithRightQuery() throws Exception {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem("0", "sku_0", 1));
        validation.validateVoucher("SAMPLE_CODE", 100, orderItems);

        Map<String, String> queryParams = new LinkedHashMap<>();
        queryParams.put("channel", "android");
        queryParams.put("code", "SAMPLE_CODE");
        queryParams.put("amount", "100");
        queryParams.put("item[0][product_id]", "0");
        queryParams.put("item[0][sku_id]", "sku_0");
        queryParams.put("item[0][quantity]", "1");

        verify(api, times(1)).validateVoucher(queryParams);
    }
}
