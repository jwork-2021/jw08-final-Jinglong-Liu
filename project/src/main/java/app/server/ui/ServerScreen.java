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
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerScreen {
    //private Stage stage;
    public ChoiceBox limit = new ChoiceBox(FXCollections.observableArrayList(
            2, 3,4,5,6));
    //public ChoiceBox mapSize = new ChoiceBox(FXCollections.observableArrayList(
         //   300,400,480,540,600,660));
    public TextField port = new TextField("8090");
    public Button loadButton = new Button("加载地图");
    public Button resetButton = new Button("更改人数");
    public Button saveButton = new Button("保存进度");
    public Button listenButton = new Button("启动服务");
    public Button npcButton = new Button("开始加入npc");
    //public Button resetMapButton = new Button("重置地图");
    public ServerScreen(){
        limit.setValue(2);
    }
    public Scene scene(){
        BorderPane root = new BorderPane();
        VBox vBox = new VBox();
        HBox hbox = new HBox();
        Label label = new Label("端口号  ");
        label.setPrefWidth(100);
        hbox.getChildren().add(label);
        port.setPrefWidth(100);
        hbox.getChildren().add(port);
        hbox.setAlignment(Pos.CENTER);
        /***********************************/
        HBox hBox1 = new HBox();
        limit.setPrefWidth(100);
        hBox1.getChildren().addAll(limit,resetButton);
        hBox1.setAlignment(Pos.CENTER);
        /***********************************/
        //HBox resetBox = new HBox();
        //mapSize.setPrefWidth(100);
        //mapSize.setValue(480);
        //resetBox.getChildren().addAll(mapSize,resetMapButton);
        //resetBox.setAlignment(Pos.CENTER);
        vBox.setPrefWidth(300);
        /***********************************/
        HBox hBox2 = new HBox();
        hBox2.getChildren().addAll(listenButton,new Label("   "),
                loadButton,new Label());
        hBox2.setAlignment(Pos.CENTER);
        /***********************************/
        HBox hBox3 = new HBox();
        hBox3.getChildren().addAll(npcButton,new Label("  "),
                saveButton,new Label());
        hBox3.setAlignment(Pos.CENTER);
        /**********************************/
        vBox.getChildren().addAll(hbox,new Label(),
               // resetBox,new Label(),
                hBox1,new Label(),
                hBox2,new Label(),
                hBox3
                );


        vBox.setAlignment(Pos.CENTER);
        root.setCenter(vBox);
        Scene scn = new Scene(root, 400, 400);
        return scn;
    }
}
