package app.base;

public class Brick extends Thing{
    private static final long serialVersionUID = 2L;

    public Brick(World world, double maxHP, double attackValue, double defenseValue) {
        super(world, maxHP, attackValue, defenseValue);
        setImage(BRICK_IMG);
    }
    public Brick(World world){
        super(world,1,0,0);
    }


    @Override
    public int getMask() {
        return super.getMask();
    }

    @Override
    public void setImage() {
        //todo nothing.
    }

    private static final String BRICK_IMG = "brick.gif";
}
