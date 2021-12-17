package app.base;

import app.base.request.SendAble;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Thing implements SendAble {
    private static final long serialVersionUID = 1L;
    private double x;
    private double y;
    private int hp;
    //private Image image;
    private String imageFile;
    protected World world;
    protected double width;
    protected double height;
    public Thing(World world){
        this.world = world;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void modifyHp(int amount){
        hp -= amount;
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

    }
    public boolean outRange(double targetX,double targetY){
        return world.outRange(targetX,targetY,height,width);
    }
}
