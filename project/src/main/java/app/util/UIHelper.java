package app.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
