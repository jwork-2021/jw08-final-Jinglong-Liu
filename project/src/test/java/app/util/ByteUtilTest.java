package app.util;

import app.base.Bullet;
import app.base.Player;
import app.base.Tank;
import app.base.World;
import app.server.game.Factory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class ByteUtilTest {
    private World world;
    private Tank tank;
    @Before
    public void setUp() throws Exception {
        world = new World();
        tank = Factory.createPlayer(world,"player1");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getBytes() {
        Bullet b1 = new Bullet(null,12,2,1);
        b1.setHp(-12345);
        byte[] bytes =ByteUtil.getBytes(b1);
        Bullet b2 = (Bullet)ByteUtil.getObject(bytes);
        assertEquals(b1.getHp(),b2.getHp(),0.000001);
    }

    @Test
    public void getObject() {
        ByteBuffer buffer = ByteUtil.getByteBuffer(tank);
        Tank newTank = (Tank) ByteUtil.getObject(buffer);
        assertEquals(tank.getImageFile(),newTank.getImageFile());
    }

    @Test
    public void testGetObject() {
        byte[] bytes = ByteUtil.getBytes(tank);
        Tank newTank = (Tank) ByteUtil.getObject(bytes);
        assertEquals(tank.getImageFile(),newTank.getImageFile());
    }

    @Test
    public void getByteBuffer() {
        ByteBuffer buffer = ByteUtil.getByteBuffer(world);
        World newWorld = (World) ByteUtil.getObject(buffer);
        assertEquals(world.getThings().size(),newWorld.getThings().size());
        assertEquals(world.getPlayer("player1").getAttackValue(),
                newWorld.getPlayer("player1").getAttackValue(),0);
    }
}