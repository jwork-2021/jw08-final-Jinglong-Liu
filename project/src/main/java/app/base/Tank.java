package app.base;

import app.server.game.Factory;
import app.util.ThreadPoolUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tank extends Thing{
    private static final long serialVersionUID = 100000L;

    //private Direction direction;
    public Tank(World world,double maxHP,double attackValue,double defenseValue){
        super(world,maxHP,attackValue,defenseValue);
        setWhite();
        setHp(4);
        setDirection(Direction.UP);
    }


    @Override
    public void moveBy(double dx, double dy) {
        double targetX = getX() + dx;
        double targetY = getY() + dy;
        if(this.outRange(targetX,targetY)){
           //
        }
        else{
            Thing other = world.collideThing(this,targetX,targetY);
            if(other!= null && !(other instanceof Grass)){
                attack(other);
            }
            else{
                setPos(targetX,targetY);
            }
        }
    }
    @Override
    public void setImage() {
        Direction direction = getDirection();
        switch (direction){
            case UP:
                setImage(TANK_UP);
                break;
            case DOWN:
                setImage(TANK_DOWN);
                break;
            case LEFT:
                setImage(TANK_LEFT);
                break;
            case RIGHT:
                setImage(TANK_RIGHT);
                break;
            default:
                break;
        }
    }
    public void shoot(){
        System.out.println("shoot");
        //new Thread(Factory.createBullet(world,this)).start();
        //ThreadPoolUtil.execute(Factory.createBullet(world,this));
        Factory.createBullet(world,this);
        System.out.println("ok.");//not ok
    }
    /**
     * set white color
     */
    protected String TANK_UP;
    protected String TANK_DOWN;
    protected String TANK_LEFT;
    protected String TANK_RIGHT;
    public void setWhite() {
        TANK_UP = WHITE_TANK_UP;
        TANK_DOWN = WHITE_TANK_DOWN;
        TANK_LEFT = WHITE_TANK_LEFT;
        TANK_RIGHT = WHITE_TANK_RIGHT;
    }
    /**
     * set red color
     */
    public void setRed() {
        TANK_UP = RED_TANK_UP;
        TANK_DOWN = RED_TANK_DOWN;
        TANK_LEFT = RED_TANK_LEFT;
        TANK_RIGHT = RED_TANK_RIGHT;
    }
    /**
     * set green color
     */
    public void setGreen() {
        TANK_UP = GREEN_TANK_UP;
        TANK_DOWN = GREEN_TANK_DOWN;
        TANK_LEFT = GREEN_TANK_LEFT;
        TANK_RIGHT = GREEN_TANK_RIGHT;
    }
    public void setYellow(){
        TANK_UP = YELLOW_TANK_UP;
        TANK_DOWN = YELLOW_TANK_DOWN;
        TANK_LEFT = YELLOW_TANK_LEFT;
        TANK_RIGHT = YELLOW_TANK_RIGHT;
    }
    protected static final String WHITE_TANK_UP = "white-tank-up.gif";
    protected static final String WHITE_TANK_DOWN = "white-tank-down.gif";
    protected static final String WHITE_TANK_LEFT = "white-tank-left.gif";
    protected static final String WHITE_TANK_RIGHT = "white-tank-right.gif";
    protected static final String GREEN_TANK_UP = "green-tank-up.gif";
    protected static final String GREEN_TANK_DOWN = "green-tank-down.gif";
    protected static final String GREEN_TANK_LEFT = "green-tank-left.gif";
    protected static final String GREEN_TANK_RIGHT = "green-tank-right.gif";
    protected static final String RED_TANK_UP = "red-tank-up.gif";
    protected static final String RED_TANK_DOWN = "red-tank-down.gif";
    protected static final String RED_TANK_LEFT = "red-tank-left.gif";
    protected static final String RED_TANK_RIGHT = "red-tank-right.gif";
    protected static final String YELLOW_TANK_UP = "yellow-tank-up.gif";
    protected static final String YELLOW_TANK_DOWN = "yellow-tank-down.gif";
    protected static final String YELLOW_TANK_LEFT = "yellow-tank-left.gif";
    protected static final String YELLOW_TANK_RIGHT = "yellow-tank-right.gif";
}
