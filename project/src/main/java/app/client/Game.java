package app.client;

import app.base.request.KeyCodeRequest;
import app.base.request.LoginRequest;
import app.base.World;
import app.client.ui.screen.*;
import app.util.SaveUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;


public class Game {
    volatile State state;
    private Handler handler;
    private Client client;
    private GraphicsContext graphicsContext;
    private Scene scene;
    private Stage stage;
    private Screen screen;
    private World world;
    private static final int FRAMES_PER_SECOND = 30;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    String playerId;
    private KeyFrame frame;
    private Timeline animation;

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public State getState() {
        return state;
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public Game(Stage stage){
        this.stage = stage;
        //this.client = new Client();
        this.handler = new Handler(this);

        this.state = State.INIT;
        this.world = new World();
        if(stage == null)return;
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
        world.restart();

        screen = rScreen;
        scene = ((RestartScreen) screen).restartScene();

        stage.setTitle("Tank Battle");
        stage.setScene(scene);
        stage.setResizable(false);

        ((RestartScreen) screen).startButton.setOnAction((e)->{
            playRequest(((RestartScreen) screen).choiceBox);
        });
        ((RestartScreen) screen).connectButton.setOnAction((e)-> {
            String host = ((RestartScreen) screen).host.getText();
            String port = ((RestartScreen) screen).port.getText();
            SaveUtil.saveConfig(host, Integer.valueOf(port));
            connectRequest(host,Integer.valueOf(port));
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
        if(stage==null)return;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ((RestartScreen) screen).connectButton.setText("连接成功");
                ((RestartScreen) screen).connectButton.setDisable(true);
                ((RestartScreen) screen).startButton.setDisable(false);
            }
        });
    }
    public void playRequest(ChoiceBox choiceBox){
        String player = choiceBox.getValue().toString();

        client.send(new LoginRequest(player));
        this.playerId = player;

    }
    public void play(){
        this.state = State.PLAY;
        if(stage == null)return;
        screen = new PlayScreen(client);
        scene = ((PlayScreen) screen).playScene();
        graphicsContext = ((PlayScreen) screen).getGraphicsContext();
        //System.out.println(Thread.currentThread().getName());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //System.out.println(Thread.currentThread().getName());
                stage.setScene(scene);
                stage.setTitle(playerId);
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
        if(stage!=null){
            Platform.runLater(()->{
                restart(new LoseScreen());
            });
        }
    }
    public void win(){
        this.state = State.WIN;
        if(stage!=null){
            Platform.runLater(()->{
                restart(new WinScreen());
            });
        }
    }

    private void step(double elapsedTime){
        world.render(graphicsContext);
    }
    public void keyPress(KeyCode keyCode){
        KeyCodeRequest r = new KeyCodeRequest(playerId,keyCode);
        //if(this.state == State.PLAY){
        client.send(r);
        //}
    }
}
