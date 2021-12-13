package app.base;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.Serializable;

public class Thing implements SendAble {
    private static final long serialVersionUID = 1L;
    private double x;
    private double y;
    private int hp;
    //private Image image;
    private String imageFile;
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
    }

    public void setImage(String filename) {
        this.imageFile = filename;
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
}
