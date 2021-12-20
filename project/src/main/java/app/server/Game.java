package app.server;

import app.base.Player;
import app.base.request.SendAble;
import app.base.World;
import app.server.game.Factory;
import app.util.SaveUtil;

import java.util.HashMap;

public class Game {
    public int limit;
    public World world;
    public Game(){
        world = Factory.createWorld();
    }
    public Game(World world,int number){
        this.world = world;
        this.limit = number;
    }
    public void restart(){
        world.restart();
    }
    public Player getPlayer(String playerId) {
        return world.getPlayer(playerId);
    }
    public Player getTheOtherPlayer(String playerId){
        return world.getOtherPlayer(playerId);
    }
    public void saveWorld(){
        SaveUtil.saveWorld(world,"world");
    }
}
