package app.server.game;

import app.base.NPTank;
import app.base.Player;
import app.base.Tank;
import app.base.World;
import app.util.FetchUtil;

import java.util.Random;

public class Factory {
    public static Player createPlayer(World world,String id){
        Player player = new Player(world,id,25,25,4,2,1);
        double width = (int)player.getWidth();
        double height = (int)player.getHeight();
        while(world.outRange(player.getX(), player.getY(), width, height)
        || world.collideThing(player, player.getX(), player.getY())!=null){
            Random random = new Random();
            double x = random.nextInt((int) (world.WIDTH - 2 * width)) + (int)width;
            double y = random.nextInt((int) (world.HEIGHT - 2 * height)) + (int)height;
            player.setPos(x,y);
        }
        world.add(player);
        return player;
    }
    public static void createNPTank(World world){
        NPTank npc = new NPTank(world,2,2,1);
        npc.setWhite();
        addTank(world,npc);
        new Thread(npc).start();
    }
    private static void addTank(World world,Tank tank){
        double width = (int)tank.getWidth();
        double height = (int)tank.getHeight();
        while(world.outRange(tank.getX(), tank.getY(), width, height)
                || world.collideThing(tank, tank.getX(), tank.getY())!=null){
            Random random = new Random();
            double x = random.nextInt((int) (world.WIDTH - 2 * width)) + (int)width;
            double y = random.nextInt((int) (world.HEIGHT - 2 * height)) + (int)height;
            tank.setPos(x,y);
        }
        world.add(tank);
    }
    public static World loadWorld(String fileName){
        return FetchUtil.fetchWorld(fileName);
    }
    public static World emptyWorld(){
        return new World();
    }
}
