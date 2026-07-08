import java.util.List;

public class Order {
    private String orderID;
    private User customer;
    private Restaurant restaurant;
    private List<OrderItem> items;

    public Order(User customer, Restaurant restaurant, List<OrderItem> items) {
        this.orderID = "ORD" + System.currentTimeMillis() + "-" + (int)(Math.random()*1000);// Simple unique ID generation
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = items;
    }

    public String getOrderID() {
        return orderID;
    }

    public User getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    // Calculate total price
    public double calculateTotal() {
        double total = 0;

        for (OrderItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        // Group items by name for quantity display
        java.util.LinkedHashMap<String, int[]> grouped = new java.util.LinkedHashMap<>();
        for (OrderItem item : items) {
            if (grouped.containsKey(item.getFoodName())) {
                grouped.get(item.getFoodName())[0]++;
                grouped.get(item.getFoodName())[1] += (int) (item.getPrice() * 100);
            } else {
                grouped.put(item.getFoodName(), new int[] { 1, (int) (item.getPrice() * 100) });
            }
        }

        StringBuilder itemLines = new StringBuilder();
        boolean first = true;
        for (java.util.Map.Entry<String, int[]> entry : grouped.entrySet()) {
            String line = String.format("%-25s x%-2d RM%.2f",
                    entry.getKey(),
                    entry.getValue()[0],
                    entry.getValue()[1] / 100.0);
            if (first) {
                itemLines.append(line);
                first = false;
            } else {
                itemLines.append("\n             ").append(line);
            }
        }

        return "\n[Order Details]" +
                "\nOrder ID   : " + orderID +
                "\nCustomer   : " + customer.getUserName() +
                "\nRestaurant : " + restaurant.getRestaurantName() +
                "\nItems      : " + itemLines.toString() +
                "\nTotal      : RM" + String.format("%.2f", calculateTotal());
    }
}
