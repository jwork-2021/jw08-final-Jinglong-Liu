package app.server;

import app.base.Player;
import app.base.request.SendAble;
import app.base.World;

import java.util.HashMap;

public class Game {


    public int state = 0;
    public World world = new World();
    public Game(){

    }

    public HashMap<String,Player>players = new HashMap<>();

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }
    public Player getTheOtherPlayer(String playerId){
        for(String str: players.keySet()){
            if(!str.equals(playerId)){
                return players.get(str);
            }
        }
        return null;
    }
    public void removePlayer(String playerId){
        players.remove(playerId);
    }
    public boolean registerPlayer(String playerId){
        return false;
    }

}
