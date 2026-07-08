import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class OrderCart {

    // Stack = LIFO
    private Stack<OrderItem> cart;

    public OrderCart() {
        cart = new Stack<>();
    }

    // Push item
    public void addItem(OrderItem item) {

        cart.push(item);

        System.out.println(item.getFoodName() + " added to cart.");
    }

    // Pop item
    public void undoLastItem() {

        if (!cart.isEmpty()) {

            OrderItem removed = cart.pop();

            System.out.println("Removed: " + removed.getFoodName());

        } else {
            System.out.println("Cart is empty.");
        }
    }

    // Confirm order
    public List<OrderItem> confirmOrder() {
        List<OrderItem> finalizedItems = new ArrayList<>();
        while (!cart.isEmpty()) {
            finalizedItems.addAll(cart); 
            cart.clear();
        }
        return finalizedItems;
    }

    public void displayCart() {
        // Find longest item name for dynamic column width
        java.util.LinkedHashMap<String, int[]> grouped = new java.util.LinkedHashMap<>();
        if (!cart.isEmpty()) {
            for (OrderItem item : cart) {
                if (grouped.containsKey(item.getFoodName())) {
                    grouped.get(item.getFoodName())[0]++;
                    grouped.get(item.getFoodName())[1] += (int) (item.getPrice() * 100);
                } else {
                    grouped.put(item.getFoodName(), new int[] { 1, (int) (item.getPrice() * 100) });
                }
            }
        }

        int maxLen = 10;
        for (java.util.Map.Entry<String, int[]> entry : grouped.entrySet()) {
            if (entry.getKey().length() > maxLen)
                maxLen = entry.getKey().length();
        }

        String divider = "  " + "─".repeat(maxLen + 18);
        System.out.println(divider);

        if (cart.isEmpty()) {
            System.out.println("  Cart is empty.");
            System.out.println(divider);
            return;
        }

        int num = 1;
        for (java.util.Map.Entry<String, int[]> entry : grouped.entrySet()) {
            System.out.printf("  %d. %-" + maxLen + "s  x%-3d RM%.2f%n",
                    num++, entry.getKey(),
                    entry.getValue()[0],
                    entry.getValue()[1] / 100.0);
        }

        String totalLabel = "Total (" + cart.size() + " items):";
        System.out.println(divider);
        System.out.printf("  %-" + (maxLen + 9) + "s RM%.2f%n", totalLabel, calculateTotal());
        System.out.println(divider);
    }

    public double calculateTotal() {
        double total = 0;
        for (OrderItem item : cart) {
            total += item.getPrice();
        }
        return total;
    }

    public boolean isEmpty() {
        return cart.isEmpty();
    }
}
