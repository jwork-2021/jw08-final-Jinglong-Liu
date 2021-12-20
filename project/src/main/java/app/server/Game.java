package app.server;

import app.base.Player;
import app.base.request.SendAble;
import app.base.World;
import app.server.game.Factory;

import java.util.HashMap;

public class Game {
    public int state = 0;
    public World world = Factory.createWorld();
    public Game(){

    }
    public Game(World world){
        this.world = world;
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
