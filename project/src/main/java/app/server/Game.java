package app.server;

import app.base.NPTank;
import app.base.Player;
import app.base.World;
import app.server.game.Factory;
import app.util.SaveUtil;
import app.util.ThreadPoolUtil;

public class Game {
    private int limit;
    private World world;
    public Game(World world,int number){
        this.world = world;
        this.limit = number;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public void restart(){
        world.restart();
    }
    public Player getPlayer(String playerId) {
        return world.getPlayer(playerId);
    }

    public void saveWorld(){
        SaveUtil.saveWorld(world,"world");
    }

    public void addNPC(){
        NPTank npc = Factory.createNPTank(world);
        ThreadPoolUtil.execute(npc);
    }
}
