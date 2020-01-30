package com.asodc.camel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderServiceImplementation implements OrderService {

    // in memory dummy order system
    private Map<Integer, Order> orders = new HashMap<>();

    private final AtomicInteger idGen = new AtomicInteger();

    public OrderServiceImplementation() {
        setupDummyOrders();
    }

    public void setupDummyOrders() {
        Order order = new Order();
        order.setAmount(1);
        createOrder(order);

        order = new Order();
        order.setAmount(3);
        createOrder(order);
    }

    @Override
    public Order getOrder(int id) {
        return orders.get(id);
    }

    @Override
    public Order createOrder(Order order) {
        int id = idGen.incrementAndGet();
        order.setId(id);
        orders.put(id, order);
        return order;
    }

    @Override
    public Order updateOrder(Order order) {
        int id = order.getId();
        orders.remove(id);
        orders.put(id, order);
        return order;
    }

    @Override
    public void deleteOrder(int id) {
        orders.remove(id);
    }
}
