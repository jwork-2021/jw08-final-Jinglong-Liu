package app;

import app.base.World;
import app.base.request.LoginRequest;
import app.client.Client;
import app.client.State;
import app.server.Game;
import app.server.Server;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class GameTest {
    Server server;
    app.server.Game serverGame;
    app.server.Handler serverHandler;
    app.client.Game[] clientGame = new app.client.Game[2];
    Client[] clients = new Client[2];
    app.client.Handler[] clientHandler = new app.client.Handler[2];
    @Before
    public void setUp() throws Exception {
        server = new Server(8090);
        serverGame = new Game(new World(),2);
        serverHandler = new app.server.Handler(server,serverGame);
        assertEquals(serverHandler.game, serverGame);
        server.start();

        for(int i = 0;i<2;i++){
            clients[i] = new Client();
            clientGame[i] = new app.client.Game(null);
            clientHandler[i] = new app.client.Handler(clientGame[i]);
            clients[i].setHandler(clientHandler[i]);
            clients[i].setHost("127.0.0.1");
            clients[i].setPort(8090);
            clients[i].start();
            clients[i].connect("127.0.0.1",8090);
        }
    }

    @After
    public void tearDown() throws Exception {
        server.interrupt();
    }
    @Test
    public void testGame() {
        try{
            assertEquals(0,clientGame[0].getWorld().getPlayers().size());
            clientGame[0].setPlayerId("player1");
            clientGame[1].setPlayerId("player2");
            clients[0].send(new LoginRequest("player1"));
            clients[1].send(new LoginRequest("player2"));
            TimeUnit.MILLISECONDS.sleep(500);
            assertTrue(serverGame.getPlayer("player1").isOnline());
            TimeUnit.MILLISECONDS.sleep(500);
            assertEquals(2,clientGame[0].getWorld().getPlayers().size());

            serverGame.getWorld().getPlayer("player1").setHp(1);
            TimeUnit.MILLISECONDS.sleep(1000);
            assertEquals(1,clientGame[0].getWorld().getPlayer("player1").getHp(),0);
            serverGame.getWorld().getPlayer("player1").setPos(0,0);
            serverGame.getWorld().getPlayer("player2").setPos(0,50);
            TimeUnit.MILLISECONDS.sleep(1000);
            assertEquals(0,clientGame[0].getWorld().getPlayer("player2").getX(),0);
            assertEquals(0,clientGame[1].getWorld().getPlayer("player2").getX(),50);
            assertTrue(serverHandler.channelIdHashMap().containsValue("player1"));
            clientGame[1].keyPress(KeyCode.W);
            clientGame[1].keyPress(KeyCode.W);
            clientGame[1].keyPress(KeyCode.W);
            clientGame[1].keyPress(KeyCode.W);
            TimeUnit.MILLISECONDS.sleep(1000);
            assertEquals(app.client.State.WIN,clientGame[1].getState());
            assertEquals(app.client.State.LOSE,clientGame[0].getState());

            assertFalse(serverHandler.channelIdHashMap().containsValue("player1"));
            assertFalse(serverHandler.channelIdHashMap().containsValue("player2"));
        }
        catch (Exception e){

        }
    }
}