package app.server;

import app.base.NPTank;
import app.base.Player;
import app.base.World;
import app.server.game.Factory;
import app.server.ui.ServerScene;
import app.util.SaveUtil;
import app.util.UIHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main{
    private ServerScene serverScene = new ServerScene();
    Main(Stage stage){
        server = new Server(8090);
        game = new Game(new World(),2);
        handler = new Handler(server,game);

        stage.setScene(serverScene.scene());
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //saveWorld();
                System.out.println("服务器已断开");
                disconnect();
                System.exit(0);
            }
        });
        stage.show();
    }
    public void start(){
        registerButton();
    }
    private void registerButton(){
        serverScene.listenButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                restart();
                serverScene.listenButton.setDisable(true);
                //serverScene.number.setDisable(true);
            }
        });
        serverScene.resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                game.setLimit((int)serverScene.limit.getValue());
            }
        });

        serverScene.loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text = serverScene.loadButton.getText();
                int count = game.getWorld().getPlayers().stream()
                        .filter(player -> player.isOnline())
                        .collect(Collectors.toList()).size();

                if(count > 0){
                    UIHelper.prompt("提示","游戏正在进行，不可更改地图");
                    return;
                }

                if(text.equals("加载地图")){
                    game.setWorld(Factory.loadWorld("world"));

                    UIHelper.prompt("提示","地图已加载");
                    serverScene.loadButton.setText("重置地图");
                }
                else{
                    serverScene.loadButton.setText("加载地图");
                    game.setWorld(Factory.emptyWorld());
                    //game.setWorld(world);

                    UIHelper.prompt("提示","地图已重置");
                }
            }
        });

        serverScene.saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SaveUtil.saveWorld(getWorld(),"world");
                UIHelper.prompt("提示","保存地图成功");
            }
        });

        serverScene.npcButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(serverScene.npcButton.getText().equals("开始加入npc"))
                {
                    npcThread = new AddNPCThread();
                    npcThread.start();
                    serverScene.npcButton.setText("停止加入npc");
                }
                else{
                    npcThread.interrupt();
                    serverScene.npcButton.setText("开始加入npc");
                }
            }
        });
    }
    private Server server;
    private Game game;
    private Handler handler;
    private World getWorld(){
        return game.getWorld();
    }
    private void restart(){
        int number = 2;
        int port = 8090;
        try {
            number = (int)serverScene.limit.getValue();
            port = Integer.parseInt(serverScene.port.getText());
            server.setPort(port);
            game.setLimit(number);
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

        System.out.println("服务器正在启动中，请稍等……");
        server.start();
    }
    private void disconnect(){

    }
    class AddNPCThread extends Thread{
        @Override
        public void run() {
            while(!Thread.currentThread().interrupted()){
                try {
                    TimeUnit.MILLISECONDS.sleep(5000);
                }
                catch (InterruptedException e){
                    break;
                }
                if(game.getWorld().countThing(NPTank.class) >= 5){
                    continue;
                }
                else{
                    game.addNPC();
                }
            }
        }
    }
    private AddNPCThread npcThread = new AddNPCThread();
}
