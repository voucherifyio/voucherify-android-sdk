package io.voucherify.android.client;

import org.junit.Test;

import java.math.BigDecimal;

import io.voucherify.android.client.model.Discount;
import io.voucherify.android.client.model.DiscountType;
import io.voucherify.android.client.model.Gift;
import io.voucherify.android.client.model.VoucherResponse;

import static org.junit.Assert.assertEquals;

public class VoucherifyUtilsTest {

    // calculatePrice
    //percentage
    @Test
    public void calculatePrice_percentageDiscountGreaterThanZero_returnRightValue() throws Exception {
        assertPricePercentage("discount > 0", 10.0, 10.0, 9.0);
    }

    @Test
    public void calculatePrice_percentageDiscountEqualsZero_returnRightValue() throws Exception {
        assertPricePercentage("discount == 0", 10.0, 0.0, 10.0);
    }

    //amount
    @Test
    public void calculatePrice_amountDiscountLessThanPrice_returnRightValue() throws Exception {
        assertPriceAmount("discount < price", 10.0, 10, 9.9);
    }

    @Test
    public void calculatePrice_amountDiscountEqualsPrice_returnZero() throws Exception {
        assertPriceAmount("discount == price", 10.0, 1000, 0.0);
    }

    @Test
    public void calculatePrice_amountDiscountGreaterThanPrice_returnZero() throws Exception {
        assertPriceAmount("discount > price", 10.0, 2000, 0.0);
    }

    @Test
    public void calculatePrice_amountDiscountEqualsZero_returnRightValue() throws Exception {
        assertPriceAmount("discount == 0", 10.0, 0, 10.0);
    }

    // unit
    @Test
    public void calculatePrice_unitDiscountGreaterThanZero_returnRightValue() throws Exception {
        assertPriceUnit("unitOff > 0", 10.0, 1.0, 2.0, 8.0);
    }

    @Test
    public void calculatePrice_unitDiscountEqualZero_returnRightValue() throws Exception {
        assertPriceUnit("unitOff == 0", 10.0, 1.0, 0.0, 10.0);
    }

    // gift
    @Test
    public void calculatePrice_giftBalanceLessThanPrice_returnRightValue() throws Exception {
        assertPriceGift("giftBalance < price", 10.0, 100, 9.0);
    }

    @Test
    public void calculatePrice_giftBalanceEqualPrice_returnZero() throws Exception {
        assertPriceGift("giftBalance == price", 10.0, 10000, 0.0);
    }

    @Test
    public void calculatePrice_giftBalanceGreaterThanPrice_returnZero() throws Exception {
        assertPriceGift("giftBalance > price", 10.0, 20000, 0.0);
    }

    @Test
    public void calculatePrice_giftBalanceEqualZero_returnRightValue() throws Exception {
        assertPriceGift("giftBalance == 0", 10.0, 0, 10.0);
    }

    @Test
    public void calculatePrice_giftBalanceLessThanZero_returnRightValue() throws Exception {
        assertPriceGift("giftBalance < 0", 10.0, -100, 11.0);
    }

