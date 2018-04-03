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
import io.voucherify.android.client.model.VouchersList;
import io.voucherify.android.helper.MockSuccessCall;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListingTest {
    @Mock
    VoucherifyApi api;

    private Listing listing;

    @Before
    public void setUp() {
        listing = new Listing(api, Schedulers.trampoline(), "test_id");
        MockSuccessCall<VouchersList> mockSuccessCall = new MockSuccessCall<>();
        when(api.listVouchers("test_id"))
                .thenReturn(mockSuccessCall);
    }

    @Test
    public void listVouchers_passTrackingId_invokeApiListVouchersWithRightQuery() throws Exception {
        listing.list();

        String expectedTrackingId =  "test_id";
        verify(api, times(1)).listVouchers(expectedTrackingId);
    }
}
