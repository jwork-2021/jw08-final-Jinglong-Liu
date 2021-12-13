package app.base;

public class Player extends Tank{
    private static final long serialVersionUID = 100001L;
    private String playerId;
    private double originX;
    private double originY;

    public String getPlayerId() {
        return playerId;
    }

    public void resetPos(){
        setPos(originX,originY);
    }
    public Player(World world,String id,double originX,double originY){
        super(world);
        this.playerId = id;
        this.originX = originX;
        this.originY = originY;
        setDirection(Direction.UP);
        setPos(originX,originY);
    }
}
