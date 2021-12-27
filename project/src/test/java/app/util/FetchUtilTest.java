package app.util;

import app.base.Config;
import app.base.Player;
import app.base.World;
import app.server.game.Factory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class FetchUtilTest {
    @Test
    public void fetchWorld() {
        World world = Factory.createWorld();
        Factory.createPlayer(world,"p1");
        Factory.createPlayer(world,"p2");
        Factory.createPlayer(world,"p3");
        SaveUtil.saveWorld(world,"testWorld");
        World newWorld = FetchUtil.fetchWorld("testWorld");
        for(Player player:world.getPlayers()){
            Player newPlayer = newWorld.getPlayer(player.getPlayerId());
            assertEquals(player.getX(),newPlayer.getX(),0);
            assertEquals(player.getY(),newPlayer.getY(),0);
            assertEquals(player.getAttackValue(),newPlayer.getAttackValue(),0);
            assertEquals(player.getHp(),newPlayer.getHp(),0);
        }
    }

    @Test
    public void getConfig() throws Exception {
        Config[] configs = new Config[]{
                new Config("12.34.56.78",2333),
                new Config("127,56,98,11",2546),
                new Config("192.168.54.55",9852),
        };
        for(Config config:configs){
            SaveUtil.saveConfig(config.getHost(),config.getPort());
            Config newConfig = FetchUtil.getConfig("config");
            assertEquals(config.getHost(),newConfig.getHost());
            assertEquals(config.getPort(),newConfig.getPort());
            assertNotEquals(config.hashCode(),newConfig.hashCode());
        }
    }
}