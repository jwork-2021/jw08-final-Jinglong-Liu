package app.util;

import app.base.Config;
import app.base.Player;
import app.base.World;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class FetchUtil {
    private static Serializable fetch(Class<? extends Serializable>cls,String url)throws Exception{
        Object o = null;
        o = cls.newInstance();
        ObjectInputStream objectInputStream = new ObjectInputStream(
                    new FileInputStream(url));
        o = objectInputStream.readObject();
        return (Serializable) o;
    }
    public static World fetchWorld(String url){
        try{
            World world = (World) fetch(World.class,url);
            for(Player player:world.getPlayers()){
                player.setOnline(false);
            }
            return world;
        }
        catch (Exception ignored){

        }
        return new World();
    }
    public static Config getConfig(String fileName)throws Exception{
        Config config = (Config) fetch(Config.class,fileName);
        return config;
    }
}
