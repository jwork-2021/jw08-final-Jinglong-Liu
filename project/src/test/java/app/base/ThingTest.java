package app.base;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ThingTest {
    World world;
    Thing thing;
    @Before
    public void setUp() throws Exception {
        world = new World();
        thing = new Thing(world,10,3,1);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setDirection() {
        thing.setDirection(Direction.UP);
        assertEquals(Direction.UP,thing.getDirection());
        thing.setDirection(Direction.DOWN);
        assertEquals(Direction.DOWN,thing.getDirection());
        thing.setDirection(Direction.LEFT);
        assertEquals(Direction.LEFT,thing.getDirection());
        thing.setDirection(Direction.RIGHT);
        assertEquals(Direction.RIGHT,thing.getDirection());
    }

    @Test
    public void getDirection() {

    }

    @Test
    public void getAttackValue() {
        Thing thing = new Thing(world,3,2,1);
        assertEquals(2,thing.getAttackValue(),0);
    }

    @Test
    public void getDefenseValue() {
        Thing thing = new Thing(world,3,2,1);
        assertEquals(1,thing.getDefenseValue(),0);
    }

    @Test
    public void render() {
    }

    @Test
    public void getX() {
    }

    @Test
    public void getY() {
    }

    @Test
    public void setPos() {
        Thing thing = new Thing(world,0,0,0);
        thing.setPos(233,456);
        assertEquals(233,thing.getX(),0);
        assertEquals(456,thing.getY(),0);
    }

    @Test
    public void getHp() {
        Thing thing = new Thing(world,56,0,0);
        assertEquals(56,thing.getHp(),0);
    }

    @Test
    public void setHp() {
        Thing thing = new Thing(world,0,0,0);
        thing.setHp(12);
        assertEquals(12,thing.getHp(),0);
    }

    @Test
    public void modifyHp() {
        Thing thing = new Thing(world,23,0,0);
        thing.modifyHp(-12);
        assertEquals(11,thing.getHp(),0);
        thing.modifyHp(-11);
        assertEquals(0,thing.getHp(),0);
        assertFalse(thing.world.getThings().contains(thing));
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
    public void getMask() {
    }

    @Test
    public void moveBy() {
    }

    @Test
    public void attack() {
    }

    @Test
    public void outRange() {
        Grass thing = new Grass(world,23,0,0);
        assertTrue(thing.outRange(0,-1));
        assertFalse(thing.outRange(0,0));
        assertFalse(thing.outRange(World.HEIGHT- thing.getHeight(),
                World.WIDTH - thing.getWidth()));
        assertTrue(thing.outRange(World.HEIGHT- thing.getHeight() + 1,
                World.WIDTH - thing.getWidth()));
    }

    @Test
    public void intersects() {
        Grass g1 = new Grass(world,1,0,0);
        Grass g2 = new Grass(world,1,0,0);
        g1.setPos(0,0);
        g2.setPos(20,20);
        assertFalse(g1.intersects(g2));
        g1.setPos(1,1);
        assertTrue(g1.intersects(g2));
    }

    @Test
    public void testIntersects() {
        Grass g1 = new Grass(world,1,0,0);
        Grass g2 = new Grass(world,1,0,0);
        g2.setPos(20,20);
        assertFalse(g1.intersects(0,0,g2));
        assertTrue(g1.intersects(1,1,g2));
    }

    @Test
    public void move() {

    }

    @Test
    public void outRangeAction() {
    }
}