package app.server;

import app.base.request.SendAble;
import app.base.request.SimpleRequest;
import app.util.ByteUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Server extends Thread{
    private ServerSocketChannel ssc;
    private Selector selector;

    volatile HashMap<SocketChannel,Queue<ByteBuffer>>channelQueueHashMap = new HashMap<>();

    public Game game;
    public Handler handler;
    public Server(){
        handler = new Handler(this);
        game = handler.game;
    }
    @Override
    public void run(){
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
                    try{
                        if (key.isAcceptable()) {
                            SocketChannel sc = ssc.accept();
                            sc.configureBlocking(false);//
                            sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

                            this.channelQueueHashMap.put(sc,new ConcurrentLinkedDeque<>());
                            System.out.println("用户连接成功");
                        }
                        if (key.isReadable()) {
                            //System.out.println("readable");
                            SocketChannel sc = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int count = sc.read(buffer);

                            buffer.flip();
                            handler.handle(sc,buffer);
                            sc.register(selector,  SelectionKey.OP_WRITE);
                        }
                        if (key.isWritable()) {
                            SocketChannel sc = (SocketChannel) key.channel();
                            //System.out.println("writable");
                            if(channelQueueHashMap.containsKey(sc) && !channelQueueHashMap.get(sc).isEmpty()){
                                sc.write(channelQueueHashMap.get(sc).poll());
                            }
                            sc.register(selector, SelectionKey.OP_READ);
                        }
                    }
                    catch (IOException e){
                        System.out.println("断开连接");
                        SocketChannel sc = (SocketChannel) key.channel();
                        sc.close();
                        this.channelQueueHashMap.remove(sc);
                        it.remove();
                        continue;
                    }
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

