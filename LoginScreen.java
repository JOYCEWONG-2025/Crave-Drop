import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginScreen {

    private Stage stage;
    private ManagementSystem managementSystem = new ManagementSystem();

    public LoginScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // Root layout with a lively, warm gradient background
        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        
        // Maintained the 60px top padding to keep the layout spaced perfectly down
        root.setPadding(new Insets(60, 50, 50, 50)); 
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #FDFBF7, #F5EBE0);");

        // Logo / Title Section
        // ─── UPGRADED LOGO / TITLE SECTION (irresistible food vibe) ───
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);

        // ─── NATURAL FOOD ICON CLUSTER (warm gold/orange glow, no hard badge shape) ───
        HBox foodCluster = new HBox(14);
        foodCluster.setAlignment(Pos.CENTER);

        Label burgerLabel = new Label("🍔");
        Label pizzaLabel = new Label("🍕");
        Label friesLabel = new Label("🍟");

        String foodStyle = "-fx-font-size: 48px;";
        burgerLabel.setStyle(foodStyle);
        pizzaLabel.setStyle(foodStyle);
        friesLabel.setStyle(foodStyle);

        // Soft warm gold/orange glow behind each icon — subtle, not a hard badge outline
        DropShadow warmGlow = new DropShadow();
        warmGlow.setColor(Color.web("#FFA630", 0.45));
        warmGlow.setRadius(18);
        warmGlow.setSpread(0.15);

        burgerLabel.setEffect(warmGlow);
        pizzaLabel.setEffect(warmGlow);
        friesLabel.setEffect(warmGlow);

        // Gentle natural bounce on hover (scale instead of a stiff rotate)
        for (Label foodIcon : new Label[]{burgerLabel, pizzaLabel, friesLabel}) {
            foodIcon.setOnMouseEntered(e -> { foodIcon.setScaleX(1.15); foodIcon.setScaleY(1.15); });
            foodIcon.setOnMouseExited(e -> { foodIcon.setScaleX(1.0); foodIcon.setScaleY(1.0); });
        }

        foodCluster.getChildren().addAll(burgerLabel, pizzaLabel, friesLabel);

        // Thin golden accent underline — a nod to the double-arch branding motif
        Region archAccent = new Region();
        archAccent.setPrefHeight(4);
        archAccent.setMaxWidth(48);
        archAccent.setMinWidth(48);
        archAccent.setStyle("-fx-background-color: #FFC72C; -fx-background-radius: 10;");

        Label title = new Label("CraveDrop"); // Using a punchy, catchy name!
        title.setStyle(
                "-fx-font-size: 46px;" +
                        "-fx-font-weight: 900;" +
                        "-fx-text-fill: #222222;" +
                        "-fx-font-family: 'Arial Black', sans-serif;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 4, 0, 0, 2);");

        Label subtitle = new Label("Sizzling Hot Delivery Right to Your Door");
        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #DA291C;" + // Vibrant appetizing branding red
                        "-fx-letter-spacing: 1.5px;");

        // Swapped the single badge for a natural cluster of appetizing food icons
        titleBox.getChildren().addAll(foodCluster, archAccent, title, subtitle);

        // Card container (Clean crisp card with soft shadow)
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40, 40, 40, 40));
        card.setMaxWidth(400);
        card.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 24;" +
                        "-fx-border-color: rgba(0,0,0,0.03);" +
                        "-fx-border-radius: 24;" +
                        "-fx-border-width: 1;");

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.color(0.5, 0.1, 0.1, 0.08));
        shadow.setRadius(25);
        shadow.setOffsetY(8);
        card.setEffect(shadow);

        Label cardTitle = new Label("Welcome Back!");
        cardTitle.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222222;");

        Label cardSubtitle = new Label("Select your portal to start ordering");
        cardSubtitle.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #777777;");

        // Separator
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: rgba(0,0,0,0.06); -fx-padding: 5 0;");

        // Customer Portal Button (Bold Appetizing Red gradient)
        Button customerBtn = createPortalButton(
                "👤    Customer Portal",
                "linear-gradient(to bottom right, #E63946, #B31B12)", // Vibrant Red gradient
                "linear-gradient(to bottom right, #B31B12, #8B0000)");

        // Admin Portal Button (Warm Golden Yellow/Orange gradient)
        Button adminBtn = createPortalButton(
                "🔧    Management Portal",
                "linear-gradient(to bottom right, #FFD54F, #FFC72C)", // Golden Yellow gradient
                "linear-gradient(to bottom right, #FFC72C, #E6A100)");
        adminBtn.setStyle(adminBtn.getStyle() + "-fx-text-fill: #222222;"); // Dark text for high contrast on yellow

        // Exit Button
        Button exitBtn = new Button("Exit");
        exitBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #777777;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: #E0E0E0;" +
                        "-fx-border-radius: 10;" +
                        "-fx-padding: 10 24;");

        // Status label for messages
        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: #DA291C; -fx-font-size: 12px;");

        // Button Actions
        customerBtn.setOnAction(e -> {
            MainMenuScreen mainMenu = new MainMenuScreen(stage, "customer", managementSystem);
            mainMenu.show();
        });

        adminBtn.setOnAction(e -> {
            MainMenuScreen mainMenu = new MainMenuScreen(stage, "admin", managementSystem);
            mainMenu.show();
        });

        exitBtn.setOnAction(e -> stage.close());

        // Hover effects for Exit button
        exitBtn.setOnMouseEntered(e -> exitBtn.setStyle(
                "-fx-background-color: #F5F5F5;" +
                        "-fx-text-fill: #222222;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: #CCCCCC;" +
                        "-fx-border-radius: 10;" +
                        "-fx-padding: 10 24;"));
        exitBtn.setOnMouseExited(e -> exitBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #777777;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: #E0E0E0;" +
                        "-fx-border-radius: 10;" +
                        "-fx-padding: 10 24;"));

        card.getChildren().addAll(
                cardTitle, cardSubtitle, sep,
                customerBtn, adminBtn, exitBtn, statusLabel);

        root.getChildren().addAll(titleBox, card);

        // Preserved clean layout window size setup
        Scene scene = new Scene(root, 520, 660); 
        
        stage.setTitle("CraveDrop - Smart Food Delivery"); 
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private Button createPortalButton(String text, String color, String hoverColor) {
        Button btn = new Button(text);
        boolean isYellow = color.contains("FFC72C");
        String textFill = isYellow ? "#222222" : "white";

        String baseStyle = "-fx-background-color: " + color + ";" +
                "-fx-text-fill: " + textFill + ";" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 16 32;" +
                "-fx-background-radius: 14;" +
                "-fx-cursor: hand;" +
                "-fx-min-width: 280;";

        btn.setStyle(baseStyle);

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + hoverColor + ";" +
                        "-fx-text-fill: " + textFill + ";" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 16 32;" +
                        "-fx-background-radius: 14;" +
                        "-fx-cursor: hand;" +
                        "-fx-min-width: 280;" +
                        "-fx-effect: dropshadow(gaussian, rgba(139,0,0,0.15), 12, 0, 0, 4);"));

        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));

        return btn;
    }
}