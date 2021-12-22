package app.base;

import app.base.request.SendAble;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class World implements SendAble {
    private static final long serialVersionUID = 233L;
    public static int WIDTH = 680;
    public static int HEIGHT = 680;
    private int state = 0;
    public World(){
        restart();
    }
    //private HashMap<String,Player>players;//error.
    private List<Player> players;

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void restart(){
        things = new CopyOnWriteArrayList<>();
        players = new CopyOnWriteArrayList<>();
        state = 0;
    }
    private List<Thing> things;
    public  List<Thing> things(){
        return things;
    }
    public void add(Thing thing){
        things.add(thing);
    }
    public void remove(Thing thing){
        things.remove(thing);
    }

    public final boolean outRange(double x,double y,double w,double h){
        return x < 0 || y < 0 || x + w > WIDTH || y + h > WIDTH;
    }

    public void render(GraphicsContext gc){
        gc.clearRect(0, 0, WIDTH,HEIGHT);
        synchronized (things){
            for(Thing thing:things){
                thing.render(gc);
            }
        }
    }
    public void setThings(List<Thing> things) {
        synchronized (things){
            this.things = things;
        }
    }

    public List<Thing> getThings() {
        return things;
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
    public Player getOtherPlayer(String id){
        for(Player player:players){
            if(!id.equals(player.getPlayerId())){
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
}
