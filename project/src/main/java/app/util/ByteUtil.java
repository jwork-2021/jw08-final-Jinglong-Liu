package app.util;

import app.base.Bullet;
import app.base.Player;
import app.base.Thing;
import app.base.World;
import app.server.game.Factory;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ByteUtil {
    public static byte[] getBytes(Serializable obj)  {
        try{
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(obj);
            out.flush();
            byte[] bytes = bout.toByteArray();
            bout.close();
            out.close();
            return bytes;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Object getObject(byte[] bytes){
        try{
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);
            Object obj = oi.readObject();
            bi.close();
            oi.close();
            return obj;
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Object getObject(ByteBuffer byteBuffer){
        try{
            InputStream input = new ByteArrayInputStream(byteBuffer.array());
            ObjectInputStream oi = new ObjectInputStream(input);
            Object obj = oi.readObject();
            input.close();
            oi.close();
            byteBuffer.clear();
            return obj;
        }
        catch (ClassNotFoundException |IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static ByteBuffer getByteBuffer(Serializable obj){
        byte[] bytes = ByteUtil.getBytes(obj);
        ByteBuffer buff = ByteBuffer.wrap(bytes);
        return buff;
    }
}
