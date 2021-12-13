package app.base;

public class Bullet extends Thing{
    private static final long serialVersionUID = 100L;
    private int attack = 10;
    public Bullet(int attack){
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }
}
