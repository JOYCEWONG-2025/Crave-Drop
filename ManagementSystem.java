import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ManagementSystem {
    private LinkedList<User> userList;
    private LinkedList<Restaurant> restaurantList;
    private LinkedList<Admin> adminList;
    private final String userFile = "users.txt";
    private final String adminFile = "admin.txt";
    private final String restaurantFile = "restaurants.txt";
    private final String menuFile = "menus.txt";

    // Use HashMap for fast data retrieval
    private HashMap<String, User> userIndex;
    private HashMap<String, Restaurant> restaurantIndex;
    private HashMap<String, Admin> adminIndex;

    public ManagementSystem() {
        this.userList = new LinkedList<>();
        this.restaurantList = new LinkedList<>();
        this.userIndex = new HashMap<>();
        this.restaurantIndex = new HashMap<>();
        this.adminIndex = new HashMap<>();
        this.adminList = new LinkedList<>();
        loadUsersFromFile();
        loadAdminsFromFile();
        loadRestaurantsFromFile();
        loadMenusFromFile();
    }

    // Restaurant Management
    public void addRestaurant(Restaurant restaurant) {
        restaurantList.add(restaurant);
        restaurantIndex.put(restaurant.getRestaurantID().toUpperCase(), restaurant);
        System.out.println("Restaurant '" + restaurant.getRestaurantName() + "' with id " + restaurant.getRestaurantID()
                + " added successfully.");
        saveRestaurantsToFile();
    }

    public void removeRestaurant(String restaurantID) {
        Restaurant res = restaurantIndex.get(restaurantID.toUpperCase());
        if (res != null) {
            restaurantList.remove(res);
            restaurantIndex.remove(restaurantID.toUpperCase());
            System.out.println("Restaurant ID " + restaurantID + " removed.");
        } else {
            System.out.println("Restaurant not found.");
        }
        saveRestaurantsToFile();
        saveAllMenuItemsToFile();
    }

    // Restaurant management
    public Restaurant getRestaurant(String restaurantID) {
        return restaurantIndex.get(restaurantID.toUpperCase());
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantList;
    }

    // Inside ManagementSystem.java
    public List<Restaurant> searchRestaurantsByKeyword(String keyword) {
        List<Restaurant> matchingRestaurants = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase(); // For case-insensitive matching

        for (Restaurant r : restaurantIndex.values()) { // or restaurantList
            String name = r.getRestaurantName().toLowerCase();
            String location = r.getLocationNode().toLowerCase();
            String cuisine = r.getFoodCategory().toLowerCase();
            String id = r.getRestaurantID().toLowerCase();

            // Check if the keyword is contained in the name, location, or cuisine type!
            if (name.contains(lowerKeyword) || location.contains(lowerKeyword) || cuisine.contains(lowerKeyword)
                    || id.contains(lowerKeyword)) {
                matchingRestaurants.add(r);
            }
        }
        return matchingRestaurants;
    }

    public int getTotalRestaurants() {
        return restaurantList.size();
    }

    public void displayAllRestaurants() {
        System.out.println("\nRestaurant List: ");
        for (Restaurant r : restaurantList) {
            System.out.println(r.toString() + "\n-------------------");
        }
    }

    // Load restaurants from text file
    private void loadRestaurantsFromFile() {
        File file = new File(restaurantFile);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(restaurantFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String restaurantId = parts[0].trim();
                    String restaurantName = parts[1].trim();
                    String location = parts[2].trim();
                    String foodCategory = parts[3].trim();
                    Restaurant restaurant = new Restaurant(restaurantId, restaurantName, location, foodCategory);
                    restaurantList.add(restaurant);
                    restaurantIndex.put(restaurantId.toUpperCase(), restaurant);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading restaurants' information from file: " + e.getMessage());
        }
    }

    public void saveRestaurantsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(restaurantFile))) {
            for (Restaurant restaurant : restaurantList) {
                writer.write(restaurant.getRestaurantID() + "," + restaurant.getRestaurantName() + ","
                        + restaurant.getLocationNode() + "," + restaurant.getFoodCategory());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving restaurants to text file: " + e.getMessage());
        }
    }

    // Load menu items from file into each restaurant on startup
    private void loadMenusFromFile() {
        File file = new File(menuFile);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(menuFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] parts = line.split(",", 4);
                if (parts.length == 4) {
                    String restaurantID = parts[0].trim();
                    String foodName = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    String category = parts[3].trim();

                    Restaurant restaurant = getRestaurant(restaurantID);
                    if (restaurant != null) {
                        restaurant.addMenuItem(new FoodItem(restaurantID, foodName, price, category));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading menu items from file: " + e.getMessage());
        }
    }

    // Append one new menu item to file — called when admin adds item
    public void appendMenuItemToFile(FoodItem item) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(menuFile, true))) {
            writer.write(item.getRestaurantID() + "," + item.getFoodName() + ","
                    + item.getPrice() + "," + item.getCategory());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving menu item to file: " + e.getMessage());
        }
    }

    public void saveAllMenuItemsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(menuFile))) {
            for (Restaurant restaurant : restaurantList) {
                for (FoodItem item : restaurant.getMenuItems()) {
                    writer.write(item.getRestaurantID() + "," + item.getFoodName() + ","
                            + item.getPrice() + "," + item.getCategory());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving menu items to file: " + e.getMessage());
        }
    }

    // User Management
    public boolean addUser(User user) {
        String upperID = user.getUserID().toUpperCase();
        if (userIndex.containsKey(upperID)) {
            return false;
        }
        userList.add(user);
        userIndex.put(upperID, user);
        System.out.println("Welcome, User '" + user.getUserName() + "'!");
        saveUsersToFile();
        return true;
    }

    // Check for duplicate phone numbers
    public boolean hasDuplicatePhoneNumber(String phoneNumber) {
        for (User existingUser : userList) {
            if (existingUser.getUserPhoneNumber().equals(phoneNumber)) {
                System.out.println("\n[Registration Error] Phone number '" + phoneNumber
                        + "' is already registered under the name '" + existingUser.getUserName() + "'.");
                return true; // Duplicate found
            }
        }
        return false;
    }

    public void removeUser(String userID) {
        User user = userIndex.get(userID);
        if (user != null) {
            userList.remove(user);
            userIndex.remove(userID);
            System.out.println("User ID " + userID + " removed.");
            saveUsersToFile();
        } else {
            System.out.println("User not found.");
        }
    }

    public User getUser(String id) {
        return userIndex.get(id.toUpperCase());
    }

    public void displayAllUsers() {
        System.out.println("\nUser List: ");
        for (User u : userList) {
            System.out.println(u.toString() + "\n-------------------");
        }
    }

    // Save all users currently into text file
    public void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile))) {
            for (User user : userList) {
                writer.write(user.getUserID() + "," + user.getUserName() + "," + user.getUserPhoneNumber() + ","
                        + user.getUserAddressNode() + "," + user.getUserPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users to text file: " + e.getMessage());
        }
    }

    // Load users from text file
    private void loadUsersFromFile() {
        File file = new File(userFile);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String phone = parts[2].trim();
                    String location = parts[3].trim();
                    String password = parts[4].trim();
                    User user = new User(id, name, phone, location, password);

                    userList.add(user);
                    userIndex.put(id.toUpperCase(), user);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users' information from file: " + e.getMessage());
        }
    }

    // admin management
    public void addAdmin(Admin admin) {
        if (adminIndex.containsKey(admin.getAdminID())) {
            System.out.println("Admin ID '" + admin.getAdminID() + "' already exists!");
            return;
        }

        adminList.add(admin);
        adminIndex.put(admin.getAdminID(), admin);
        System.out.println("Admin '" + admin.getAdminName() + "' added successfully.");

        saveAdminsToFile();
    }

    public void removeAdmin(String adminID) {
        Admin admin = adminIndex.get(adminID);
        if (admin != null) {
            adminList.remove(admin);
            adminIndex.remove(adminID);
            System.out.println("Admin ID " + adminID + " removed.");
            saveAdminsToFile();
        } else {
            System.out.println("Admin not found.");
        }
    }

    public Admin getAdmin(String adminID) {
        return adminIndex.get(adminID);
    }

    // Load admins from text file
    private void loadAdminsFromFile() {
        File file = new File(adminFile);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(adminFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String adminId = parts[0].trim();
                    String adminName = parts[1].trim();
                    String password = parts[2].trim();
                    Admin admin = new Admin(adminId, adminName, password);
                    adminList.add(admin);
                    adminIndex.put(adminId.toUpperCase(), admin);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading admins' information from file: " + e.getMessage());
        }
    }

    private void saveAdminsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(adminFile))) {
            for (Admin admin : adminList) {
                writer.write(admin.getAdminID() + "," + admin.getAdminName() + "," + admin.getAdminPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving admins to text file: " + e.getMessage());
        }
    }

    public void displayAllAdmins() {
        System.out.println("\nAdmin List: ");
        for (Admin a : adminList) {
            System.out.println(a.toString() + "\n-------------------");
        }
    }
}