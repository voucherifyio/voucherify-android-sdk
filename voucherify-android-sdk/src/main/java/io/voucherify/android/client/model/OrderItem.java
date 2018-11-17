package io.voucherify.android.client.model;

import com.google.gson.annotations.SerializedName;

public class OrderItem {

    @SerializedName("product_id")
    private String productId;

    @SerializedName("sku_id")
    private String skuId;

    private Integer quantity;

    private Integer price;

    public OrderItem() {
    }

    public OrderItem(String productId, String skuId, Integer quantity) {
        this.productId = productId;
        this.skuId = skuId;
        this.quantity = quantity;
    }

    public OrderItem(String productId, String skuId, Integer quantity, Integer price) {
        this.productId = productId;
        this.skuId = skuId;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public String getSkuId() {
        return skuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getPrice() {
        return price;
    }
}
