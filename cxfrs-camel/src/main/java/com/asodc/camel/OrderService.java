package com.asodc.camel;

public interface OrderService {

    Order getOrder(int orderId);

    Order createOrder(Order order);

    Order updateOrder(Order order);

    void deleteOrder(int orderId);

}
