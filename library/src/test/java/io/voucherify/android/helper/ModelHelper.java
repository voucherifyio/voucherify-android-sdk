package io.voucherify.android.helper;

import io.voucherify.android.client.model.Discount;
import io.voucherify.android.client.model.DiscountType;
import io.voucherify.android.client.model.Gift;
import io.voucherify.android.client.model.VoucherResponse;

public class ModelHelper {
    public static VoucherResponse createVoucherResponse(String code,
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

    public static Discount createDiscount(DiscountType type,
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
