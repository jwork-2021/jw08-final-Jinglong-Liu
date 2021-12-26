package app.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Server extends Thread{
    private ServerSocketChannel ssc;
    private Selector selector;

    private Handler handler;

    private int port = 8090;
    private int playerNumber = 2;
    public Server(int port){
        port = port;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    private void bind() throws IOException{
        ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器已启动，端口:" + port);
    }
    @Override
    public void run(){
        try {
            this.bind();
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
                            handler.channelQueueHashMap.put(sc,new ConcurrentLinkedDeque<>());
                            System.out.println("用户连接成功");
                        }
                        if (key.isReadable()) {
                            SocketChannel sc = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int count = sc.read(buffer);
                            buffer.flip();
                            handler.handle(sc,buffer);
                            sc.register(selector,  SelectionKey.OP_WRITE);
                        }
                        if (key.isWritable()) {
                            SocketChannel sc = (SocketChannel) key.channel();
                            handler.write(sc);
                            sc.register(selector, SelectionKey.OP_READ);
                        }
                    }
                    catch (IOException e){
                        System.out.println("断开连接");
                        SocketChannel sc = (SocketChannel) key.channel();
                        handler.handleOffline(sc);
                        sc.close();
                        it.remove();
                        continue;
                    }
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error\n\n");
        }
    }
}

