package app.base;

import app.base.request.SendAble;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Thing implements SendAble {
    private static final long serialVersionUID = 1L;
    private double x;
    private double y;
    private double hp;
    //private Image image;
    private String imageFile;
    protected World world;
    protected double width;
    protected double height;
    private Direction direction;
    public void setDirection(Direction direction) {
        this.direction = direction;
        this.setImage();
    }

    public Direction getDirection() {
        return direction;
    }
    public double getWidth() {
        return width;
    }
    public void setImage(){

    }
    public double getHeight() {
        return height;
    }

    private double attackValue;
    private double defenseValue;

    public double getAttackValue() {
        return attackValue;
    }

    public double getDefenseValue() {
        return defenseValue;
    }

    public Thing(World world,double maxHP,double attackValue,double defenseValue){
        this.world = world;
        this.hp = maxHP;
        this.attackValue = attackValue;
        this.defenseValue = defenseValue;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(getImage(), x, y);//ok.
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setPos(double x,double y) {
        this.x = x;
        this.y = y;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void modifyHp(double amount){
        hp += amount;
        if(hp <= 0){
            world.remove(this);
        }
    }

    public void setImage(String filename) {
        this.imageFile = filename;
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageFile));
        height = image.getHeight();
        width = image.getWidth();
        //System.out.println("height = " + height);
        //System.out.println("width = " + width);
    }

    public Image getImage() {
        return new Image(getClass().getClassLoader().getResourceAsStream(imageFile));
    }

    public String getImageFile() {
        return imageFile;
    }

    @Override
    public int getMask() {
        return 1;
    }

    /**
     *
     * @param dx
     * @param dy
     */
    public void moveBy(double dx,double dy){
        x += dx;
        y += dy;
    }
    public void attack(Thing other){
        double damage = Math.max(getAttackValue() - other.getDefenseValue(),0);
        other.modifyHp(-damage);
    }
    public boolean outRange(double targetX,double targetY){
        return world.outRange(targetX,targetY,height,width);
    }

    private Rectangle2D getRect() {
        return new Rectangle2D(x,y,width,height);
    }
    private Rectangle2D getRect(double x, double y){
        return new Rectangle2D(x, y, width, height);
    }
    public boolean intersects(double targetX,double targetY,Thing thing) {
        return getRect(targetX,targetY).intersects(thing.getRect());
    }
    public boolean intersects(Thing thing) {
        return getRect().intersects(thing.getRect());
    }
    public void move(int distance){
        distance = Math.abs(distance);
        switch (getDirection()){
            case LEFT:
                moveBy(-distance,0);
                break;
            case UP:
                moveBy(0,-distance);
                break;
            case DOWN:
                moveBy(0,distance);
                break;
            case RIGHT:
                moveBy(distance,0);
                break;
        }
    }
    public void outRangeAction(){

    }
}
