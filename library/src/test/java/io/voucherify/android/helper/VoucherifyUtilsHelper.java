package io.voucherify.android.helper;

import java.math.BigDecimal;

import io.voucherify.android.client.VoucherifyUtils;
import io.voucherify.android.client.model.Discount;
import io.voucherify.android.client.model.DiscountType;
import io.voucherify.android.client.model.Gift;
import io.voucherify.android.client.model.VoucherResponse;

import static io.voucherify.android.helper.ModelHelper.createFakeDiscount;
import static io.voucherify.android.helper.ModelHelper.createFakeVoucherResponse;

public class VoucherifyUtilsHelper {
    public static BigDecimal calculatePricePercentage(Double basePriceDouble, Double discountDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createFakeDiscount(DiscountType.PERCENT, null, discountDouble, null, null);
        VoucherResponse voucherResponse = createFakeVoucherResponse("", true, discount, null, null, null);
        return VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
    }

    public static BigDecimal calculatePriceAmount(Double basePriceDouble, Integer discountInt) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createFakeDiscount(DiscountType.AMOUNT, discountInt, null, null, null);
        VoucherResponse voucherResponse = createFakeVoucherResponse("", true, discount, null, null, null);
        return VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
    }

    public static BigDecimal calculatePriceUnit(Double basePriceDouble, Double unitPriceDouble, Double unitOffDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        BigDecimal unitPrice = new BigDecimal(unitPriceDouble);
        Discount discount = createFakeDiscount(DiscountType.UNIT, null, null, unitOffDouble, null);
        VoucherResponse voucherResponse = createFakeVoucherResponse("", true, discount, null, null, null);
        return VoucherifyUtils.calculatePrice(basePrice, voucherResponse, unitPrice);
    }

    public static BigDecimal calculatePriceGift(Double basePriceDouble, Integer giftBalanceInt) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Gift gift = new Gift(giftBalanceInt, giftBalanceInt);
        VoucherResponse voucherResponse = createFakeVoucherResponse("", true, null, gift, null, null);
        return VoucherifyUtils.calculatePrice(basePrice, voucherResponse, null);
    }

    public static BigDecimal calculateDiscountPercentage(Double basePriceDouble, Double discountDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createFakeDiscount(DiscountType.PERCENT, null, discountDouble, null, null);
        VoucherResponse voucherResponse = createFakeVoucherResponse("", true, discount, null, null, null);
        return VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, null);
    }

    public static BigDecimal calculateDiscountAmount(Double basePriceDouble, Integer discountInt) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Discount discount = createFakeDiscount(DiscountType.AMOUNT, discountInt, null, null, null);
        VoucherResponse voucherResponse = createFakeVoucherResponse("", true, discount, null, null, null);
        return VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, null);
    }

    public static BigDecimal calculateDiscountUnit(Double basePriceDouble, Double unitPriceDouble, Double unitOffDouble) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        BigDecimal unitPrice = new BigDecimal(unitPriceDouble);
        Discount discount = createFakeDiscount(DiscountType.UNIT, null, null, unitOffDouble, null);
        VoucherResponse voucherResponse = createFakeVoucherResponse("", true, discount, null, null, null);
        return VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, unitPrice);
    }

    public static BigDecimal calculateDiscountGift(Double basePriceDouble, Integer giftBalanceInt) {
        BigDecimal basePrice = new BigDecimal(basePriceDouble);
        Gift gift = new Gift(giftBalanceInt, giftBalanceInt);
        VoucherResponse voucherResponse = createFakeVoucherResponse("", true, null, gift, null, null);
        return VoucherifyUtils.calculateDiscount(basePrice, voucherResponse, null);
    }
}
