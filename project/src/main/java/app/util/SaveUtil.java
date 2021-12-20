package app.util;

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
    public static boolean saveWorld(World world,String url){
        return save(world,url);
    }
}
