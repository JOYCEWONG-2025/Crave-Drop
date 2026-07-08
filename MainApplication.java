
import java.util.List;
import java.util.Scanner;

public class MainApplication {

    private static Scanner scanner = new Scanner(System.in);
    private static CityMap myMap = new CityMap();
    private static ManagementSystem managementSystem = new ManagementSystem();
    private static OrderCart cartStack = new OrderCart();
    private static OrderQueue orderQueue = new OrderQueue();
    private static NavigationSystem nav;
    private static DeliveryScheduler deliveryScheduler = new DeliveryScheduler();
    private static MenuTree menuSystem = new MenuTree();
    private static Rider assignedRider = null;

    public static void main(String[] args) {
        myMap.addLocation("UM Central");
        myMap.addLocation("KK12 Hostel");
        myMap.addLocation("Faculty of Computer Science");
        myMap.addLocation("Main Library");
        myMap.addLocation("DTC");
        myMap.addLocation("KL Gateway Mall");
        myMap.addLocation("KL Gate");
        myMap.addLocation("Mid Valley Megamall");
        myMap.addLocation("KFC University");
        myMap.addLocation("Zus Coffee University Malaya");
        myMap.addLocation("PJ Gate");

        myMap.addRoad("UM Central", "DTC", 0.8, true); // oneway
        myMap.addRoad("UM Central", "Faculty of Computer Science", 1.3, false); // Two-way
        myMap.addRoad("UM Central", "Main Library", 0.45, true); // oneway
        myMap.addRoad("DTC", "KL Gate", 1.4, true);
        myMap.addRoad("DTC", "KK12 Hostel", 0.9, true);
        myMap.addRoad("KK12 Hostel", "KL Gate", 1.0, true);
        myMap.addRoad("KK12 Hostel", "Faculty of Computer Science", 2.5, false);
        myMap.addRoad("Zus Coffee University Malaya", "UM Central", 0.35, true);
        myMap.addRoad("Zus Coffee University Malaya", "DTC", 0.65, true);
        myMap.addRoad("PJ Gate", "UM Central", 0.75, true);
        myMap.addRoad("Main Library", "Zus Coffee University Malaya", 0.1, true);
        myMap.addRoad("KL Gate", "Main Library", 1.1, true);
        myMap.addRoad("PJ Gate", "Main Library", 0.35, true);
        myMap.addRoad("KL Gate", "PJ Gate", 1.3, true);
        myMap.addRoad("KL Gate", "Mid Valley Megamall", 3.2, false);
        myMap.addRoad("KL Gate", "KL Gateway Mall", 1.8, false);
        myMap.addRoad("KL Gateway Mall", "Mid Valley Megamall", 2.0, false);
        myMap.addRoad("KL Gateway Mall", "KFC University", 4.0, false);
        myMap.addRoad("KL Gateway Mall", "PJ Gate", 3.7, false);
        myMap.addRoad("PJ Gate", "KFC University", 1.0, false);

        nav = new NavigationSystem(myMap);

        // Keeps returning to portal selection after each logout
        boolean running = true;
        while (running) {
            System.out.println("\nWELCOME TO GOOD TECH SMART FOOD DELIVERY SYSTEM");
            System.out.println("Please select your portal to log in or sign up:");
            System.out.println("1. Customer Portal (Order Food & Track Delivery)");
            System.out.println("2. Restaurant Manager & Admin Portal");
            System.out.println("3. Exit System");
            System.out.print("Enter your choice (1-3): ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input!");
                scanner.nextLine();
                continue;
            }

            int portalChoice = scanner.nextInt();
            scanner.nextLine();

            switch (portalChoice) {
                case 1:
                    runCustomerPortal(scanner);
                    break;
                case 2:
                    runRestaurantPortal(scanner);
                    break;
                case 3:
                    System.out.println("Exiting system. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }
        scanner.close();
    }

    // Customer Portal
    public static void runCustomerPortal(Scanner scanner) {
        // login/sign up process as USER

        System.out.print("Please enter your user ID to log in / Sign up by default if not found):");
        String userID = scanner.next();
        scanner.nextLine(); // Consume the newline character after next()

        boolean isAuthenticated = false;

        do {
            // Primary User Verification Check
            if (managementSystem.getUser(userID) == null) {
                System.out.println("User not found. Creating new user profile...");
                System.out.print("Please enter your name: ");
                String userName = scanner.nextLine();

                String userPhoneNumber;
                String userLocation;
                String userPassword, confirmPassword;

                // Dedicated Phone Number & Info Registration Validation Loop
                while (true) {
                    System.out.print("Please enter your phone number: ");
                    userPhoneNumber = scanner.next();
                    scanner.nextLine(); // Consume the trailing newline

                    // --- REVISED: COMBINED CHECK WITH NO EXTRA LOOPS OR EXTRA IF-ELSE BLOCKS ---
                    if (!userPhoneNumber.matches("\\d{10,11}")
                            || managementSystem.hasDuplicatePhoneNumber(userPhoneNumber)) {
                        if (!userPhoneNumber.matches("\\d{10,11}")) {
                            System.out.println(
                                    "Invalid phone number format! It must contain only numbers and be 10 or 11 digits long.\n");
                        }
                        System.out.println("Please try again directly below.");
                        continue; // Immediately jumps back up to "Please enter your phone number: "
                    }

                    System.out.println("Please enter your location by selecting a number from the following list: ");
                    int i = 1;
                    for (String loc : myMap.getLocations()) {
                        System.out.println(i + ". " + loc);
                        i++;
                    }
                    System.out.print("Select a location by number: ");
                    int locationChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the trailing newline
                    if (locationChoice < 1 || locationChoice > myMap.getLocations().size()) {
                        System.out.println("Invalid selection.");
                        break;
                    }
                    userLocation = myMap.getLocations().get(locationChoice - 1);

                    // Password Construction and Match Verification Loop
                    while (true) {
                        System.out.print("Create your password: ");
                        userPassword = scanner.nextLine();
                        System.out.print("Confirm your password: ");
                        confirmPassword = scanner.nextLine();
                        if (!userPassword.equals(confirmPassword)) {
                            System.out.println("Passwords do not match. Please try again.");
                        } else {
                            break; // Exit password verification block
                        }
                    }

                    // Construct profile instance and run structural constraints mapping
                    User newUser = new User(userID, userName, userPhoneNumber, userLocation, userPassword);
                    boolean registrationSuccess = managementSystem.addUser(newUser);

                    if (registrationSuccess) {
                        System.out.println("User '" + userName + "' created successfully. Welcome " + userName + "!");
                        isAuthenticated = true; // Set to true because they are now safely in the database
                        break; // Break the registration loop because info is valid
                    }
                }

                if (isAuthenticated) {
                    break; // Safely exit outer authentication loop to proceed to customer portal
                }

            } else {
                // Handle standard existing user verification path
                System.out.print("Enter your password:");
                String userPassword = scanner.nextLine();
                if (userPassword.equals(managementSystem.getUser(userID).getUserPassword())) {
                    System.out.println("Welcome back " + managementSystem.getUser(userID).getUserName() + "!");
                    break; // Authenticated successfully. Exit main portal loop.
                } else {
                    System.out.println("Incorrect password. Please try again.");
                    System.out.print("Please enter your user ID to log in / Sign up by default if not found):");
                    userID = scanner.next();
                    scanner.nextLine(); // Consume the newline character after next()
                }
            }
        } while (true);

        cartStack = new OrderCart();
        assignedRider = null;

        // Now runCustomerPortal will never receive a null user profile!
        // check if user wants to change delivery location
        String userLocation = managementSystem.getUser(userID).getUserAddressNode();
        System.out.println("Delivery location set to: " + userLocation);
        System.out.print("Change delivery location? ('Y' for Yes/N for No): ");
        char changeLocation = scanner.next().trim().charAt(0);
        scanner.nextLine();
        if (changeLocation == 'Y' || changeLocation == 'y') {
            int i = 1;
            for (String loc : myMap.getLocations()) {
                System.out.println(i + ". " + loc);
                i++;
            }
            System.out.print("Select a new delivery location by number: ");
            int locChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline
            if (locChoice > 0 && locChoice <= myMap.getLocations().size()) {
                userLocation = myMap.getLocations().get(locChoice - 1);
                System.out.println("Delivery location updated to: " + userLocation);
                managementSystem.getUser(userID).setUserAddressNode(userLocation);
                managementSystem.saveUsersToFile();
            } else {
                System.out.println("Invalid selection. Keeping current location.");
            }
        } else if (changeLocation == 'N' || changeLocation == 'n') {
            System.out.println("Keeping current delivery location: " + userLocation);
        } else {
            System.out.println("Invalid input. Keeping current delivery location: " + userLocation);
        }

        // menu for customer portal
        String restaurantID = null;
        boolean orderPlaced = false;
        boolean inCustomerMenu = true;

        while (inCustomerMenu) {
            System.out.println("\n--- GOODTECH CUSTOMER PORTAL ---");
            System.out.println("1. Display all available restaurants");
            System.out.println("2. Search Food or Restaurant by Name/ID and add items to Cart");
            System.out.println("3. View Shopping Cart");
            System.out.println("4. Undo Last Item Added to Cart");
            System.out.println("5. Confirm Order & Checkout");
            System.out.println("6. Track Active Order & Route Map");
            System.out.println("7. Logout & Back");
            System.out.println("8. Delete Account");
            System.out.println("9. Change delivery location");
            System.out.print("Please select an option (1-9): ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("\nDisplaying all restaurants...");
                    managementSystem.displayAllRestaurants();
                    break;

                case 2:
                    System.out.print("\nEnter Restaurant Name, ID, Location or Cuisine keyword: ");
                    String searchTerm = scanner.nextLine().trim();
                    if (searchTerm.isEmpty()) {
                        System.out.println("Showing all restaurants:");
                        managementSystem.displayAllRestaurants();
                        break;
                    }

                    // 1. Get and store all partial keyword matches from the database
                    List<Restaurant> matches = managementSystem.searchRestaurantsByKeyword(searchTerm);

                    if (matches.isEmpty()) {
                        System.out.println("No restaurants found matching \"" + searchTerm + "\".");
                        break;
                    }

                    // This loop keeps the "Matches list" alive so users can return to it easily
                    boolean viewingShopList = true;
                    while (viewingShopList) {
                        Restaurant selectedRestaurant = null;

                        // 2. Resolve matching selections
                        if (matches.size() == 1) {
                            selectedRestaurant = matches.get(0);
                            System.out.println("\nFound: " + selectedRestaurant.getRestaurantName());
                            // Turn off list viewing since there's nowhere else to go back to
                            viewingShopList = false;
                        } else {
                            System.out.println("\n===== MATCHES FOUND FOR \"" + searchTerm.toUpperCase() + "\" =====");
                            for (int i = 0; i < matches.size(); i++) {
                                Restaurant r = matches.get(i);
                                System.out.printf("[%d] %s (%s) - %s\n", (i + 1), r.getRestaurantName(),
                                        r.getFoodCategory(), r.getLocationNode());
                            }
                            System.out.printf("[%d] Exit Search & Return to Main Portal\n", (matches.size() + 1));

                            System.out.print("Select a restaurant number: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("Invalid input! Please enter a number.");
                                scanner.nextLine();
                                continue;
                            }
                            int restNum = scanner.nextInt();
                            scanner.nextLine(); // Clear buffer

                            if (restNum > 0 && restNum <= matches.size()) {
                                selectedRestaurant = matches.get(restNum - 1);
                            } else if (restNum == matches.size() + 1) {
                                System.out.println("Exiting search...");
                                break; // Breaks the viewingShopList loop, heading back to main portal
                            } else {
                                System.out.println(" Invalid selection. Please try again.");
                                continue;
                            }
                        }

                        // 3. Prevent cross-ordering / Handle existing cart contents
                        String chosenID = selectedRestaurant.getRestaurantID();
                        if (restaurantID != null && !chosenID.equals(restaurantID) && !cartStack.isEmpty()) {
                            System.out.println("\n Warning! Your cart contains items from a different restaurant.");
                            System.out.print("Do you want to clear the previous cart and start a new order? (Y/N): ");
                            String confirmClear = scanner.nextLine().trim();

                            if (confirmClear.equalsIgnoreCase("Y")) {
                                while (!cartStack.isEmpty()) {
                                    cartStack.undoLastItem();
                                }
                                restaurantID = chosenID;
                                System.out.println(
                                        "Cart cleared. Switched to: " + selectedRestaurant.getRestaurantName());
                            } else {
                                System.out.println("Returning to results list...");
                                continue; // Skips the menu loading and loops back to the list selection
                            }
                        } else {
                            restaurantID = chosenID;
                        }

                        // 4. Load the selected restaurant into Person 2's AVL Menu Subsystem
                        if (selectedRestaurant.getMenuItems().isEmpty()) {
                            System.out.println("This restaurant has no menu items listed yet.");
                            if (matches.size() == 1) {
                                break;
                            }
                            continue;
                        }
                        menuSystem.loadFromRestaurant(selectedRestaurant);

                        // 5. Present Customer Browsing & Ordering Workflow Loop
                        boolean browsingMenu = true;
                        while (browsingMenu) {
                            System.out.println("\n---" + selectedRestaurant.getRestaurantName().toUpperCase() + "---");
                            System.out.println("1. Display sorted menu (A-Z)");
                            System.out.println("2. Search food item by name snippet");
                            System.out.println("3. Add items directly to your Cart");
                            System.out.println("4. Back to Shop List Results");
                            System.out.println("5. Back to Main Menu");
                            System.out.print("Select option (1-5): ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("Invalid input! Please enter a number.");
                                scanner.nextLine();
                                continue;
                            }
                            int menuChoice = scanner.nextInt();
                            scanner.nextLine(); // Clean input stream buffer

                            switch (menuChoice) {
                                case 1:
                                    System.out.println("\n ===== "
                                            + selectedRestaurant.getRestaurantName().toUpperCase() + " MENU =====");
                                    menuSystem.displaySortedMenu();
                                    break;

                                case 2:
                                    System.out.print("Enter food item name keyword to search: ");
                                    String searchName = scanner.nextLine().trim();

                                    List<FoodItem> foundItems = menuSystem.searchByKeyword(searchName);
                                    if (!foundItems.isEmpty()) {
                                        System.out.println("\n[MATCHING DISHES FOUND]:");
                                        for (FoodItem item : foundItems) {
                                            System.out.println(item.toString());
                                        }
                                    } else {
                                        System.out.println(" \"" + searchName + "\" is not available here.");
                                    }
                                    break;

                                case 3:
                                    System.out.println("\nEnter the EXACT Food Name to add to your cart:");
                                    boolean addingItems = true;
                                    while (addingItems) {
                                        System.out.print("Item name: ");
                                        String foodName = scanner.nextLine().trim();

                                        // Look up via exact name mapping rules (O(log n) efficiency check)
                                        FoodItem itemToAdd = menuSystem.searchByName(foodName);

                                        if (itemToAdd != null) {
                                            cartStack.addItem(new OrderItem(itemToAdd));
                                            System.out.printf("Current cart total: RM %.2f\n", cartStack.calculateTotal());
                                        } else {
                                            System.out.println(
                                                    "Item not found. Please verify spelling from the A-Z menu selection.");
                                        }

                                        // Prompt the user explicitly if they want to keep adding items
                                        while (true) {
                                            System.out.print("\nDo you want to continue adding food items? (Y/N): ");
                                            String userChoice = scanner.nextLine().trim();

                                            if (userChoice.equalsIgnoreCase("N")) {
                                                System.out.println("Finished adding items to cart.");
                                                addingItems = false; // Flag to exit the primary item adding loop
                                                break;               // Break out of this validation loop
                                            } else if (userChoice.equalsIgnoreCase("Y")) {
                                                System.out.println();
                                                break;
                                            } else {
                                                System.out.println("Invalid input! Please enter 'Y' for Yes or 'N' for No.");
                                            }
                                        }
                                    }
                                    break;

                                case 4:
                                    System.out.println("Returning to your search results roster...");
                                    browsingMenu = false; // Break out of this specific restaurant's hub loop
                                    break;

                                case 5:
                                    System.out.println("Returning to main portal...");
                                    browsingMenu = false; // Break out of this specific restaurant's hub loop
                                    viewingShopList = false; // Break out of background list loop too
                                    break;
                                default:
                                    System.out.println("Invalid choice selection.");
                            }
                        }
                    }
                    break;

                case 3:
                    System.out.println("\n  [Current Cart]");
                    cartStack.displayCart();
                    break;

                case 4:
                    if (cartStack.isEmpty()) {
                        System.out.println("Your cart is already empty! Nothing to undo.");
                    } else {
                        System.out.print("Are you sure you want to remove the last added item? (Y/N): ");
                        String confirmUndo = scanner.nextLine().trim();
                        if (confirmUndo.equalsIgnoreCase("Y")) {
                            cartStack.undoLastItem();
                        } else {
                            System.out.println("Undo cancelled.");
                        }
                    }
                    break;

                case 5:
                    if (cartStack.isEmpty() || restaurantID == null) {
                        System.out.println("Your cart is empty. Please add items before confirming your order.");
                        break;
                    }
                    System.out.println("\n--- ORDER CONFIRMATION ---");
                    System.out.println("You are about to place an order from: " + managementSystem.getRestaurant(restaurantID).getRestaurantName());
                    System.out.printf("Delivery Location: %s\n", managementSystem.getUser(userID).getUserAddressNode());
                    System.out.println("\n  Order Summary");
                    cartStack.displayCart();
                    System.out.printf("Your current order total is: RM %.2f\n", cartStack.calculateTotal());
                    System.out.print("Are you sure you want to confirm your order? (Y/N): ");
                    String confirm = scanner.nextLine().trim();
                    if (confirm.equalsIgnoreCase("Y")) {
                        System.out.println("\nSubmitting order to Processing Queue...");

                        Order newOrder = new Order(managementSystem.getUser(userID),
                                managementSystem.getRestaurant(restaurantID), cartStack.confirmOrder());

                        orderQueue.receiveOrder(newOrder);
                        orderPlaced = true;
                        System.out.println("Order confirmed and added to processing queue!");
                        System.out.println("Your order id is: " + newOrder.getOrderID());
                        System.out.println("Your order is being prepared. You can track it once it's out for delivery.");
                        // Assign delivery rider and calculate delivery route
                        System.out.println("Matching optimal rider and path...");

                        String restaurantLocNode = managementSystem.getRestaurant(restaurantID).getLocationNode();

                        DeliveryScheduler checkoutMatchEngine = new DeliveryScheduler(restaurantLocNode, nav);

                        for (Rider r : deliveryScheduler.getRegisteredRiders()) {
                            // for debug purposes
                            // System.out.println("Evaluating rider: " + r.getRiderName());
                            checkoutMatchEngine.registerRider(r);
                        }

                        checkoutMatchEngine.displayAllRiders();
                        assignedRider = checkoutMatchEngine.assignBestRider();
                        if (assignedRider != null) {
                            System.out.println(String.format("Successfully assigned %s with rating %.1f to your order!",
                                    assignedRider.getRiderName(), assignedRider.getRating()));
                        } else {
                            System.out.println("No rider available at the moment.");
                        }
                    } else {
                        System.out.println("Checkout cancelled. Returning to main menu.");
                    }
                    break;

                case 6:
                    if (restaurantID == null || !orderPlaced) {
                        System.out.println("No active order found. Please checkout first (Option 5).");
                        break;
                    }

                    Restaurant currentRest = managementSystem.getRestaurant(restaurantID);
                    if (currentRest == null) {
                        System.out.println("Error! Restaurant not found for tracking.");
                        break;
                    }

                    System.out.println("\n=========================================");
                    System.out.println("       ACTIVE ORDER LIVE TRACKING        ");
                    System.out.println("=========================================");
                    System.out.println("Customer Profile : " + managementSystem.getUser(userID).getUserName());
                    System.out.println("Dispatch From    : " + currentRest.getRestaurantName() + " (" + currentRest.getLocationNode() + ")");
                    System.out.println("Destination Node : " + userLocation);
                    System.out.println("Assigned Courier : "
                            + (assignedRider != null ? assignedRider.getRiderName() + " (Rating: " + assignedRider.getRating() + ")" : "Searching..."));
                    nav.calculateShortestPath(managementSystem.getRestaurant(restaurantID).getLocationNode(),
                            userLocation);
                    nav.simulateDeliveryRoute(nav.finalRoute, myMap);
                    System.out.println("Your order has been delivered! Thank you for using GOODTECH.");
                    if (assignedRider != null) {
                        int rating = 0;
                        while (rating < 1 || rating > 5) {
                            System.out.print("Rate your delivery experience with " + assignedRider.getRiderName() + " (1-5 stars): ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("Invalid input! Please enter a number 1-5.");
                                scanner.nextLine();
                                continue;
                            }
                            rating = scanner.nextInt();
                            scanner.nextLine();
                            if (rating < 1 || rating > 5) {
                                System.out.println("Invalid rating! Please enter between 1 and 5.");
                            }
                        }
                        deliveryScheduler.rateRider(assignedRider, rating);
                        deliveryScheduler.setRiderLocation(assignedRider, userLocation);
                    } else {
                        System.out.println("No rider was assigned to this order.");
                    }

                    restaurantID = null;
                    orderPlaced = false;

                    System.out.print("Return to main menu or logout? (M/L): ");
                    String choices = scanner.nextLine().trim().toUpperCase();
                    if (choices.equals("M")) {
                        inCustomerMenu = true;
                        System.out.println("Returning to main menu...");
                    } else if (choices.equals("L")) {
                        inCustomerMenu = false;
                    } else {
                        System.out.println("Invalid choice. Returning to main menu by default.");
                    }
                    break;

                case 7:
                    System.out.println("Logging out of customer session...");
                    inCustomerMenu = false;
                    break;

                case 8:
                    System.out.println("Are you sure you want to delete your account? This action cannot be undone. (Y/N): ");
                    String deleteConfirm = scanner.nextLine().trim();
                    if (deleteConfirm.equalsIgnoreCase("Y")) {
                        managementSystem.removeUser(userID);
                        System.out.println("Your account has been permanently deleted.");
                        inCustomerMenu = false;
                    } else {
                        System.out.println("Account deletion cancelled. Returning to main menu.");
                    }
                    break;

                case 9:
                    if (orderPlaced) {
                        System.out.println("You are not allowed to change delivery location before your order is completed.");
                        break;
                    }
                    if (restaurantID != null || !cartStack.isEmpty()) {
                        System.out.print("Changing delivery will clear your current cart and reset your restaurant selection. Do you want to proceed? (Y/N): ");
                        String proceed = scanner.nextLine().trim();
                        if (!proceed.equalsIgnoreCase("Y")) {
                            System.out.println("Change delivery location cancelled. Returning to main menu.");
                            break;
                        } else {
                            restaurantID = null;
                            while (!cartStack.isEmpty()) {
                                cartStack.undoLastItem();
                            }
                            System.out.println("Cart cleared and restaurant selection reset.");
                        }
                    }
                    // Change delivery location
                    System.out.println("\nCurrent delivery location: " + userLocation);
                    int index = 1;
                    for (String loc : myMap.getLocations()) {
                        System.out.println(index + ". " + loc);
                        index++;
                    }
                    System.out.print("Select a new delivery location by number: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid input! Please enter a valid number.");
                        scanner.nextLine(); // Clear the bad input
                        break;
                    }

                    int locChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the trailing newline

                    if (locChoice > 0 && locChoice <= myMap.getLocations().size()) {
                        // Only update variables and file if the choice is completely valid
                        userLocation = myMap.getLocations().get(locChoice - 1);
                        managementSystem.getUser(userID).setUserAddressNode(userLocation);
                        managementSystem.saveUsersToFile(); // Explicitly save changes to your data file
                        System.out.println("Delivery location successfully updated to: " + userLocation);
                    } else {
                        System.out.println("Invalid selection. Keeping current location: " + userLocation);
                    }
                    break;

                default:
                    System.out.println("Invalid choice! Select 1-8.");
            }
        }
    }

    public static void runRestaurantPortal(Scanner scanner) {
        System.out.print("Please enter your admin ID to log in: ");
        String adminID = scanner.next().toUpperCase();
        if (managementSystem.getAdmin(adminID) == null) {
            System.out.println("Admin ID " + adminID + " not found. Please try again.");
            return;
        }
        System.out.print("Enter your password: ");
        String password = scanner.next();

        if (!managementSystem.getAdmin(adminID).getAdminPassword().equals(password)) {
            System.out.println("Incorrect password. Please try again.");
            return;
        }
        System.out.println("Admin " + managementSystem.getAdmin(adminID).getAdminName() + " logged in successfully.");

        boolean inAdminMenu = true;
        while (inAdminMenu) {
            System.out.println("\n--- GOODTECH RESTAURANT MANAGER/ADMIN PORTAL ---");
            System.out.println("1. Add New Restaurant");
            System.out.println("2. Remove Existing Restaurant");
            System.out.println("3. View All Restaurants");
            System.out.println("4. Update Restaurant Information");
            System.out.println("5. Update Restaurant Menu");
            System.out.println("6. Review Active Orders");
            System.out.println("7. Manage User Accounts");
            System.out.println("8. Display All Admin Accounts");
            System.out.println("9. Logout & Back");
            System.out.print("Please select an option (1-9): ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Adding new restaurant...");
                    int counter = managementSystem.getTotalRestaurants() + 1;
                    String newRestaurantId;
                    do {
                        newRestaurantId = "R" + String.format("%03d", counter++);
                    } while (managementSystem.getRestaurant(newRestaurantId) != null);

                    System.out.print("Enter restaurant name: ");
                    String restaurantName = scanner.nextLine().trim();

                    System.out.println("Choose location for the restaurant: ");
                    int i = 1;
                    for (String loc : myMap.getLocations()) {
                        System.out.println(i + ". " + loc);
                        i++;
                    }
                    System.out.print("Select a location by number: ");
                    int locationChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline
                    if (locationChoice < 1 || locationChoice > myMap.getLocations().size()) {
                        System.out.println("Invalid selection.");
                        break;
                    }
                    String location = myMap.getLocations().get(locationChoice - 1);

                    System.out.print("Enter food category: ");
                    String foodCategory = scanner.nextLine().trim();

                    Restaurant newRestaurant = new Restaurant(newRestaurantId, restaurantName, location, foodCategory);
                    managementSystem.addRestaurant(newRestaurant);
                    System.out.println(
                            "Successfully registered restaurant " + restaurantName + " with ID: " + newRestaurantId);
                    break;

                case 2:
                    System.out.println("Removing a restaurant...");

                    System.out.print("Enter restaurant ID to remove: ");
                    String restaurantIdToRemove = scanner.nextLine().trim();
                    if (managementSystem.getRestaurant(restaurantIdToRemove) != null) {
                        managementSystem.removeRestaurant(restaurantIdToRemove);
                        System.out.println("Restaurant removed successfully.");
                    } else {
                        System.out.println("Restaurant ID " + restaurantIdToRemove + " not found.");
                    }
                    break;

                case 3:
                    System.out.println("Displaying all restaurants...");
                    managementSystem.displayAllRestaurants();
                    break;

                case 4:
                    System.out.println("Updating restaurant information...");

                    System.out.print("Enter restaurant ID to update: ");
                    String restaurantIdToUpdate = scanner.nextLine().trim();

                    Restaurant restaurantToUpdate = managementSystem.getRestaurant(restaurantIdToUpdate);

                    // if the restaurant doesn't exist
                    if (restaurantToUpdate == null) {
                        System.out.println("Restaurant ID " + restaurantIdToUpdate + " not found.");
                        break; // Safely exit case 4 and return to admin dashboard
                    }

                    // 3. If it exists, proceed with gathering updates directly from the reference
                    // object
                    System.out.print("Enter new name (Current: " + restaurantToUpdate.getRestaurantName() + "): ");
                    String newRestaurantName = scanner.nextLine().trim();

                    System.out.println("Choose new location (Current: " + restaurantToUpdate.getLocationNode() + "): ");
                    int k = 1;
                    for (String loc : myMap.getLocations()) {
                        System.out.println(k + ". " + loc);
                        k++;
                    }
                    System.out.println("Select a new location by number: ");
                    int newlocationChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline
                    if (newlocationChoice < 1 || newlocationChoice > myMap.getLocations().size()) {
                        System.out.println("Invalid selection.");
                        break;
                    }
                    String newLocation = myMap.getLocations().get(newlocationChoice - 1);

                    System.out.print("Enter new category (Current: " + restaurantToUpdate.getFoodCategory() + "): ");
                    String newFoodCategory = scanner.nextLine().trim();

                    // 4. Commit the modifications to the data models and persist to file
                    restaurantToUpdate.setRestaurantName(newRestaurantName);
                    restaurantToUpdate.setLocationNode(newLocation);
                    restaurantToUpdate.setFoodCategory(newFoodCategory);

                    managementSystem.saveRestaurantsToFile();
                    System.out.println("Restaurant " + restaurantIdToUpdate + " details updated successfully.");
                    break;

                case 5:
                    System.out.println("Updating restaurant menu...");

                    System.out.print("Enter restaurant ID to update menu:");
                    String restaurantIdToUpdateMenu = scanner.nextLine().trim();
                    Restaurant restaurantToUpdateMenu = managementSystem.getRestaurant(restaurantIdToUpdateMenu);
                    if (restaurantToUpdateMenu == null) {
                        System.out.println("Restaurant ID " + restaurantIdToUpdateMenu + " not found.");
                        break;
                    }
                    System.out.println(
                            "1. Adding new food item to " + restaurantToUpdateMenu.getRestaurantName() + "'s menu.");
                    System.out.println(
                            "2. Removing food item from " + restaurantToUpdateMenu.getRestaurantName() + "'s menu.");
                    System.out.print("Select option (1-2): ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid input! Please enter 1 or 2.");
                        scanner.nextLine();
                        break;
                    }
                    int menuUpdateChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    switch (menuUpdateChoice) {
                        case 1:
                            System.out.print("Enter new food item name: ");
                            String itemName = scanner.nextLine().trim();

                            System.out.print("Enter price for " + itemName + ": ");
                            while (!scanner.hasNextDouble()) {
                                System.out.print("Invalid price number! Try again: ");
                                scanner.nextLine();
                            }
                            double itemPrice = scanner.nextDouble();
                            scanner.nextLine();

                            FoodItem newDish = new FoodItem(restaurantIdToUpdateMenu, itemName, itemPrice,
                                    restaurantToUpdateMenu.getFoodCategory());
                            restaurantToUpdateMenu.addMenuItem(newDish);
                            managementSystem.appendMenuItemToFile(newDish);
                            System.out.println("'" + itemName + "' (RM " + itemPrice + ") successfully added to "
                                    + restaurantToUpdateMenu.getRestaurantName() + "'s menu.");
                            break;
                        case 2:
                            System.out.print("Enter the name of the food item to remove: ");
                            String itemNameToRemove = scanner.nextLine().trim();
                            List<FoodItem> menuItems = restaurantToUpdateMenu.getMenuItems();
                            FoodItem itemToDelete = null;

                            // Step 1: Scan safely to find the matching item without altering the list size
                            // mid-flight
                            for (FoodItem item : menuItems) {
                                if (item.getFoodName().equalsIgnoreCase(itemNameToRemove)) {
                                    itemToDelete = item; // Store the reference pointer
                                    break; // Exit the loop safely now that a match is recorded
                                }
                            }

                            // Step 2: Execute modification outside the loop context
                            if (itemToDelete != null) {
                                menuItems.remove(itemToDelete); // Remove it once cleanly from the reference list
                                managementSystem.saveAllMenuItemsToFile(); // Rewrite the menu file to match changes
                                System.out.println("'" + itemNameToRemove + "' has been removed from "
                                        + restaurantToUpdateMenu.getRestaurantName() + "'s menu.");
                            } else {
                                System.out.println("Food item not found in the menu.");
                            }
                            break;
                    }
                    break;

                case 6: // New case to review the shared FIFO queue
                    System.out.println("\n===== REVENUE & ORDER REVIEW DASHBOARD =====");

                    // 1. Display the current live state of the queue
                    orderQueue.displayActiveOrders();

                    // 2. Ask the admin if they want to process the next order in line
                    System.out.print("\nWould you like to process the next order in the queue? (Y/N): ");
                    String processConfirm = scanner.nextLine().trim();

                    if (processConfirm.equalsIgnoreCase("Y")) {
                        System.out.println();
                        // Polling the oldest order out of the FIFO structure
                        orderQueue.processNextOrder();
                    }
                    break;

                case 7:
                    // Implement user account management logic here
                    System.out.print("Remove user account by ID: ");
                    String userIdToRemove = scanner.nextLine().trim();
                    if (managementSystem.getUser(userIdToRemove) != null) {
                        managementSystem.removeUser(userIdToRemove);
                    } else {
                        System.out.println("User ID not found.");
                    }
                    break;

                case 8:
                    System.out.println("Displaying active admin accounts...");
                    managementSystem.displayAllAdmins();
                    break;

                case 9:
                    System.out.println("Logging out of admin session...");
                    inAdminMenu = false;
                    break;

                default:
                    System.out.println("Invalid choice! Select 1-9.");
            }

        }
    }
}
