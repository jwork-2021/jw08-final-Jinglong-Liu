package app.base;

public class Grass extends Thing{
    private static final long serialVersionUID = 22L;

    public Grass(World world, double maxHP, double attackValue, double defenseValue) {
        super(world, maxHP, attackValue, defenseValue);
        setImage(GRASS_IMG);
    }
    public Grass(World world) {
        super(world, 1, 0, 0x3f3f3f3f);
        setImage(GRASS_IMG);
    }

    @Override
    public void setImage() {
        //todo nothing
    }
    private static final String GRASS_IMG = "grass.gif";
}
