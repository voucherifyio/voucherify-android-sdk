package io.voucherify.android.client.model;

import java.util.Map;

public class RedemptionContext {

    private final Customer customer;

    private final Order order;

    private final Map<String, Object> metadata;

    public RedemptionContext(Customer customer, Order order, Map<String, Object> metadata) {
        this.customer = customer;
        this.order = order;
        this.metadata = metadata;
    }

    public RedemptionContext(Customer customer, Order order) {
        this(customer, order, null);
    }

    public RedemptionContext(Customer customer, Map<String, Object> metadata) {
        this(customer, null, metadata);
    }

    public RedemptionContext(Customer customer) {
        this(customer, null, null);
    }

    public Customer getCustomer() {
        return customer;
    }

    public Order getOrder() {
        return order;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

}
