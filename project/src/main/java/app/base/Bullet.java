package app.base;

public class Bullet extends Thing{
    private static final long serialVersionUID = 100L;
    private int attack = 10;
    public Bullet(World world,int attack){
        super(world,1,2,0);
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }
}
