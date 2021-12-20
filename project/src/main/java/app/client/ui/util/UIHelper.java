package app.client.ui.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class UIHelper {
    public static void prompt(String title,String text){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.titleProperty().set(title);
                alert.headerTextProperty().set("注意");
                alert.setContentText(text);
                alert.showAndWait();
            }
        });
    }
}
