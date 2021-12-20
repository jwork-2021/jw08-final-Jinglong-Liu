package app.client.ui.screen;

import app.base.request.SendAble;
import app.base.request.SimpleRequest;
import app.client.Client;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

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

    private Client client;
    public PlayScreen(Client client){
        this.client = client;
    }
    public Scene playScene(){
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        canvas.setStyle("-fx-background-color: black;");
        root = new BorderPane();
        root.setStyle("-fx-background-color: black;");
        root.setCenter(canvas);
        graphicsContext = canvas.getGraphicsContext2D();
        Scene scene = new Scene(root,WIDTH,HEIGHT);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }
    private void handleKeyInput(KeyCode code){
        SendAble request = new SimpleRequest(code.getName());

        //client.queue.offer(ByteUtil.getByteBuffer(request));
        client.send(request);

    }
}
