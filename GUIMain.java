import javafx.application.Application;
import javafx.stage.Stage;

public class GUIMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginScreen loginScreen = new LoginScreen(primaryStage);
        loginScreen.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
