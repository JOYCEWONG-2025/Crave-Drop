public class OrderItem {
    private String foodName;
    private double price;

    public OrderItem(FoodItem foodItem) {
        this.foodName = foodItem.getFoodName();
        this.price = foodItem.getPrice();
    }

    public String getFoodName() {
        return foodName;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%-25s RM%.2f", foodName, price);
    }
}