    // exception
    @Test(expected = RuntimeException.class)
    public void calculatePrice_invalidType_throwException() throws Exception {
        BigDecimal basePrice = new BigDecimal(10);
        Discount discount = createDiscount(null, null, null, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_percentageMoreThanHundred_throwException() throws Exception {
        assertPricePercentage("discount > 100", 10.0, 110.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_percentageLessThanZero_throwException() throws Exception {
        assertPricePercentage("discount < 0", 10.0, -10.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_percentageNull_throwException() throws Exception {
        assertPricePercentage("discount == null", 10.0, null, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_amountLessThanZero_throwException() throws Exception {
        assertPriceAmount("discount < 0", 10.0, -10, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_amountNull_throwException() throws Exception {
        assertPriceAmount("discount == null", 10.0, null, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_unitLessThanZero_throwException() throws Exception {
        assertPriceUnit("unitOff < 0", 10.0, 1.0, -2.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_unitNull_throwException() throws Exception {
        assertPriceUnit("unitOff == null", 10.0, 1.0, null, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_giftNull_throwException() throws Exception {
        assertPriceGift("giftBalance == null", 10.0, null, 0.0);
    }

    // discount
    // percentage
    @Test
    public void calculateDiscount_percentageDiscountGreaterThanZero_returnRightValue() throws Exception {
        assertDiscountPercentage("discount > 0", 10.0, 10.0, 1.0);
    }

    @Test
    public void calculateDiscount_percentageDiscountEqualsZero_returnZero() throws Exception {
        assertDiscountPercentage("discount == 0", 10.0, 0.0, 0.0);
    }

    // amount
    @Test
    public void calculateDiscount_amountDiscountLessThanPrice_returnRightValue() throws Exception {
        assertDiscountAmount("discount < price", 10.0, 10, 0.1);
    }

    @Test
    public void calculateDiscount_amountDiscountEqualPrice_returnRightValue() throws Exception {
        assertDiscountAmount("discount == price", 10.0, 1000, 10.0);
    }

    @Test
    public void calculateDiscount_amountDiscountGreaterThanPrice_returnRightValue() throws Exception {
        assertDiscountAmount("discount > price", 10.0, 2000, 10.0);
    }

    @Test
    public void calculateDiscount_amountDiscountEqualZero_returnZero() throws Exception {
        assertDiscountAmount("discount == 0", 10.0, 0, 0.0);
    }

    // unit
    @Test
    public void calculateDiscount_unitDiscountGreaterThanZero_returnRightValue() throws Exception {
        assertDiscountUnit("unitOff > 0", 10.0, 1.0, 2.0, 2.0);
    }

    @Test
    public void calculateDiscount_unitDiscountEqualZero_returnZero() throws Exception {
        assertDiscountUnit("unitOff == 0", 10.0, 1.0, 0.0, 0.0);
    }

    // gift
    @Test
    public void calculateDiscount_giftBalanceLessThanPrice_returnRightValue() throws Exception {
        assertDiscountGift("giftBalance < price", 10.0, 100, 1.0);
    }

    @Test
    public void calculateDiscount_giftBalanceGreaterThanPrice_returnRightValue() throws Exception {
        assertDiscountGift("giftBalance > price", 10.0, 20000, 10.0);
    }

    @Test
    public void calculateDiscount_giftBalanceEqualPrice_returnRightValue() throws Exception {
        assertDiscountGift("giftBalance == price", 10.0, 10000, 10.0);
    }

    @Test
    public void calculateDiscount_giftBalanceEqualZero_returnZero() throws Exception {
        assertDiscountGift("giftBalance == 0", 10.0, 0, 0.0);
    }

    @Test
    public void calculateDiscount_giftBalanceLessThanZero_returnRightValue() throws Exception {
        assertDiscountGift("giftBalance < 0", 10.0, -100, -1.0);
    }

    // exception
    @Test(expected = RuntimeException.class)
    public void calculateDiscount_invalidType_throwException() throws Exception {
        BigDecimal basePrice = new BigDecimal(10);
        Discount discount = createDiscount(null, null, null, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, null);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_percentageMoreThanHundred_throwException() throws Exception {
        assertDiscountPercentage("discount > 100", 10.0, 110.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_percentageLessThanZero_throwException() throws Exception {
        assertPricePercentage("discount < 0", 10.0, -10.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_percentageNull_throwException() throws Exception {
        assertDiscountPercentage("discount == null", 10.0, null, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_amountLessThanZero_throwException() throws Exception {
        assertDiscountAmount("discount < 0", 10.0, -10, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_amountNull_throwException() throws Exception {
        assertDiscountAmount("discount == null", 10.0, null, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_unitLessThanZero_throwException() throws Exception {
        assertDiscountUnit("unitOff < 0", 10.0, 1.0, -2.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_unitNull_throwException() throws Exception {
        assertDiscountUnit("unitOff == null", 10.0, 1.0, null, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_giftNull_throwException() throws Exception {
        assertDiscountGift("giftBalance == null", 10.0, null, 0.0);
    }

    private void assertPricePercentage(String description,
                                       Double basePriceDouble,
                                       Double discountDouble,
                                       Double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.PERCENT, null, discountDouble, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        BigDecimal actual = VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void assertPriceAmount(String description,
                                   Double basePriceDouble,
                                   Integer discountInt,
                                   Double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.AMOUNT, discountInt, null, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        BigDecimal actual = VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void assertPriceUnit(String description,
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

    private void assertPriceGift(String description,
                                 Double basePriceDouble,
                                 Integer giftBalanceInt,
                                 Double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Gift gift = new Gift(giftBalanceInt, giftBalanceInt);
        VoucherResponse voucherResponse = createVoucherResponse("", true, null, gift, null, null);
        BigDecimal actual = VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void assertDiscountPercentage(String description,
                                          Double basePriceDouble,
                                          Double discountDouble,
                                          Double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.PERCENT, null, discountDouble, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        BigDecimal actual = VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, null);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void assertDiscountAmount(String description,
                                      Double basePriceDouble,
                                      Integer discountInt,
                                      Double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.AMOUNT, discountInt, null, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        BigDecimal actual = VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, null);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void assertDiscountUnit(String description,
                                    Double basePriceDouble,
                                    Double unitPriceDouble,
                                    Double unitOffDouble,
                                    Double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        BigDecimal unitPrice = new BigDecimal(unitPriceDouble);
        Discount discount = createDiscount(DiscountType.UNIT, null, null, unitOffDouble, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        BigDecimal actual = VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, unitPrice);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void assertDiscountGift(String description,
                                    Double basePriceDouble,
                                    Integer giftBalanceInt,
                                    Double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Gift gift = new Gift(giftBalanceInt, giftBalanceInt);
        VoucherResponse voucherResponse = createVoucherResponse("", true, null, gift, null, null);
        BigDecimal actual = VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, null);
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
