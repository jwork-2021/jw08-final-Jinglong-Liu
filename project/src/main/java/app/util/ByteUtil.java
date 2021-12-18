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
    public static byte[] getBytes(Serializable obj) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        out.flush();
        byte[] bytes = bout.toByteArray();
        bout.close();
        out.close();
        return bytes;
    }
    public static int sizeof(Serializable obj) throws IOException {
        return getBytes(obj).length;
    }

    public static Object getObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        Object obj = oi.readObject();
        bi.close();
        oi.close();
        return obj;
    }

    public static Object getObject(ByteBuffer byteBuffer) throws ClassNotFoundException, IOException {
        InputStream input = new ByteArrayInputStream(byteBuffer.array());
        ObjectInputStream oi = new ObjectInputStream(input);
        Object obj = oi.readObject();
        input.close();
        oi.close();
        byteBuffer.clear();
        return obj;
    }

    public static ByteBuffer getByteBuffer(Serializable obj) throws IOException {
        byte[] bytes = ByteUtil.getBytes(obj);
        ByteBuffer buff = ByteBuffer.wrap(bytes);
        return buff;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Bullet b1 = new Bullet(null,12);
        b1.setHp(1);
        byte[] bytes = getBytes(b1);
        Bullet b2 = (Bullet)getObject(bytes);
        System.out.println(b2.getHp());

        System.out.println(b1.hashCode());
        System.out.println(b2.hashCode());
        System.out.println(b1 == b2);
        System.out.println(b1.equals(b2));
        HashSet<Thing>set = new HashSet<>();
        set.add(b1);
        set.add(b2);
        System.out.println(set.size());
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        System.out.println(new Date());
        System.out.println(new Date());

        HashMap<String, Player>hashMap = new HashMap<>();
        hashMap.put("a", Factory.createPlayer(new World(),"aa"));
        ByteBuffer buffer = ByteUtil.getByteBuffer(hashMap);
        hashMap = (HashMap<String, Player>) ByteUtil.getObject(buffer);
        System.out.println(hashMap.get("a").getHp());
    }
}
