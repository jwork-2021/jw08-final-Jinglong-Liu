package app.server;

import app.base.Player;
import app.base.request.SendAble;
import app.base.World;

import java.util.HashMap;

public class Game {
    private Handler handler;

    public int state = 0;
    public World world = new World();
    public Game(Handler handler){
        this.handler = handler;
    }

    public HashMap<String,Player>players = new HashMap<>();

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    public boolean registerPlayer(String playerId){
        return false;
    }

}
