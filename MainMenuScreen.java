import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Modality;

public class MainMenuScreen {

    private Stage stage;
    private String portalType;
    private ManagementSystem managementSystem;

    // ADD THIS LINE HERE: This ensures the status bar is remembered across methods
    private Label statusBar = new Label("");

    public MainMenuScreen(Stage stage, String portalType, ManagementSystem managementSystem) {
        this.stage = stage;
        this.portalType = portalType;
        this.managementSystem = managementSystem;
    }

    public void show() {
        if (portalType.equals("customer")) {
            showCustomerLogin();
        } else {
            showAdminLogin();
        }
    }

    // ─── CUSTOMER LOGIN ───────────────────────────────────────────
    private void showCustomerLogin() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #FDFBF7, #F5EBE0);");

        VBox card = buildCard();

        Label title = new Label("👤   Customer Login");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #222222;");

        Label subtitle = new Label("Enter your credentials to enter the food arena");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #777777;");

        TextField userIDField = buildTextField("Enter your User ID");
        PasswordField passwordField = buildPasswordField("Enter your password");

        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: #DA291C; -fx-font-size: 12px;");
        statusLabel.setWrapText(true);

        Button loginBtn = buildPrimaryButton("Login / Sign Up", "#DA291C", "white");
        Button backBtn = buildSecondaryButton("← Back to Portals");

        loginBtn.setOnAction(e -> {
            String userID = userIDField.getText().trim();
            String password = passwordField.getText();

            if (userID.isEmpty()) {
                statusLabel.setText("Please enter your User ID!");
                return;
            }

            User user = managementSystem.getUser(userID);
            if (user == null) {
                showRegistrationPopup(userID);
            } else {
                if (password.equals(user.getUserPassword())) {
                    showCustomerDashboard(user);
                } else {
                    statusLabel.setText("Incorrect password. Please try again.");
                    passwordField.clear();
                }
            }
        });

        backBtn.setOnAction(e -> new LoginScreen(stage).show());

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: rgba(0,0,0,0.06);");

        card.getChildren().addAll(
                title, subtitle, sep,
                buildFieldLabel("User ID:"), userIDField,
                buildFieldLabel("Password:"), passwordField,
                loginBtn, backBtn, statusLabel);

