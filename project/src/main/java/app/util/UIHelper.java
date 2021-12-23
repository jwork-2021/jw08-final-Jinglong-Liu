package app.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UIHelper {
    public static void prompt(String head,String text){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("注意");
            alert.headerTextProperty().set(head);
            alert.setContentText(text);
            alert.showAndWait();
        });
    }
}
