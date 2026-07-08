import java.util.LinkedList;
import java.util.Queue;

public class OrderQueue {

    // Queue = FIFO
    private Queue<Order> activeOrders;

    public OrderQueue() {
         activeOrders = new LinkedList<>();
    }

    // Add order to queue
    public void receiveOrder(Order order) {
        activeOrders.offer(order);
        System.out.println("Order " +order.getOrderID() +" received.");
    }

    // Process next order
    public Order processNextOrder() {

        if (!activeOrders.isEmpty()) {
            Order processing = activeOrders.poll();
            System.out.println("Processing Order: " + processing.getOrderID());
            return processing;

        } else {
            System.out.println("No active orders.");
            return null;
        }
    }

    public void displayActiveOrders() {

        System.out.println("\n[Active Orders]");
        if (activeOrders.isEmpty()) {
            System.out.println("No active orders.");
            return;
        }

        for (Order order : activeOrders) {
            System.out.println(order);
        }
    }
    public Order getOrderByID(String orderID) {
        for (Order order : activeOrders) {
            if (order.getOrderID().equals(orderID)) {
                return order;
            }
        }
        return null; // Not found
    }
}