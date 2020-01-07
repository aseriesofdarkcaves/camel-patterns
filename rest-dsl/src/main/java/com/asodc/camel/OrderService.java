package com.asodc.camel;

public interface OrderService {
    Order getOrder(int id);

    Order createOrder(Order order);

    Order updateOrder(Order order);

    void deleteOrder(int id);
}
