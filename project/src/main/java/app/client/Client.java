package app.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class Client extends Thread {
    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private String playerId;
    private SocketChannel sc;
    private Selector selector;
    public Queue<ByteBuffer> queue = new ConcurrentLinkedDeque<>();//

    private void connect(String host,int port){
        try {
            sc = SocketChannel.open();
            selector = Selector.open();
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_CONNECT);
            sc.connect(new InetSocketAddress(host, port));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        connect("localhost",8090);
        try{
            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    //connect.
                    if (key.isConnectable()) {
                        SocketChannel scx = (SocketChannel) key.channel();
                        while (!scx.finishConnect())
                            ;
                        handler.connect();
                        scx.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                    }
                    // writable
                    if (key.isWritable()) {
                        SocketChannel scx = (SocketChannel) key.channel();
                        if(!queue.isEmpty()){
                            scx.write(queue.poll());
                        }
                        scx.register(selector,SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                    }
                    // readable
                    if (key.isReadable()) {
                        SocketChannel scx = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        scx.read(buffer);
                        buffer.flip();
                        //System.out.println(new String(buffer.array(), 0, buffer.limit()));
                        //System.out.println(playerId + " recv: "+ new String(buffer.array()));
                        handler.handle(buffer);
                        //scx.register(selector,  SelectionKey.OP_WRITE);
                        scx.register(selector,SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                    }
                    it.remove();
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
