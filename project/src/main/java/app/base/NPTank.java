package app.base;


import java.util.Random;
import java.util.concurrent.TimeUnit;

public class NPTank extends Tank implements Runnable{
    private static final long serialVersionUID = 5L;
    public NPTank(World world, double maxHP, double attackValue, double defenseValue) {
        super(world, maxHP, attackValue, defenseValue);
    }

    @Override
    public void run() {
        while(getHp() > 0){
            try{
                step();
                TimeUnit.MILLISECONDS.sleep(300);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    private void step(){
        Direction direction = getDirection();
        if(new Random().nextInt(8) == 0){
            turnRandomly();
        }
        if(new Random().nextInt(8) == 0){
            shoot();
        }
        else{
            move(8);
        }
    }
    private void turnRandomly(){
        int pro = new Random().nextInt(4);
        switch (pro){
            case 0:
                setDirection(Direction.UP);
                break;
            case 1:
                setDirection(Direction.DOWN);
                break;
            case 2:
                setDirection(Direction.LEFT);
                break;
            case 3:
                setDirection(Direction.RIGHT);
                break;
            default:
                break;
        }
    }
}
