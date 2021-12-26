package app.client.ui.screen;

import app.base.Config;
import app.base.World;
import app.util.FetchUtil;
import javafx.collections.FXCollections;
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

public class RestartScreen extends Screen{
    public Button connectButton;
    public Button startButton;
    public ChoiceBox choiceBox;
    public TextField host;
    public TextField port;
    public RestartScreen(){
        startButton = new Button("start game");
        startButton.setPrefWidth(120);
        connectButton = new Button("connect");
        //connectButton.setPrefWidth(120);
    }
    private void fetchConfig(){
        try {
            Config config = FetchUtil.getConfig("config");
            host = new TextField(config.getHost());
            port = new TextField(String.valueOf(config.getPort()));
        }
        catch (Exception e){
            e.printStackTrace();
            //System.out.println("load fail");
            host = new TextField("127.0.0.1");
            port = new TextField("8090");
        }
    }
    protected Node startNode() {
        this.fetchConfig();
        VBox buttons = new VBox();

        buttons.setPadding(new Insets(15, 12, 15, 12));
        buttons.setSpacing(100);


        Text text = new Text();
        text.setFont(new Font(16));
        //text.setWrappingWidth(400);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setText("WASD or arrow keys to move around\n\nJ to shoot\n\nProtect your home and destroy enemies");

        HBox start = new HBox();

        choiceBox = new ChoiceBox(FXCollections.observableArrayList(
                "player1", "player2","player3","player4","player5","player6")
        );
        choiceBox.setValue("player1");
        start.getChildren().addAll(choiceBox,startButton);
        start.setAlignment(Pos.CENTER);

        VBox connect = new VBox();
        connect.setPrefWidth(300);
        Label label1 = new Label("host ");
        Label label2 = new Label("port ");
        HBox box1 = new HBox();
        HBox box2 = new HBox();
        HBox box3 = new HBox();
        HBox line = new HBox();
        line.getChildren().add(new Label());
        HBox line1 = new HBox();
        line1.getChildren().add(new Label());
        box1.getChildren().addAll(label1,host);
        box2.getChildren().addAll(label2,port);
        box3.getChildren().addAll(connectButton);
        box1.setAlignment(Pos.CENTER);
        box2.setAlignment(Pos.CENTER);

        box3.setAlignment(Pos.CENTER);
        connect.getChildren().addAll(box1,box2,line,box3,line1,start);

        //connect.getChildren().add(start);
        connect.setAlignment(Pos.CENTER);
        connect.setCenterShape(true);
        //buttons.getChildren().addAll(text,connect,start);
        buttons.getChildren().addAll(connect);
        buttons.setAlignment(Pos.CENTER);

        return buttons;
    }

    /**
     * @return the title label on the start scene
     */
    protected Label title(String titleString) {
        Label title = new Label(titleString);
        title.setFont(new Font(20));
        title.setPadding(new Insets(15, 15, 15, 15));
        title.setTextAlignment(TextAlignment.CENTER);
        return title;
    }
    public Scene restartScene() {
        return restartScene("Tank Battle");
    }
    public Scene restartScene(String titleString) {
        BorderPane root = new BorderPane();
        Node startNode = startNode();
        Label title = title(titleString);
        root.setTop(title);
        root.setCenter(startNode);
        BorderPane.setAlignment(title, Pos.CENTER);
        Scene scn = new Scene(root, World.WIDTH, World.HEIGHT);
        return scn;
    }
}
