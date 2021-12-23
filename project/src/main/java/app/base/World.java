package app.base;

import app.base.request.SendAble;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class World implements SendAble {
    private static final long serialVersionUID = 2L;
    public static int WIDTH = 600;
    public static int HEIGHT = 600;
    private int state = 0;
    public World(){
        restart();
    }
    //private HashMap<String,Player>players;//error.
    private Set<Player> players;
    private List<Thing> grasses;

    public List<Thing> getGrasses() {
        return grasses;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void restart(){
        //stables = new CopyOnWriteArrayList<>();
        things = new CopyOnWriteArrayList<>();
        players = new CopyOnWriteArraySet<>();
        grasses = new ArrayList<>();
        //initBricks(brickPos);
        //initBricks(grassPos);
        addThings(brickPos,Brick.class,1,0,0);
        addThings(grassPos,Grass.class,1,0,1000);
        state = 0;
    }
    private List<Thing> things;
    public  List<Thing> things(){
        return things;
    }
    public void add(Thing thing){
        things.add(thing);
        if(thing instanceof Grass){
            grasses.add(thing);
        }
        else if(thing instanceof Player && !players.contains(thing)){
            players.add((Player) thing);
        }
    }
    public void remove(Thing thing){
        things.remove(thing);
    }

    public final boolean outRange(double x,double y,double w,double h){
        return x < 0 || y < 0 || x + w > WIDTH || y + h > WIDTH;
    }

    public void render(GraphicsContext gc){
        gc.clearRect(0, 0, WIDTH,HEIGHT);
        for(Thing thing:things){
            if(!(thing instanceof Grass)) {
                thing.render(gc);
            }
        }
        for(Thing thing:grasses){
            thing.render(gc);
        }
    }
    //public void setThings(List<Thing> things) {
    //    this.things = things;
    //}
    public void setWorld(World world){
        this.things = world.things;
        this.grasses = world.grasses;
        this.players = world.players;
    }
    public List<Thing> getThings() {
        return things;
    }
    public List<Thing>collideThings(Thing thing,double targetX,double targetY){
        return things.stream().filter(o -> thing.intersects(o) && thing!=o).collect(Collectors.toList());
    }
    public Thing collideThing(Thing thing,double targetX,double targetY){
        for(Thing other:getThings()){
            if(thing == other){
                continue;
            }
            if(thing.intersects(targetX,targetY,other)){
                return other;
            }
        }
        return null;
    }
    @Override
    public int getMask() {
        return 233;
    }
    public Player getPlayer(String id){
        for(Player player:players){
            if(id.equals(player.getPlayerId())){
                return player;
            }
        }
        return null;
    }

    public void removePlayer(Player player){
        things.remove(player);
        players.remove(player);
    }
    public int countThing(Class<? extends Thing>cls){
        return things().stream().
                filter(thing->thing.getClass() == cls)
                .collect(Collectors.toList()).size();
    }
    public void clearItem(Class<? extends Thing>cls){
        things = things.stream().
                filter(thing->thing.getClass() != cls)
                .collect(Collectors.toList());
    }
    public int onlinePlayerCount(){
        return players.stream().filter(player -> player.isOnline())
                .collect(Collectors.toList()).size();
    }
    public void addThings(int [][]pos,Class<? extends Thing>cls,double maxHP,double attackValue,double defenseValue){
        try {
            Constructor c = cls.getConstructor(World.class, double.class, double.class, double.class);
            for (int[] chunk : pos) {
                int x = chunk[0];
                int y = chunk[1];
                int chunkWidth = chunk[2];
                int chunkHeight = chunk[3];
                for (int p = x; p <= x + chunkWidth - 20; p += 20) {
                    for (int q = y; q <= y + chunkHeight - 20; q += 20) {
                        Thing thing = (Thing) c.newInstance(this, maxHP, attackValue, defenseValue);
                        thing.setPos(p, q);
                        this.add(thing);
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void clear(){
        things.clear();
        players.clear();
        grasses.clear();
        state = 0;
    }
    //x y width height
    private static final int[][] brickPos =
            {
                    {WIDTH/2-WIDTH/24, HEIGHT/2-HEIGHT/6, WIDTH/12, HEIGHT/3},
                    {WIDTH/4-WIDTH/24, HEIGHT/2-HEIGHT/6, WIDTH/12, HEIGHT/3},
                    {WIDTH/4*3-WIDTH/24, HEIGHT/2-HEIGHT/6, WIDTH/12, HEIGHT/3}
            };
    private static final int[][] waterPos = {
            {}
    };
    private static final int[][] grassPos = {
            {WIDTH/2+WIDTH/24 + (WIDTH/4-WIDTH/12)/4,HEIGHT/2-HEIGHT/12 ,(WIDTH/4-WIDTH/12)/2, HEIGHT/6},
            {WIDTH/4+WIDTH/24 + (WIDTH/4-WIDTH/12)/4,HEIGHT/2-HEIGHT/12 ,(WIDTH/4-WIDTH/12)/2, HEIGHT/6}
    };
}
