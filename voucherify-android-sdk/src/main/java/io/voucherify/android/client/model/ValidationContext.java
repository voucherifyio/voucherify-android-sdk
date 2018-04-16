package io.voucherify.android.client.model;

import java.util.List;
import java.util.Map;

public class ValidationContext implements Querable {

    public static ValidationContext create(Integer amount) {
        return new ValidationContext(null, new Order(100, null));
    }

    public static ValidationContext create(Integer amount, List<OrderItem> items) {
        return new ValidationContext(null, new Order(100, items));
    }

    private final Customer customer;

    private final Order order;

    private final Map<String, Object> metadata;

    public ValidationContext(Customer customer, Order order, Map<String, Object> metadata) {
        this.customer = customer;
        this.order = order;
        this.metadata = metadata;
    }

    public ValidationContext(Customer customer, Order order) {
        this(customer, order, null);
    }

    public ValidationContext(Customer customer, Map<String, Object> metadata) {
        this(customer, null, metadata);
    }

    public ValidationContext(Customer customer) {
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

    @Override
    public void createQuery(Map<String, String> query) {
        if (customer != null) {
            customer.createQuery(query);
        }
        if (order != null) {
            order.createQuery(query);
        }
        if (metadata != null) {
            for (Map.Entry<String, Object> ob : metadata.entrySet()) {
                query.put("metadata[" + ob.getKey() + "]", ob.getValue().toString());
            }
        }
    }
}
