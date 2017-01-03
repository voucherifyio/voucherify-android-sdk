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
        testPricePercentageDiscount("discount > 0", 10.0, 10.0, 9.0);
        testPricePercentageDiscount("discount == 0", 10.0, 0.0, 10.0);
    }

    @Test
    public void calculatePriceAmount() throws Exception {
        testPriceAmountDiscount("discount < price", 10.0, 10, 9.9);
        testPriceAmountDiscount("discount == price", 10.0, 1000, 0.0);
        testPriceAmountDiscount("discount > price", 10.0, 2000, 0.0);
        testPriceAmountDiscount("discount == 0", 10.0, 0, 10.0);
    }

    @Test
    public void calculatePriceUnit() throws Exception {
        testPriceUnitDiscount("unitOff > 0", 10.0, 1.0, 2.0, 8.0);
        testPriceUnitDiscount("unitOff == 0", 10.0, 1.0, 0.0, 10.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePriceInvalidType() throws Exception {
        BigDecimal basePrice = new BigDecimal(10);
        Discount discount = createDiscount(null, null, null, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePricePercentageMoreThanHundred() throws Exception {
        testPricePercentageDiscount("discount > 100", 10.0, 110.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePricePercentageLessThanZero() throws Exception {
        testPricePercentageDiscount("discount < 0", 10.0, -10.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePricePercentageNull() throws Exception {
        testPricePercentageDiscount("discount == null", 10.0, null, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePriceAmountLessThanZero() throws Exception {
        testPriceAmountDiscount("discount < 0", 10.0, -10, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePriceAmountNull() throws Exception {
        testPriceAmountDiscount("discount == null", 10.0, null, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePriceUnitLessThanZero() throws Exception {
        testPriceUnitDiscount("unitOff < 0", 10.0, 1.0, -2.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePriceUnitNull() throws Exception {
        testPriceUnitDiscount("unitOff == null", 10.0, 1.0, null, 0.0);
    }

    @Test
    public void calculateDiscount() throws Exception {

    }

    private void testPricePercentageDiscount(String description,
                                             Double basePriceDouble,
                                             Double discountDouble,
                                             Double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.PERCENT, null, discountDouble, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        BigDecimal actual = VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void testPriceAmountDiscount(String description,
                                         Double basePriceDouble,
                                         Integer discountInt,
                                         Double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.AMOUNT, discountInt, null, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        BigDecimal actual = VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void testPriceUnitDiscount(String description,
                                       Double basePriceDouble,
                                       Double unitPriceDouble,
                                       Double unitOffDouble,
                                       Double expectedDouble) {
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
