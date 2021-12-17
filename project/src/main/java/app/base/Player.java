package app.base;


public class Player extends Tank{
    private static final long serialVersionUID = 100001L;
    private String playerId;
    private double originX;
    private double originY;
    private PlayerState state = PlayerState.INIT;
    public void setState(PlayerState state){
        this.state = state;
    }
    public PlayerState getState() {
        return state;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void resetPos(){
        setPos(originX,originY);
    }
    public Player(World world,String id,double originX,double originY,double maxHP,double attackValue,double defenseValue){
        super(world,maxHP,attackValue,defenseValue);
        this.playerId = id;
        this.originX = originX;
        this.originY = originY;
        setDirection(Direction.UP);
        setPos(originX,originY);
    }

    @Override
    public void modifyHp(double amount) {
        super.modifyHp(amount);
        if(getHp() <= 0){
            setState(PlayerState.LOSE);
        }
    }
}
