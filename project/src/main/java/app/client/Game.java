package app.client;

import app.base.LoginRequest;
import app.base.Player;
import app.base.SimpleRequest;
import app.base.World;
import app.client.Client;
import app.client.screen.PlayScreen;
import app.client.screen.RestartScreen;
import app.client.screen.Screen;
import app.util.ByteUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import sun.font.ScriptRun;

import java.io.IOException;
enum State{
    INIT,CONNECTED,PLAY,LOST,WIN;
}
public class Game {
    State state;
    private Handler handler;
    private Client client;
    private GraphicsContext graphicsContext;
    private Scene scene;
    private Stage stage;
    private Screen screen;
    private World world;
    private static final int FRAMES_PER_SECOND = 60;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    String playerId;
    private KeyFrame frame;
    private Timeline animation;
    Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Game(Stage stage){
        this.stage = stage;
        this.client = new Client();
        this.handler = new Handler(this);
        handler.setClient(client);
        client.setHandler(handler);
        this.state = State.INIT;
        this.world = new World();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    System.out.println("Quit game");
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public World getWorld() {
        return world;
    }

    public void restart(){
        screen = new RestartScreen();
        scene = ((RestartScreen) screen).restartScene();
        stage.setTitle("Tank Battle");
        stage.setScene(scene);
        stage.setResizable(false);

        ((RestartScreen) screen).startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playRequest(((RestartScreen) screen).choiceBox);
            }
        });
        ((RestartScreen) screen).connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String host = ((RestartScreen) screen).host.getText();
                String port = ((RestartScreen) screen).port.getText();
                connectRequest(host,Integer.valueOf(port));
            }
        });
        stage.show();
    }
    public void start(){
        restart();
    }
    private void connectRequest(String host,int port){
        client.start();
    }
    public void connectSucceed(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //更新JavaFX的主线程的代码放在此处
                ((RestartScreen) screen).connectButton.setText("连接成功");
                ((RestartScreen) screen).connectButton.setDisable(true);
            }
        });
    }
    public void playRequest(ChoiceBox choiceBox){
        String player = choiceBox.getValue().toString();
        try {
            client.queue.offer(ByteUtil.getByteBuffer(new LoginRequest(player)));
            this.playerId = player;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void play(){
        this.state = State.PLAY;
        screen = new PlayScreen(client);
        scene = ((PlayScreen) screen).playScene();
        graphicsContext = ((PlayScreen) screen).getGraphicsContext();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.setScene(scene);
                frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
                animation = new Timeline();
                animation.setCycleCount(Timeline.INDEFINITE);
                animation.getKeyFrames().add(frame);
                animation.play();
            }
        });
    }
    public void fail(){

    }
    public void win(){

    }
    private void register(){

    }
    private void step(double elapsedTime){
        world.render(graphicsContext);
    }
}
