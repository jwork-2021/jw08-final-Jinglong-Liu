package app.server;

import app.base.World;
import app.server.ui.ServerScene;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Thread{
    private ServerScene serverScene = new ServerScene();
    Main(Stage stage){
        stage.setScene(serverScene.scene());
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //saveWorld();
                System.out.println("服务器已断开");
                disconnect();
                System.exit(0);
            }
        });

        serverScene.listenButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                restart();
                serverScene.listenButton.setDisable(true);
                //serverScene.number.setDisable(true);
            }
        });
    }
    @Override
    public void run() {
        super.run();
    }
    private Server server;
    private Game game;
    private Handler handler;
    private World world;

    private void restart(){
        int number = 2;
        int port = 8090;
        try {
            number = (int)serverScene.number.getValue();
            port = Integer.parseInt(serverScene.port.getText());
        }
        catch (Exception e){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.titleProperty().set("提醒");
                    alert.headerTextProperty().set("注意");
                    alert.setContentText("请输入合法的端口号");
                    alert.showAndWait();
                }
            });
            return;
        }
        world = new World();
        server = new Server(port);
        game = new Game(world,number);
        handler = new Handler(server,game);
        System.out.println("服务器正在启动中，请稍等……");
        server.start();
    }
    private void disconnect(){

    }
}
