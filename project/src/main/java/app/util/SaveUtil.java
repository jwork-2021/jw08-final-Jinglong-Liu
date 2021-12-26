package app.util;

import app.base.Bullet;
import app.base.Config;
import app.base.NPTank;
import app.base.World;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SaveUtil {
    private static boolean save(Serializable o,String url){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(url));
            objectOutputStream.writeObject(o);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    public static boolean saveWorld(World world,String fileName){
        world.clearItem(Bullet.class);
        world.clearItem(NPTank.class);
        return save(world,fileName);
    }
    public static boolean saveConfig(String host,int port){
        Config config = new Config(host,port);
        return save(config,"config");
    }
}
