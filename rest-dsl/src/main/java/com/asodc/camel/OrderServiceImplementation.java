package com.asodc.camel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderServiceImplementation implements OrderService {
    private Map<Integer, Order> orders = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger();

    // implicit default constructor

    @Override
    public Order getOrder(int id) {
        return orders.get(id);
    }

    @Override
    public Order createOrder(Order order) {
        int generatedId = idGenerator.incrementAndGet();
        order.setId(generatedId);
        orders.put(generatedId, order);
        return order;
    }

    @Override
    public Order updateOrder(Order order) {
        orders.put(order.getId(), order);
        return order;
    }

    @Override
    public void deleteOrder(int id) {
        orders.remove(id);
    }
}
