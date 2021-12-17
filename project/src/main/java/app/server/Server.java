package app.server;

import app.base.request.SendAble;
import app.base.request.SimpleRequest;
import app.util.ByteUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class Server extends Thread{
    private ServerSocketChannel ssc;
    private Selector selector;

    public Queue<MyNode> queue = new ConcurrentLinkedDeque<>();

    static class MyNode{
        ByteBuffer buffer;
        SocketChannel socketChannel;
        private MyNode(ByteBuffer buffer,SocketChannel socketChannel){
            this.buffer = buffer;
            this.socketChannel = socketChannel;
        }
        static MyNode allocate(ByteBuffer buffer,SocketChannel socketChannel){
            return new MyNode(buffer,socketChannel);
        }
    }
    volatile HashMap<SocketChannel,Queue<ByteBuffer>>channelQueueHashMap = new HashMap<>();
    private volatile List<SocketChannel> clientChannels = new LinkedList<>();


    /**
     * id-channel hashMap.
     */
    volatile HashMap<String,SocketChannel>map;
    private KeyFrame frame;
    private Timeline animation;
    private static final int FRAMES_PER_SECOND = 60;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public Game game;
    public Handler handler;
    public Server(){
        handler = new Handler(this);
        game = handler.game;
        map = new HashMap<>();
    }


    @Override
    public  void run(){

        try {
            ssc = ServerSocketChannel.open();
            ssc.bind(new InetSocketAddress(8090));
            selector = Selector.open();
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("start listening...");
            while (true) {
                int nReady = selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    if (key.isAcceptable()) {
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        this.clientChannels.add(sc);
                        this.channelQueueHashMap.put(sc,new ConcurrentLinkedDeque<>());
                        System.out.println("用户连接成功");
                    }
                    if (key.isReadable()) {
                        //System.out.println("readable");
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);

                        buffer.flip();
                        handler.handle(sc,buffer);
                        sc.register(selector,  SelectionKey.OP_WRITE);
                    }

                    if (key.isWritable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        //System.out.println("writable");
                        if(channelQueueHashMap.containsKey(sc) && !channelQueueHashMap.get(sc).isEmpty()){
                            sc.write(channelQueueHashMap.get(sc).poll());
                            //System.out.println("send to sb");
                        }
                        //else{
                            //System.out.println("empty");
                            //sc.write(ByteUtil.getByteBuffer(game.world));
                        //}
                        sc.register(selector, SelectionKey.OP_READ);
                    }
                    it.remove();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    public void broadcast(SendAble o){
        try {
            ByteBuffer byteBuffer = ByteUtil.getByteBuffer(o);
            // 当前在selector上注册的所有key就是所有用户
            Set keys = selector.keys();
            //System.out.println(keys.size());
            for (Object key : keys) {
                //if(((SelectionKey)key).isWritable()){
                    // 获取每个用户的通道
                    SelectableChannel channel = ((SelectionKey)key).channel();
                    // 实现数据发送
                    if (channel instanceof SocketChannel) {
                        SocketChannel socketChannel = (SocketChannel) channel;
                        socketChannel.write(byteBuffer);
                        socketChannel.register(selector,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    }
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}

