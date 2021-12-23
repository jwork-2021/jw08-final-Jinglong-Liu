package app.base;

import app.server.game.Factory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;

public class WorldTest {
    World world;
    @Before
    public void setUp() throws Exception {
        world = new World();
        Factory.createPlayer(world,"1");
        Factory.createPlayer(world,"2");
        Factory.createNPTank(world);
        Factory.createNPTank(world);
        Factory.createNPTank(world);
        Factory.createNPTank(world);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setPlayers() {
        World world1 = new World();
        Factory.createPlayer(world1,"3");
        world.setPlayers(world1.getPlayers());
        assertArrayEquals(world1.getPlayers().toArray(),world.getPlayers().toArray());
    }

    @Test
    public void restart() {
        world.restart();
        assertEquals(0,world.getPlayers().size());
        assertEquals(0,world.countThing(NPTank.class),0);
        assertEquals(0,world.countThing(Bullet.class),0);
        assertNotEquals(0,world.countThing(Brick.class),0);
    }

    @Test
    public void things() {
    }

    @Test
    public void add() {
        world.restart();
        world.clearItem(Grass.class);
        world.add(new Grass(world,1,0,0));
        assertEquals(1,world.countThing(Grass.class),0);
    }

    @Test
    public void remove() {
    }

    @Test
    public void outRange() {
    }

    @Test
    public void setWorld() {
        World world1 = Factory.emptyWorld();
        assertEquals(0,world1.getPlayers().size());
        assertEquals(0,world1.getThings().size());
        assertEquals(0,world1.getGrasses().size());

        world1.setWorld(world);

        assertArrayEquals(world.getPlayers().toArray(),world1.getPlayers().toArray());
        assertArrayEquals(world.getThings().toArray(),world1.getThings().toArray());
        assertArrayEquals(world.getGrasses().toArray(),world1.getGrasses().toArray());
    }

    @Test
    public void collideThings() {
    }

    @Test
    public void collideThing() {
    }

    @Test
    public void removePlayer() {
        assertEquals(1,world.getPlayers().stream()
                .filter((o)->{return o.getPlayerId().equals("1");})
                .count());
        int count = world.getThings().size();
        world.removePlayer(world.getPlayer("1"));
        assertEquals(0,world.getPlayers().stream()
                .filter((o)->{return o.getPlayerId().equals("1");})
                .count());
        assertEquals(count - 1,world.getThings().size());
    }

    @Test
    public void countThing() {
        World world1 = Factory.emptyWorld();
        assertEquals(0,world1.countThing(Player.class));
        assertEquals(0,world1.countThing(NPTank.class));
        Factory.createPlayer(world1,"13");
        Factory.createNPTank(world1);
        assertEquals(1,world1.countThing(Player.class));
        assertEquals(1,world1.countThing(NPTank.class));
    }

    @Test
    public void clearItem() {
        world.clearItem(Player.class);
        assertEquals(0,world.countThing(Player.class));
        assertNotEquals(0,world.countThing(Brick.class));
        world.clearItem(Brick.class);
        assertEquals(0,world.countThing(Brick.class));
    }

    @Test
    public void onlinePlayerCount() {
        assertNotEquals(0,world.getPlayers().size());
        assertNotEquals(0,world.onlinePlayerCount());
        world.getPlayers().forEach(o->o.setOnline(false));
        assertEquals(0,world.onlinePlayerCount());
    }

    @Test
    public void addThings() {
    }
}