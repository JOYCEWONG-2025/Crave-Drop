import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private String restaurantID;
    private String restaurantName;
    private String locationNode;
    private String foodCategory;
    // For Person 3 to store the food items available at this restaurant
    private List<FoodItem> menuItems;

    public Restaurant(String restaurantID, String restaurantName, String locationNode, String foodCategory) {
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.locationNode = locationNode;
        this.foodCategory = foodCategory;
        this.menuItems = new ArrayList<>();
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getLocationNode() {
        return locationNode;
    }

    public void setLocationNode(String locationNode) {
        this.locationNode = locationNode;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    // Methods for Menu Management (Supports Search & Recommendation rubric)
    public List<FoodItem> getMenuItems() {
        return menuItems;
    }

    public void addMenuItem(FoodItem item) {
        this.menuItems.add(item);
    }

    @Override
    public String toString() {
        return "\n[Restaurant Profile]" +
                "\nID      : " + restaurantID +
                "\nName    : " + restaurantName +
                "\nCategory: " + foodCategory +
                "\nLocation: " + locationNode;
    }
}
