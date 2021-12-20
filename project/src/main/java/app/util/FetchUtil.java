package app.util;

import app.base.Player;
import app.base.World;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class FetchUtil {
    private static Serializable fetch(Class<? extends Serializable>cls,String url){
        Object o = null;
        try {
            o = cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    new FileInputStream(url));
            try {
                o = objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (Serializable) o;
    }
    public static World fetchWorld(String url){
        World world = (World) fetch(World.class,url);
        for(Player player:world.getPlayers()){
            player.setOnline(false);
        }
        System.out.println(world.getPlayers().size());
        return world;
    }
}
