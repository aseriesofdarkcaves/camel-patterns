package model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private List<Item> items;

    public Order() {
        this.items = new ArrayList<>();
    }

    public Order(String orderId) {
        this();
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Item getItem(String id) {
        for (Item item : items) {
            if (item.getItemId().equalsIgnoreCase(id)) {
                return item;
            }
        }
        return null;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }
}
