package io.voucherify.android.client.model;

import java.util.List;
import java.util.Map;

public class Order implements Querable{
    private Integer amount;
    private List<OrderItem> items;

    public Order() {
    }

    public Order(Integer amount, List<OrderItem> items) {
        this.amount = amount;
        this.items = items;
    }

    public static Order amount(int amount) {
        return new Order(amount, null);
    }

    public Integer getAmount() {
        return amount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    @Override
    public void createQuery(Map<String, String> queryParams) {
        if (amount != null) {
            queryParams.put("amount", amount.toString());
        }
        if (items != null) {
            int index = 0;
            for (OrderItem item : items) {
                queryParams.put("item[" + index + "][product_id]", item.getProductId());
                queryParams.put("item[" + index + "][sku_id]", item.getSkuId());
                queryParams.put("item[" + index + "][price]",
                        item.getPrice() != null ? item.getPrice().toString() : null);
                queryParams.put("item[" + index + "][quantity]",
                        item.getQuantity() != null ? item.getQuantity().toString() : null);
                index++;
            }
        }
    }
}