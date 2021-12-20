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

public class ServerScene {
    //private Stage stage;
    public ChoiceBox number = new ChoiceBox(FXCollections.observableArrayList(
            2, 3,4));
    public TextField port = new TextField("8090");
    public Button loadButton = new Button("加载地图");
    public Button restartButton = new Button("重新开始");
    public Button saveButton = new Button("保存进度");
    public Button listenButton = new Button("启动服务");



    public ServerScene(){
        number.setValue(2);
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

        HBox hBox1 = new HBox();
        Label label2 = new Label("人数  ");
        label2.setPrefWidth(100);
        number.setPrefWidth(100);
        hBox1.getChildren().addAll(label2,number);
        hBox1.setAlignment(Pos.CENTER);



        vBox.setPrefWidth(300);

        HBox hBox2 = new HBox();
        hBox2.getChildren().addAll(listenButton,new Label("   "),
                loadButton,new Label());
        hBox2.setAlignment(Pos.CENTER);
        HBox hBox3 = new HBox();
        hBox3.getChildren().addAll(restartButton,new Label("   "),
                saveButton);
        hBox3.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(hbox,new Label(),hBox1,new Label(),hBox2,new Label(),hBox3
                );
        vBox.setAlignment(Pos.CENTER);
        root.setCenter(vBox);
        Scene scn = new Scene(root, 400, 400);
        return scn;
    }


}