package app.base;

public class Tank extends Thing{
    private static final long serialVersionUID = 100000L;
    protected Direction direction;
    public Tank(World world,double maxHP,double attackValue,double defenseValue){
        super(world,maxHP,attackValue,defenseValue);
        setHp(4);
        setDirection(Direction.UP);
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
        setWhite();
        this.setImage();
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
            if(other!= null){
                attack(other);
            }
            else{
                setPos(targetX,targetY);
            }
        }
    }

    public void setImage() {
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
