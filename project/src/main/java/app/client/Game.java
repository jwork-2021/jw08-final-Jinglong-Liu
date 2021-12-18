package app.client;

import app.base.request.KeyCodeRequest;
import app.base.request.LoginRequest;
import app.base.Player;
import app.base.World;
import app.client.screen.*;
import app.util.ByteUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
enum State{
    INIT,CONNECTED,PLAY,LOSE,WIN;
}
public class Game {
    volatile State state;
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
        //this.client = new Client();
        this.handler = new Handler(this);
        //handler.setClient(client);
        //client.setHandler(handler);
        this.state = State.INIT;
        this.world = new World();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    System.out.println("Quit game");
                    client.sc.close();
                    System.exit(0);
                } catch (Exception ignored) {

                }
            }
        });
    }

    public World getWorld() {
        return world;
    }

    public void restart(RestartScreen rScreen){
        //screen = new RestartScreen();
        world.restart();
        screen = rScreen;
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
        restart(new RestartScreen());
    }
    private void connectRequest(String host,int port){
        client = new Client();
        client.setHandler(handler);
        client.setHost(host);
        client.setPort(port);
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

            //client.queue.offer(ByteUtil.getByteBuffer(new LoginRequest(player)));
            client.send(new LoginRequest(player));
            this.playerId = player;

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
                scene.setOnKeyPressed(e->keyPress(e.getCode()));
            }
        });
    }
    public void lose(){
        this.state = State.LOSE;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                restart(new LoseScreen());
            }
        });
    }
    public void win(){
        this.state = State.WIN;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                restart(new WinScreen());
            }
        });
    }

    private void step(double elapsedTime){
        world.render(graphicsContext);
    }
    private void keyPress(KeyCode keyCode){
        KeyCodeRequest r = new KeyCodeRequest(playerId,keyCode);
        if(this.state == State.PLAY){
            client.send(r);
        }
    }
}
