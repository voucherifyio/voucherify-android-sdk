package io.voucherify.android.client.module;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;
import io.voucherify.android.client.api.VoucherifyApi;
import io.voucherify.android.client.model.OrderItem;
import io.voucherify.android.client.model.VoucherResponse;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    }

    @Test
    public void validateVoucher() throws Exception {
        when(api.validateVoucher(ArgumentMatchers.<String, String>anyMap()))
                .thenReturn(mockSuccessApiCall());
        validation.validateVoucher("SAMPLE_CODE");
        Map<String, String> queryParams = new LinkedHashMap<>();
        queryParams.put("channel", "android");
        queryParams.put("code", "SAMPLE_CODE");

        verify(api, times(1)).validateVoucher(queryParams);
    }

    @Test
    public void validateVoucherWithAmount() throws Exception {
        when(api.validateVoucher(ArgumentMatchers.<String, String>anyMap()))
                .thenReturn(mockSuccessApiCall());
        validation.validateVoucher("SAMPLE_CODE", 100);
        Map<String, String> queryParams = new LinkedHashMap<>();
        queryParams.put("channel", "android");
        queryParams.put("code", "SAMPLE_CODE");
        queryParams.put("amount", "100");

        verify(api, times(1)).validateVoucher(queryParams);
    }

    @Test
    public void validateVoucherWithAmountAndItemList() throws Exception {
        when(api.validateVoucher(ArgumentMatchers.<String, String>anyMap()))
                .thenReturn(mockSuccessApiCall());
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

    private Call<VoucherResponse> mockSuccessApiCall() {
        return new Call<VoucherResponse>() {
            @Override
            public Response<VoucherResponse> execute() throws IOException {
                return Response.success(null);
            }

            @Override
            public void enqueue(Callback<VoucherResponse> callback) {

            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<VoucherResponse> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };
    }
}
