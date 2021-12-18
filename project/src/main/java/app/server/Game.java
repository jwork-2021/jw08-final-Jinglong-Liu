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
    public void restart(){
        state = 0;
        world.restart();
    }
    public Player getPlayer(String playerId) {
        return world.getPlayer(playerId);
    }
    public Player getTheOtherPlayer(String playerId){
        return world.getOtherPlayer(playerId);
    }

}
