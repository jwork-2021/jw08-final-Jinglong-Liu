package app.server.game;

import app.base.*;
import app.util.FetchUtil;
import app.util.ThreadPoolUtil;

import java.util.Random;

public class Factory {
    public static Player createPlayer(World world,String id){
        Player player = new Player(world,id,-100,-200,4,2,1);
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
    public static NPTank createNPTank(World world){
        NPTank npc = new NPTank(world,1,2,1);
        npc.setWhite();
        addTank(world,npc);
        //new Thread(npc).start();
        //ThreadPoolUtil.execute(npc);
        return npc;
    }
    public static void createBullet(World world,Tank owner){
        Bullet bullet = new Bullet(world,1,owner.getAttackValue(),0);
        bullet.setDirection(owner.getDirection());
        bullet.setOwner(owner);
        bullet.setPos(owner.getX() + (owner.getWidth()- bullet.getWidth())/2,
                owner.getY() + (owner.getHeight() - bullet.getHeight())/2);

        while(bullet.intersects(owner)){
            switch (bullet.getDirection()){
                case UP:
                    bullet.setPos(bullet.getX(),bullet.getY()-5);
                    break;
                case DOWN:
                    bullet.setPos(bullet.getX(),bullet.getY()+5);
                    break;
                case LEFT:
                    bullet.setPos(bullet.getX() - 5,bullet.getY());
                    break;
                case RIGHT:
                    bullet.setPos(bullet.getX() + 5,bullet.getY());
                    break;
            }
        }


        world.add(bullet);
        ThreadPoolUtil.execute(bullet);
        //new Thread(bullet).start();
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
        World world = new World();
        world.clear();
        return world;
    }
    public static World createWorld(){
        World world = new World();
        return world;
    }
}
