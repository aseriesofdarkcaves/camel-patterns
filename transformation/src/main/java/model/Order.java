package model;

import java.util.List;

public class Order {
    private String orderId;
    private List<Item> items;

    public Order() {}

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

//    public void setItems(List<Item> items) {
//        this.items = items;
//    }

    public void addItem(Item item) {
        this.items.add(item);
    }
}
