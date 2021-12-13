package app.server;

import app.base.Player;
import app.base.request.SendAble;
import app.base.World;

import java.util.HashMap;

public class Game {
    private Handler handler;
    Player player1;
    Player player2;
    public int state = 0;
    public World world = new World();
    public Game(Handler handler){
        this.handler = handler;
    }
    public void over(){
        player1 = null;
        player2 = null;
    }
    public HashMap<String,Player>players = new HashMap<>();

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    public boolean registerPlayer(String playerId){
        return false;
    }
    private void send(SendAble o){

    }
}
