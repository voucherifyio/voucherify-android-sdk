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
        testPricePercentage("discount > 0", 10.0, 10.0, 9.0);
        testPricePercentage("discount == 0", 10.0, 0.0, 10.0);
    }

    @Test
    public void calculatePriceAmount() throws Exception {
        testPriceAmount("discount < price", 10.0, 10, 9.9);
        testPriceAmount("discount == price", 10.0, 1000, 0.0);
        testPriceAmount("discount > price", 10.0, 2000, 0.0);
        testPriceAmount("discount == 0", 10.0, 0, 10.0);
    }

    @Test
    public void calculatePriceUnit() throws Exception {
        testPriceUnit("unitOff > 0", 10.0, 1.0, 2.0, 8.0);
        testPriceUnit("unitOff == 0", 10.0, 1.0, 0.0, 10.0);
    }

    @Test
    public void calculatePriceGift() throws Exception {
        testPriceGift("giftBalance < price", 10.0, 100, 9.0);
        testPriceGift("giftBalance == 0", 10.0, 0, 10.0);
        testPriceGift("giftBalance == price", 10.0, 10000, 0.0);
        testPriceGift("giftBalance > price", 10.0, 20000, 0.0);
        testPriceGift("giftBalance < 0", 10.0, -100, 11.0);
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
        testPricePercentage("discount > 100", 10.0, 110.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePricePercentageLessThanZero() throws Exception {
        testPricePercentage("discount < 0", 10.0, -10.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePricePercentageNull() throws Exception {
        testPricePercentage("discount == null", 10.0, null, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePriceAmountLessThanZero() throws Exception {
        testPriceAmount("discount < 0", 10.0, -10, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePriceAmountNull() throws Exception {
        testPriceAmount("discount == null", 10.0, null, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePriceUnitLessThanZero() throws Exception {
        testPriceUnit("unitOff < 0", 10.0, 1.0, -2.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePriceUnitNull() throws Exception {
        testPriceUnit("unitOff == null", 10.0, 1.0, null, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePriceGiftNull() throws Exception {
        testPriceGift("giftBalance == null", 10.0, null, 0.0);
    }

    @Test
    public void calculateDiscountPercentage() throws Exception {
        testDiscountPercentage("discount > 0", 10.0, 10.0, 1.0);
        testDiscountPercentage("discount == 0", 10.0, 0.0, 0.0);
    }

    @Test
    public void calculateDiscountAmount() throws Exception {
        testDiscountAmount("discount < price", 10.0, 10, 0.1);
        testDiscountAmount("discount == price", 10.0, 1000, 10.0);
        testDiscountAmount("discount > price", 10.0, 2000, 10.0);
        testDiscountAmount("discount == 0", 10.0, 0, 0.0);
    }

    @Test
    public void calculateDiscountUnit() throws Exception {
        testDiscountUnit("unitOff > 0", 10.0, 1.0, 2.0, 2.0);
        testDiscountUnit("unitOff == 0", 10.0, 1.0, 0.0, 0.0);
    }

    @Test
    public void calculateDiscountGift() throws Exception {
        testDiscountGift("giftBalance < price", 10.0, 100, 1.0);
        testDiscountGift("giftBalance == 0", 10.0, 0, 0.0);
        testDiscountGift("giftBalance == price", 10.0, 10000, 10.0);
        testDiscountGift("giftBalance > price", 10.0, 20000, 10.0);
        testDiscountGift("giftBalance < 0", 10.0, -100, -1.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscountInvalidType() throws Exception {
        BigDecimal basePrice = new BigDecimal(10);
        Discount discount = createDiscount(null, null, null, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, null);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscountPercentageMoreThanHundred() throws Exception {
        testDiscountPercentage("discount > 100", 10.0, 110.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscountPercentageLessThanZero() throws Exception {
        testPricePercentage("discount < 0", 10.0, -10.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscountPercentageNull() throws Exception {
        testDiscountPercentage("discount == null", 10.0, null, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscountAmountLessThanZero() throws Exception {
        testDiscountAmount("discount < 0", 10.0, -10, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscountAmountNull() throws Exception {
        testDiscountAmount("discount == null", 10.0, null, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscountUnitLessThanZero() throws Exception {
        testDiscountUnit("unitOff < 0", 10.0, 1.0, -2.0, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscountUnitNull() throws Exception {
        testDiscountUnit("unitOff == null", 10.0, 1.0, null, 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscountGiftNull() throws Exception {
        testDiscountGift("giftBalance == null", 10.0, null, 0.0);
    }

    private void testPricePercentage(String description,
                                     Double basePriceDouble,
                                     Double discountDouble,
                                     Double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.PERCENT, null, discountDouble, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        BigDecimal actual = VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void testPriceAmount(String description,
                                 Double basePriceDouble,
                                 Integer discountInt,
                                 Double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.AMOUNT, discountInt, null, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        BigDecimal actual = VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void testPriceUnit(String description,
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

    private void testPriceGift(String description,
                               Double basePriceDouble,
                               Integer giftBalanceInt,
                               Double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Gift gift = new Gift(giftBalanceInt, giftBalanceInt);
        VoucherResponse voucherResponse = createVoucherResponse("", true, null, gift, null, null);
        BigDecimal actual = VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void testDiscountPercentage(String description,
                                        Double basePriceDouble,
                                        Double discountDouble,
                                        Double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.PERCENT, null, discountDouble, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        BigDecimal actual = VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, null);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void testDiscountAmount(String description,
                                    Double basePriceDouble,
                                    Integer discountInt,
                                    Double expectedDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.AMOUNT, discountInt, null, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        BigDecimal actual = VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, null);
        assertEquals(description, expectedDouble, actual.doubleValue(), 0.01);
    }

    private void testDiscountUnit(String description,
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

    private void testDiscountGift(String description,
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
