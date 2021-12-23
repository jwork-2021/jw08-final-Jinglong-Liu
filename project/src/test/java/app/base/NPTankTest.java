package app.base;

import app.server.game.Factory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class NPTankTest {
    NPTank tank;
    World world;
    Thread thread;
    @Before
    public void setUp() throws Exception {
        world = new World();
        tank = Factory.createNPTank(world);
        thread = new Thread(tank);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void moveBy() {
        assertFalse(thread.isAlive());
        Player player = Factory.createPlayer(world,"player0");
        tank.setPos(0,0);
        player.setPos(tank.width + 1, 0);
        assertFalse(tank.intersects(player));
        double hp = player.getHp();
        tank.moveBy(5,0);
        assertFalse(tank.intersects(player));
        assertEquals(hp - (tank.getAttackValue() - player.getDefenseValue())
                ,player.getHp(),0);
        assertEquals(0,tank.getX(),0);
    }

    @Test
    public void setImage() {
        tank.setImage();
        assertEquals(tank.getImageFile(),Tank.WHITE_TANK_UP);
    }

    @Test
    public void shoot() {
        tank.shoot();
        int bulletSize = world.getThings().stream()
                .filter(o->(o instanceof Bullet))
                .collect(Collectors.toList()).size();
        assertEquals(1,bulletSize);
        Bullet bullet = (Bullet) world.getThings().stream()
                .filter(o->(o instanceof Bullet))
                .collect(Collectors.toList()).get(0);
        assertFalse(tank.intersects(bullet));
    }

    @Test
    public void setDirection() {
        tank.setDirection(Direction.DOWN);
        assertEquals(Tank.WHITE_TANK_DOWN,tank.getImageFile());
        tank.setDirection(Direction.LEFT);
        assertEquals(Tank.WHITE_TANK_LEFT,tank.getImageFile());
    }


    @Test
    public void testSetImage() {
    }

    @Test
    public void getImage() {
    }

    @Test
    public void getImageFile() {
    }

    @Test
    public void testMoveBy() {
    }

    @Test
    public void attack() {
    }

    @Test
    public void move() {
        NPTank tank1 = Factory.createNPTank(world);
        assertFalse(tank1.intersects(tank));
        tank1.setPos(0,0);
        tank.setPos(0, tank1.height + 1);
        assertFalse(tank1.intersects(tank));

        tank1.setDirection(Direction.DOWN);
        assertTrue(tank1.intersects(0,10,tank));

        double hp = tank.getHp();
        tank1.move(10);
        assertEquals(0,tank1.getY(),0);
        assertNotEquals(hp,tank.getHp(),0);
        assertEquals(hp -
                (tank1.getAttackValue() - tank.getDefenseValue()),
                tank.getHp(),0);
        assertFalse(tank1.intersects(tank));
    }
}