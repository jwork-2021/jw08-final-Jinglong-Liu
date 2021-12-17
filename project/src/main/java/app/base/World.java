package app.base;

import app.base.request.SendAble;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class World implements SendAble {
    private static final long serialVersionUID = 233L;
    public static int WIDTH = 600;
    public static int HEIGHT = 600;

    private List<Thing> things = new ArrayList<>();
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
    public Thing collideThing(Thing thing){
        return null;
    }
    @Override
    public int getMask() {
        return 233;
    }
}
