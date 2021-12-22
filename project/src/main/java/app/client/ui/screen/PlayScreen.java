package app.client.ui.screen;

import app.base.Bullet;
import app.base.request.SendAble;
import app.base.request.SimpleRequest;
import app.client.Client;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.omg.CORBA.BAD_CONTEXT;

import static app.base.World.WIDTH;
import static app.base.World.HEIGHT;
public class PlayScreen extends Screen{
    private GraphicsContext graphicsContext;

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }
    BorderPane root;

    public BorderPane getRoot() {
        return root;
    }
    public TextArea textArea = textArea();
    public TextField textField = textField();
    public Button button = sendButton();
    public Canvas canvas;
    private Client client;
    public PlayScreen(Client client){
        this.client = client;
    }
    public Scene playScene(){
        canvas = new Canvas(WIDTH, HEIGHT);
        canvas.setStyle("-fx-background-color: black;");
        root = new BorderPane();
        root.setStyle("-fx-background-color: black;");
        root.setCenter(canvas);
        graphicsContext = canvas.getGraphicsContext2D();
        textArea = textArea();

        //root.setBottom(textArea);
        //root.setRight(textArea);
        Scene scene = new Scene(root,WIDTH,HEIGHT);
        return scene;
    }
    private TextArea textArea(){
        TextArea area = new TextArea();
        area.setDisable(true);
        area.setPrefWidth(150);
        area.setPrefHeight(HEIGHT);

        area.setOpacity(1);
        area.setFont(Font.font("FangSong",15));
        area.setStyle("-fx-control-inner-background:white;");
        area.layout();

        return area;
    }
    private TextField textField(){
        TextField textField = new TextField();
        textField.setDisable(true);
        textField.setPrefHeight(65);
        textField.setPrefWidth(WIDTH/4);
        return textField;
    }
    private Node bottom(){
        HBox box = new HBox();
        box.getChildren().addAll(this.textArea,rightNode());
        return box;
    }
    private Button sendButton(){
        Button button = new Button("发送");
        button.setDisable(false);
        button.setMaxWidth(WIDTH/4);
        button.setMinWidth(WIDTH/4);
        button.setPrefHeight(35);
        return button;
    }
    private Node rightNode(){
        VBox box = new VBox();
        box.setPrefHeight(100);
        box.getChildren().addAll(this.textField,this.button);
        return box;
    }
}