        root.getChildren().add(card);
        Scene scene = new Scene(root, 520, 600);
        stage.setScene(scene);
    }

    // ─── REGISTRATION POPUP ───────────────────────────────────────
    private void showRegistrationPopup(String userID) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Join the Foodie Club");

        VBox root = new VBox(12);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle("-fx-background-color: #FFFFFF;");

        Label title = new Label("Create New Account");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #222222;");

        Label info = new Label("User ID: " + userID + " is new! Fill details to start ordering.");
        info.setStyle("-fx-text-fill: #777777; -fx-font-size: 13px;");
        info.setWrapText(true);

        TextField nameField = buildTextField("Full Name");
        TextField phoneField = buildTextField("Phone Number (10-11 digits)");
        TextField locationField = buildTextField("Location (e.g. KK12 Hostel)");
        PasswordField passField = buildPasswordField("Password");
        PasswordField confirmField = buildPasswordField("Confirm Password");

        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: #DA291C; -fx-font-size: 12px;");

        Button registerBtn = buildPrimaryButton("Create Account & Order", "#DA291C", "white");

        registerBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String location = locationField.getText().trim();
            String pass = passField.getText();
            String confirm = confirmField.getText();

            if (name.isEmpty() || phone.isEmpty() || location.isEmpty() || pass.isEmpty()) {
                statusLabel.setText("All fields are required!");
                return;
            }
            if (!phone.matches("\\d{10,11}")) {
                statusLabel.setText("Phone must be 10-11 digits only!");
                return;
            }
            if (!pass.equals(confirm)) {
                statusLabel.setText("Passwords do not match!");
                return;
            }
            if (managementSystem.hasDuplicatePhoneNumber(phone)) {
                statusLabel.setText("Phone number already registered!");
                return;
            }

            User newUser = new User(userID, name, phone, location, pass);
            managementSystem.addUser(newUser);
            popup.close();
            showCustomerDashboard(newUser);
        });

        root.getChildren().addAll(
                title, info,
                buildFieldLabel("Full Name:"), nameField,
                buildFieldLabel("Phone Number:"), phoneField,
                buildFieldLabel("Location:"), locationField,
                buildFieldLabel("Password:"), passField,
                buildFieldLabel("Confirm Password:"), confirmField,
                registerBtn, statusLabel);

        Scene scene = new Scene(root, 440, 600);
        popup.setScene(scene);
        popup.showAndWait();
    }

    // ─── CUSTOMER DASHBOARD ───────────────────────────────────────
    private void showCustomerDashboard(User user) {
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: #FDFBF7;");

        // Top bar (Branded vibrant red header block)
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(18, 25, 18, 25));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #DA291C;");

        // Inside showCustomerDashboard(User user) and showAdminDashboard(Admin admin)
        Label logo = new Label("🍔 CraveDrop"); // <-- Updated branding name here!
        logo.setStyle(
                "-fx-font-size: 22px;" + // Slightly bumped up font size for balance
                        "-fx-font-weight: 900;" + // Heavy, modern font weight
                        "-fx-text-fill: #FFC72C;" + // Beautiful, golden appetizing yellow
                        "-fx-font-family: 'Arial Black', sans-serif;" // Ensures a bold, crisp app aesthetic
        );

        // Add a crisp shadow so the text looks like it's leaping off the red top-bar
        DropShadow logoShadow = new DropShadow();
        logoShadow.setColor(Color.web("#8B0000", 0.6)); // Deep crimson shadow backing
        logoShadow.setRadius(8);
        logoShadow.setOffsetY(2);
        logo.setEffect(logoShadow);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("👤 " + user.getUserName());
        userLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(
                "-fx-background-color: #FFFFFF; -fx-text-fill: #DA291C;" +
                        "-fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 16; -fx-background-radius: 8; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> new LoginScreen(stage).show());

        topBar.getChildren().addAll(logo, spacer, userLabel, new Label("   "), logoutBtn);

        // Welcome section
        VBox welcomeBox = new VBox(6);
        welcomeBox.setPadding(new Insets(25, 25, 15, 25));
        Label welcome = new Label("Welcome back, " + user.getUserName() + "! 👋");
        welcome.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #222222;");
        Label location = new Label("📍 Delivery to: " + user.getUserAddressNode());
        location.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #8B0000;");
        welcomeBox.getChildren().addAll(welcome, location);

        Label menuTitle = new Label("What are you craving today?");
        menuTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #777777; -fx-padding: 0 25;");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15, 25, 25, 25));
        grid.setHgap(15);
        grid.setVgap(15);

        String[][] menuItems = {
                { "🏪", "View Restaurants", "Browse nearby kitchen branches" },
                { "🔍", "Search Food", "Find your absolute favorite dishes" },
                { "🛒", "View Cart", "Review items ready to drop" },
                { "↩️", "Undo Last Item", "Instantly remove last added item" },
                { "✅", "Confirm Order", "Secure checkout with hot delivery" },
                { "🚴", "Track Order", "Follow your live rider path" },
                { "📍", "Change Location", "Redirect current shipping address" },
                { "🗑️", "Delete Account", "Close down active profile" }
        };

        // Lively restaurant branding accent colors
        String[] colors = {
                "#DA291C", "#FFC72C", "#27ae60", "#f39c12",
                "#27ae60", "#e67e22", "#2980b9", "#c0392b"
        };

        statusBar.setText(""); // Reset it or leave it as is
        statusBar.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 12px; -fx-padding: 0 25; -fx-font-weight: bold;");
        statusBar.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 12px; -fx-padding: 0 25; -fx-font-weight: bold;");

        for (int i = 0; i < menuItems.length; i++) {
            final int index = i;
            VBox menuCard = buildMenuCard(menuItems[i][0], menuItems[i][1], menuItems[i][2], colors[i]);
            menuCard.setOnMouseClicked(e -> handleCustomerAction(index + 1, user, statusBar));
            grid.add(menuCard, i % 2, i / 2);
        }

        root.getChildren().addAll(topBar, welcomeBox, menuTitle, grid, statusBar);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #FDFBF7; -fx-background: #FDFBF7;");

        Scene scene = new Scene(scroll, 520, 680);
        stage.setScene(scene);
    }

    private void handleCustomerAction(int choice, User user, Label statusBar) {
        switch (choice) {
            case 1:
                showInfoPopup("Our Active Kitchen Branches", getRestaurantList());
                break;
            case 7:
                showChangeLocationPopup(user, statusBar);
                break;
            case 8:
                showConfirmDeletePopup(user);
                break;
            default:
                showInfoPopup("Notice",
                        "Please use your interactive console terminal for this step.\nRun: java MainApplication");
        }
    }

    private void showChangeLocationPopup(User user, Label statusBar) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Update Delivery Address");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #FFFFFF;");

        Label titleLabel = new Label("📍 Change Delivery Location");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #222222;");

        // Pre-fill with their current address as a helpful prompt
        TextField addressField = buildTextField(user.getUserAddressNode());
        addressField.setPromptText("Enter new hostel or building node...");

        Button saveBtn = buildPrimaryButton("Update Address", "#DA291C", "white");
        Button cancelBtn = buildSecondaryButton("Cancel");

        saveBtn.setOnAction(e -> {
            String newAddress = addressField.getText().trim();
            if (!newAddress.isEmpty()) {
                // 1. Permanently update the backend user object data
                user.setUserAddressNode(newAddress);

                // 2. Close the popup window
                popup.close();

                // 3. Refresh the dashboard screen so the top header profile address updates
                // instantly
                showCustomerDashboard(user);

                // 4. CRITICAL: Find the fresh status bar on the newly rendered screen and
                // preserve your message!
                // (Since showCustomerDashboard re-creates the UI elements, we find the
                // container or pass text)
                statusBar.setText("📍 Delivery Target Confirmed: " + user.getUserAddressNode());
            }
        });

        cancelBtn.setOnAction(e -> popup.close());

        root.getChildren().addAll(titleLabel, addressField, saveBtn, cancelBtn);
        popup.setScene(new Scene(root, 360, 220));
        popup.showAndWait();
    }

    private String getRestaurantList() {
        StringBuilder sb = new StringBuilder();
        for (Restaurant r : managementSystem.getAllRestaurants()) {
            sb.append("🏪 ").append(r.getRestaurantName())
                    .append("\n    📍 Location: ").append(r.getLocationNode())
                    .append("\n    🍽️  Category: ").append(r.getFoodCategory())
                    .append("\n\n───────────────────────────\n\n");
        }
        return sb.length() > 0 ? sb.toString() : "No active hub restaurants found right now.";
    }

    private void showConfirmDeletePopup(User user) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Delete Account");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #FFFFFF;");

        Label title = new Label("⚠️ Delete Profile Permanently");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #DA291C;");

        Label msg = new Label("Are you sure? You'll lose all loyalty steps!");
        msg.setStyle("-fx-text-fill: #222222; -fx-font-size: 13px;");

        Button confirmBtn = buildPrimaryButton("Yes, Delete Profile", "#c0392b", "white");
        Button cancelBtn = buildSecondaryButton("Cancel");

        confirmBtn.setOnAction(e -> {
            managementSystem.removeUser(user.getUserID());
            popup.close();
            new LoginScreen(stage).show();
        });

        cancelBtn.setOnAction(e -> popup.close());

        root.getChildren().addAll(title, msg, confirmBtn, cancelBtn);
        popup.setScene(new Scene(root, 340, 220));
        popup.showAndWait();
    }

    // ─── ADMIN LOGIN ──────────────────────────────────────────────
    private void showAdminLogin() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #FDFBF7, #F5EBE0);");

        VBox card = buildCard();

        Label title = new Label("🔧   Admin Management Dashboard");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #222222;");

        Label subtitle = new Label("Provide back-end operator pass-codes");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #777777;");

        TextField adminIDField = buildTextField("Admin ID");
        PasswordField passwordField = buildPasswordField("Password");

        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: #DA291C; -fx-font-size: 12px;");

        Button loginBtn = buildPrimaryButton("Access Backoffice", "#FFC72C", "#222222");
        Button backBtn = buildSecondaryButton("← Back");

        loginBtn.setOnAction(e -> {
            String adminID = adminIDField.getText().trim();
            String password = passwordField.getText();

            if (adminID.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please fill in all fields!");
                return;
            }

            Admin admin = managementSystem.getAdmin(adminID);
            if (admin == null) {
                statusLabel.setText("Admin identity token missing.");
            } else if (!admin.getAdminPassword().equals(password)) {
                statusLabel.setText("Incorrect access authorization key.");
                passwordField.clear();
            } else {
                showAdminDashboard(admin);
            }
        });

        backBtn.setOnAction(e -> new LoginScreen(stage).show());

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: rgba(0,0,0,0.06);");

        card.getChildren().addAll(
                title, subtitle, sep,
                buildFieldLabel("Admin Operator ID:"), adminIDField,
                buildFieldLabel("Master Password:"), passwordField,
                loginBtn, backBtn, statusLabel);

        root.getChildren().add(card);
        Scene scene = new Scene(root, 520, 550);
        stage.setScene(scene);
    }

    // ─── ADMIN DASHBOARD ──────────────────────────────────────────
    private void showAdminDashboard(Admin admin) {
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: #FDFBF7;");

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(18, 25, 18, 25));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #222222;"); // High end matte black look for backoffice admin

        Label logo = new Label("🔧 GoodTech Admin");
        logo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #FFC72C;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label adminLabel = new Label("Operator: " + admin.getAdminName());
        adminLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #FFFFFF;");

        Button logoutBtn = new Button("Exit Console");
        logoutBtn.setStyle(
                "-fx-background-color: #DA291C; -fx-text-fill: white;" +
                        "-fx-font-size: 12px; -fx-font-weight:bold; -fx-padding: 6 15; -fx-background-radius: 8; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> new LoginScreen(stage).show());

        topBar.getChildren().addAll(logo, spacer, adminLabel, new Label("   "), logoutBtn);

        VBox welcomeBox = new VBox(5);
        welcomeBox.setPadding(new Insets(25, 25, 15, 25));
        Label welcome = new Label("Backoffice Control Console ⚙️");
        welcome.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #222222;");
        Label sub = new Label("Live restaurant updates & node adjustments");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #777777;");
        welcomeBox.getChildren().addAll(welcome, sub);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15, 25, 25, 25));
        grid.setHgap(15);
        grid.setVgap(15);

        String[][] menuItems = {
                { "➕", "Add Restaurant", "Link new storefront point" },
                { "🗑️", "Remove Branch", "Purge restaurant from engine" },
                { "🏪", "View System Layout", "Track list of current hubs" },
                { "📋", "Active Orders Queue", "Intercept distribution statuses" },
                { "👥", "User Profiles", "Manage registration data entries" },
                { "👨‍💼", "Staffing Management", "Audit control operator codes" }
        };

        String[] colors = { "#27ae60", "#c0392b", "#222222", "#f39c12", "#2980b9", "#7f8c8d" };

        for (int i = 0; i < menuItems.length; i++) {
            final int index = i;
            VBox menuCard = buildMenuCard(menuItems[i][0], menuItems[i][1], menuItems[i][2], colors[i]);
            menuCard.setOnMouseClicked(e -> handleAdminAction(index + 1, new Label()));
            grid.add(menuCard, i % 2, i / 2);
        }

        root.getChildren().addAll(topBar, welcomeBox, grid);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #FDFBF7; -fx-background: #FDFBF7;");

        Scene scene = new Scene(scroll, 520, 650);
        stage.setScene(scene);
    }

    private void handleAdminAction(int choice, Label statusBar) {
        if (choice == 3) {
            showInfoPopup("Systems Layout Hub Registry", getRestaurantList());
        } else {
            showInfoPopup("Terminal Redirection",
                    "Please process this command inside your terminal instance.\nRun: java MainApplication");
        }
    }

    // ─── SHARED INFO POPUP ────────────────────────────────────────
    private void showInfoPopup(String title, String content) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle(title);

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #FFFFFF;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #222222;");

        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefHeight(300);
        textArea.setStyle(
                "-fx-control-inner-background: #FDFBF7; " +
                        "-fx-text-fill: #222222;" +
                        "-fx-font-size: 13px; -fx-font-family: monospace;");

        Button closeBtn = buildPrimaryButton("Got it!", "#DA291C", "white");
        closeBtn.setOnAction(e -> popup.close());

        root.getChildren().addAll(titleLabel, textArea, closeBtn);
        popup.setScene(new Scene(root, 440, 440));
        popup.showAndWait();
    }

    // ─── UI FACTORY METHODS ───────────────────────────────────────
    private VBox buildCard() {
        VBox card = new VBox(14);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(35, 40, 35, 40));
        card.setMaxWidth(400);
        card.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: rgba(0,0,0,0.04);" +
                        "-fx-border-radius: 20;" +
                        "-fx-border-width: 1;");
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.color(0, 0, 0, 0.06));
        shadow.setRadius(20);
        shadow.setOffsetY(6);
        card.setEffect(shadow);
        return card;
    }

    private TextField buildTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(
                "-fx-background-color: #F8F9FA;" +
                        "-fx-text-fill: #222222;" +
                        "-fx-prompt-text-fill: #A0A0A0;" +
                        "-fx-padding: 12 15;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #E2E8F0;" +
                        "-fx-border-radius: 10;" +
                        "-fx-font-size: 13px;");
        return tf;
    }

    private PasswordField buildPasswordField(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setStyle(
                "-fx-background-color: #F8F9FA;" +
                        "-fx-text-fill: #222222;" +
                        "-fx-prompt-text-fill: #A0A0A0;" +
                        "-fx-padding: 12 15;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #E2E8F0;" +
                        "-fx-border-radius: 10;" +
                        "-fx-font-size: 13px;");
        return pf;
    }

    private Button buildPrimaryButton(String text, String color, String textFill) {
        Button btn = new Button(text);
        String style = "-fx-background-color: " + color + ";" +
                "-fx-text-fill: " + textFill + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 12 25;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;" +
                "-fx-min-width: 200;";
        btn.setStyle(style);
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private Button buildSecondaryButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #555555;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: #D6D6D6;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 8 20;");
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private Label buildFieldLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: #444444; -fx-font-size: 12px; -fx-font-weight: bold;");
        return lbl;
    }

    private VBox buildMenuCard(String emoji, String title, String desc, String color) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(16));
        card.setPrefWidth(210);
        card.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: rgba(0,0,0,0.05);" +
                        "-fx-border-radius: 16;" +
                        "-fx-border-width: 1;" +
                        "-fx-cursor: hand;");

        // Soft internal card shadow
        DropShadow cardShadow = new DropShadow();
        cardShadow.setColor(Color.color(0, 0, 0, 0.03));
        cardShadow.setRadius(10);
        cardShadow.setOffsetY(3);
        card.setEffect(cardShadow);

        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 28px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #222222;");

        Label descLabel = new Label(desc);
        descLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #777777;");
        descLabel.setWrapText(true);

        card.getChildren().addAll(emojiLabel, titleLabel, descLabel);

        // Lively micro-interaction on selection hover
        card.setOnMouseEntered(e -> {
            card.setStyle(
                    "-fx-background-color: #FFFFFF;" +
                            "-fx-background-radius: 16;" +
                            "-fx-border-color: " + color + ";" +
                            "-fx-border-radius: 16;" +
                            "-fx-border-width: 2;" +
                            "-fx-cursor: hand;");
            DropShadow hoverShadow = new DropShadow();
            hoverShadow.setColor(Color.web(color, 0.15));
            hoverShadow.setRadius(15);
            hoverShadow.setOffsetY(5);
            card.setEffect(hoverShadow);
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                    "-fx-background-color: #FFFFFF;" +
                            "-fx-background-radius: 16;" +
                            "-fx-border-color: rgba(0,0,0,0.05);" +
                            "-fx-border-radius: 16;" +
                            "-fx-border-width: 1;" +
                            "-fx-cursor: hand;");
            card.setEffect(cardShadow);
        });

        return card;
    }
}