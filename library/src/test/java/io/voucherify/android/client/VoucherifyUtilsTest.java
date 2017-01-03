package io.voucherify.android.client;

import org.junit.Test;

import java.math.BigDecimal;

import io.voucherify.android.client.model.Discount;
import io.voucherify.android.client.model.DiscountType;
import io.voucherify.android.client.model.Gift;
import io.voucherify.android.client.model.VoucherResponse;

import static org.junit.Assert.assertEquals;

public class VoucherifyUtilsTest {

    @Test
    public void calculatePricePercentage() throws Exception {
        testPricePercentageDiscount("discount > 0", 10, 10, 9.0);
        testPriceAmountDiscount("discount ==0", 10, 0, 10);
    }

    @Test
    public void calculatePriceAmount() throws Exception {
        testPriceAmountDiscount("discount < price", 10, 10, 9.9);
        testPriceAmountDiscount("discount == price", 10, 1000, 0);
        testPriceAmountDiscount("discount > price", 10, 2000, 0);
        testPriceAmountDiscount("discount == 0", 10, 0, 10);
    }

    @Test
    public void calculatePriceUnit() throws Exception {
        testPriceUnitDiscount("unitOff > 0", 10, 1, 2, 8);
        testPriceUnitDiscount("unitOff == 0", 10, 1, 0, 10);
    }

    @Test
    public void calculateDiscount() throws Exception {

    }

    private void testPricePercentageDiscount(String description,
                                             double basePriceDouble,
                                             double discountDouble,
                                             double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.PERCENT, null, discountDouble, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        BigDecimal actual = VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void testPriceAmountDiscount(String description,
                                         double basePriceDouble,
                                         int discountInt,
                                         double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.AMOUNT, discountInt, null, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        BigDecimal actual = VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void testPriceUnitDiscount(String description,
                                       double basePriceDouble,
                                       double unitPriceDouble,
                                       double unitOffDouble,
                                       double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        BigDecimal unitPrice = new BigDecimal(unitPriceDouble);
        Discount discount = createDiscount(DiscountType.UNIT, null, null, unitOffDouble, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        BigDecimal actual = VoucherifyUtils.calculatePrice(basePrice, voucherResponse, unitPrice);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private VoucherResponse createVoucherResponse(String code,
                                                  boolean valid,
                                                  Discount discount,
                                                  Gift gift,
                                                  String reason,
                                                  String trackingId) {
        VoucherResponse result = new VoucherResponse();
        result.setCode(code);
        result.setValid(valid);
        result.setDiscount(discount);
        result.setGift(gift);
        result.setReason(reason);
        result.setTrackingId(trackingId);
        return result;
    }

    private Discount createDiscount(DiscountType type,
                                    Integer amountOff,
                                    Double percentOff,
                                    Double unitOff,
                                    String unitType) {
        Discount result = new Discount();
        result.setType(type);
        result.setAmountOff(amountOff);
        result.setPercentOff(percentOff);
        result.setUnitOff(unitOff);
        result.setUnitType(unitType);
        return result;
    }

}