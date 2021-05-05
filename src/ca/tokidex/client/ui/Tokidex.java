package ca.tokidex.client.ui;

import ca.tokidex.client.control.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main JavaFX class that launches application and opens main window
 */
public class Tokidex extends Application {

    private final Controller controller = new Controller();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        WindowManager windowManager = new WindowManager(primaryStage, controller);
        controller.setWindowManager(windowManager);
        primaryStage.setTitle("Tokidex App");
        primaryStage.getIcons().add(new Image("file:img/icon.png"));

        Scene mainScene = windowManager.getHomeScene();

        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
