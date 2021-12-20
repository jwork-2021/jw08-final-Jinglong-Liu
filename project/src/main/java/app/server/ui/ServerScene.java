package app.server.ui;

import app.base.World;
import app.server.Game;
import app.server.Handler;
import app.server.Server;
import app.util.FetchUtil;
import app.util.SaveUtil;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerScene {
    //private Stage stage;
    private TextField port = new TextField("8090");
    private Button loadButton = new Button("加载进度");
    private Button restartButton = new Button("重新开始");
    private Button saveButton = new Button("保存进度");
    private Button listenButton = new Button("启动服务");


    private Server server;
    private Handler handler;
    private Game game;
    private World world;
    public ServerScene(Stage stage){
        //this.stage = stage;
        stage.setScene(scene());
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                saveWorld();
                System.exit(0);
            }
        });
    }
    private Scene scene(){
        BorderPane root = new BorderPane();
        VBox vBox = new VBox();
        HBox hbox = new HBox();
        Label label = new Label("端口号  ");

        hbox.getChildren().add(label);
        port.setPrefWidth(100);
        hbox.getChildren().add(port);
        hbox.setAlignment(Pos.CENTER);
        vBox.setPrefWidth(300);

        HBox hBox2 = new HBox();
        hBox2.getChildren().addAll(listenButton,new Label("   "),
                loadButton,new Label());
        hBox2.setAlignment(Pos.CENTER);
        HBox hBox3 = new HBox();
        hBox3.getChildren().addAll(restartButton,new Label("   "),
                saveButton);
        hBox3.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(hbox,new Label(),hBox2,new Label(),hBox3
                );
        vBox.setAlignment(Pos.CENTER);
        root.setCenter(vBox);
        Scene scn = new Scene(root, 400, 400);
        return scn;
    }
    public void start(){

    }
    private void loadWorld(){
        world = FetchUtil.fetchWorld("world");
    }
    private void saveWorld(){
        if(world != null){
            SaveUtil.saveWorld(world,"world");
        }
    }

}
