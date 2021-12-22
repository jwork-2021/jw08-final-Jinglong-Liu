package app.base;

import java.util.concurrent.TimeUnit;

public class Bullet extends Thing implements Runnable{
    private static final long serialVersionUID = 100L;
    private Tank owner;

    public void setOwner(Tank owner) {
        this.owner = owner;
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        while(getHp() > 0){
            try {
                TimeUnit.MILLISECONDS.sleep(35);
            }
            catch (InterruptedException e){
                setHp(0);
            }
            move(5);
            //System.out.println(getX() + " " + getY());
        }
        world.remove(this);
    }
    @Override
    public void moveBy(double dx, double dy) {
        double targetX = getX() + dx;
        double targetY = getY() + dy;
        if(this.outRange(targetX,targetY)){
            setHp(0);
        }
        else{
            Thing other = world.collideThing(this,targetX,targetY);
            if(other!= null){
                attack(other);
            }
            else{
                setPos(targetX,targetY);
            }
        }
    }

    @Override
    public void attack(Thing other) {
        if(other == owner){
            return;
        }
        super.attack(other);
        setHp(0);
    }

    public Bullet(World world, double maxHp,double attackValue,double defenseValue){
        super(world,maxHp,attackValue,defenseValue);
    }


    public void setImage(){
        switch (getDirection()){
            case UP:
                setImage(MISSILE_UP);
                break;
            case DOWN:
                setImage(MISSILE_DOWN);
                break;
            case LEFT:
                setImage(MISSILE_LEFT);
                break;
            case RIGHT:
                setImage(MISSILE_RIGHT);
                break;
            default:
                break;
        }
    }
    private static final String MISSILE_UP = "missile-up.gif";
    private static final String MISSILE_DOWN = "missile-down.gif";
    private static final String MISSILE_LEFT = "missile-left.gif";
    private static final String MISSILE_RIGHT = "missile-right.gif";
}
