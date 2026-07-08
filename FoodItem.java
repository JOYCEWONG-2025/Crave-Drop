public class FoodItem {
    private String restaurantID;
    private String foodName;
    private double price;
    private String category;

    public FoodItem(String restaurantID, String foodName, double price, String category) {
        this.restaurantID = restaurantID;
        this.foodName = foodName;
        this.price = price;
        this.category = category;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format("%-25s | %-12s | RM%.2f", foodName, category, price);
    }
}