package io.voucherify.android.client.model;

import java.util.List;

/**
 * Created by andrzejszmajnta on 03.04.2018.
 */

public class VouchersList {

    private Integer total;

    private List<Voucher> vouchers;

    public String getObject() {
        return "list";
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Voucher> getVouchers() {
        return vouchers;
    }

    public void setVouchers(List<Voucher> vouchers) {
        this.vouchers = vouchers;
    }
}
