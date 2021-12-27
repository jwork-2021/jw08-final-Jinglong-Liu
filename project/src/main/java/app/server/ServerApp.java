package app.server;

import javafx.application.Application;
import javafx.stage.Stage;

public class ServerApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        new Main(primaryStage).start();
    }
}
