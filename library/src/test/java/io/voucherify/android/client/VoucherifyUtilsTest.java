package io.voucherify.android.client;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        BigDecimal expected = BigDecimal.valueOf(9.00).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculatePricePercentage(10.0, 10.0);
        assertEquals("discount > 0", expected, actual);
    }

    @Test
    public void calculatePrice_percentageDiscountEqualsZero_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(10.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculatePricePercentage(10.0, 0.0);
        assertEquals("discount > 0", expected, actual);
    }

    //amount
    @Test
    public void calculatePrice_amountDiscountLessThanPrice_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(9.9).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculatePriceAmount(10.0, 10);
        assertEquals("discount < price", expected, actual);
    }

    @Test
    public void calculatePrice_amountDiscountEqualsPrice_returnZero() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculatePriceAmount(10.0, 1000);
        assertEquals("discount == price", expected, actual);
    }

    @Test
    public void calculatePrice_amountDiscountGreaterThanPrice_returnZero() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculatePriceAmount(10.0, 2000);
        assertEquals("discount > price", expected, actual);
    }

    @Test
    public void calculatePrice_amountDiscountEqualsZero_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(10.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculatePriceAmount(10.0, 0);
        assertEquals("discount == 0", expected, actual);
    }

    // unit
    @Test
    public void calculatePrice_unitDiscountGreaterThanZero_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(8.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculatePriceUnit(10.0, 1.0, 2.0);
        assertEquals("unitOff > 0", expected, actual);
    }

    @Test
    public void calculatePrice_unitDiscountEqualZero_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(10.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculatePriceUnit(10.0, 1.0, 0.0);
        assertEquals("unitOff == 0", expected, actual);
    }

    // gift
    @Test
    public void calculatePrice_giftBalanceLessThanPrice_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(9.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculatePriceGift(10.0, 100);
        assertEquals("giftBalance < price", expected, actual);
    }

    @Test
    public void calculatePrice_giftBalanceEqualPrice_returnZero() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculatePriceGift(10.0, 10000);
        assertEquals("giftBalance == price", expected, actual);
    }

    @Test
    public void calculatePrice_giftBalanceGreaterThanPrice_returnZero() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculatePriceGift(10.0, 20000);
        assertEquals("giftBalance > price", expected, actual);
    }

    @Test
    public void calculatePrice_giftBalanceEqualZero_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(10.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculatePriceGift(10.0, 0);
        assertEquals("giftBalance == 0", expected, actual);
    }

    @Test
    public void calculatePrice_giftBalanceLessThanZero_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(11.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculatePriceGift(10.0, -100);
        assertEquals("giftBalance < 0", expected, actual);
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
        calculatePricePercentage(10.0, 110.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_percentageLessThanZero_throwException() throws Exception {
        calculatePricePercentage(10.0, -10.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_percentageNull_throwException() throws Exception {
        calculatePricePercentage(10.0, null);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_amountLessThanZero_throwException() throws Exception {
        calculatePriceAmount(10.0, -10);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_amountNull_throwException() throws Exception {
        calculatePriceAmount(10.0, null);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_unitLessThanZero_throwException() throws Exception {
        calculatePriceUnit(10.0, 1.0, -2.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_unitNull_throwException() throws Exception {
        calculatePriceUnit(10.0, 1.0, null);
    }

    @Test(expected = RuntimeException.class)
    public void calculatePrice_giftNull_throwException() throws Exception {
        calculatePriceGift(10.0, null);
    }

    // discount
    // percentage
    @Test
    public void calculateDiscount_percentageDiscountGreaterThanZero_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(1.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculateDiscountPercentage(10.0, 10.0);
        assertEquals("discount > 0", expected, actual);
    }

    @Test
    public void calculateDiscount_percentageDiscountEqualsZero_returnZero() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculateDiscountPercentage(10.0, 0.0);
        assertEquals("discount == 0", expected, actual);
    }

    // amount
    @Test
    public void calculateDiscount_amountDiscountLessThanPrice_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(0.1).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculateDiscountAmount(10.0, 10);
        assertEquals("discount < price", expected, actual);
    }

    @Test
    public void calculateDiscount_amountDiscountEqualPrice_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(10.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculateDiscountAmount(10.0, 1000);
        assertEquals("discount == price", expected, actual);
    }

    @Test
    public void calculateDiscount_amountDiscountGreaterThanPrice_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(10.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculateDiscountAmount(10.0, 2000);
        assertEquals("discount > price", expected, actual);
    }

    @Test
    public void calculateDiscount_amountDiscountEqualZero_returnZero() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculateDiscountAmount(10.0, 0);
        assertEquals("discount == 0", expected, actual);
    }

    // unit
    @Test
    public void calculateDiscount_unitDiscountGreaterThanZero_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(2.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculateDiscountUnit(10.0, 1.0, 2.0);
        assertEquals("unitOff > 0", expected, actual);
    }

    @Test
    public void calculateDiscount_unitDiscountEqualZero_returnZero() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculateDiscountUnit(10.0, 1.0, 0.0);
        assertEquals("unitOff == 0", expected, actual);
    }

    // gift
    @Test
    public void calculateDiscount_giftBalanceLessThanPrice_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(1.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculateDiscountGift(10.0, 100);
        assertEquals("giftBalance < price", expected, actual);
    }

    @Test
    public void calculateDiscount_giftBalanceGreaterThanPrice_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(10.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculateDiscountGift(10.0, 20000);
        assertEquals("giftBalance > price", expected, actual);
    }

    @Test
    public void calculateDiscount_giftBalanceEqualPrice_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(10.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculateDiscountGift(10.0, 10000);
        assertEquals("giftBalance == price", expected, actual);
    }

    @Test
    public void calculateDiscount_giftBalanceEqualZero_returnZero() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculateDiscountGift(10.0, 0);
        assertEquals("giftBalance == 0", expected, actual);
    }

    @Test
    public void calculateDiscount_giftBalanceLessThanZero_returnRightValue() throws Exception {
        BigDecimal expected = BigDecimal.valueOf(-1.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = calculateDiscountGift(10.0, -100);
        assertEquals("giftBalance < 0", expected, actual);
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
        calculateDiscountPercentage(10.0, 110.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_percentageLessThanZero_throwException() throws Exception {
        calculateDiscountPercentage(10.0, -10.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_percentageNull_throwException() throws Exception {
        calculateDiscountPercentage(10.0, null);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_amountLessThanZero_throwException() throws Exception {
        calculateDiscountAmount(10.0, -10);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_amountNull_throwException() throws Exception {
        calculateDiscountAmount(10.0, null);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_unitLessThanZero_throwException() throws Exception {
        calculateDiscountUnit(10.0, 1.0, -2.0);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_unitNull_throwException() throws Exception {
        calculateDiscountUnit(10.0, 1.0, null);
    }

    @Test(expected = RuntimeException.class)
    public void calculateDiscount_giftNull_throwException() throws Exception {
        calculateDiscountGift(10.0, null);
    }

    private BigDecimal calculatePricePercentage(Double basePriceDouble, Double discountDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.PERCENT, null, discountDouble, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        return VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
    }

    private BigDecimal calculatePriceAmount(Double basePriceDouble, Integer discountInt) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.AMOUNT, discountInt, null, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        return VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
    }

    private BigDecimal calculatePriceUnit(Double basePriceDouble, Double unitPriceDouble, Double unitOffDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        BigDecimal unitPrice = new BigDecimal(unitPriceDouble);
        Discount discount = createDiscount(DiscountType.UNIT, null, null, unitOffDouble, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        return VoucherifyUtils.calculatePrice(basePrice, voucherResponse, unitPrice);
    }

    private BigDecimal calculatePriceGift(Double basePriceDouble, Integer giftBalanceInt) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Gift gift = new Gift(giftBalanceInt, giftBalanceInt);
        VoucherResponse voucherResponse = createVoucherResponse("", true, null, gift, null, null);
        return VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
    }

    private BigDecimal calculateDiscountPercentage(Double basePriceDouble, Double discountDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.PERCENT, null, discountDouble, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        return VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, null);
    }

    private BigDecimal calculateDiscountAmount(Double basePriceDouble, Integer discountInt) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createDiscount(DiscountType.AMOUNT, discountInt, null, null, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        return VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, null);
    }

    private BigDecimal calculateDiscountUnit(Double basePriceDouble, Double unitPriceDouble, Double unitOffDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        BigDecimal unitPrice = new BigDecimal(unitPriceDouble);
        Discount discount = createDiscount(DiscountType.UNIT, null, null, unitOffDouble, null);
        VoucherResponse voucherResponse = createVoucherResponse("", true, discount, null, null, null);
        return VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, unitPrice);
    }

    private BigDecimal calculateDiscountGift(Double basePriceDouble, Integer giftBalanceInt) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Gift gift = new Gift(giftBalanceInt, giftBalanceInt);
        VoucherResponse voucherResponse = createVoucherResponse("", true, null, gift, null, null);
        return VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, null);
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
