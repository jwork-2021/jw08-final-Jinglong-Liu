package app.client;

import app.base.request.SendAble;
import app.util.ByteUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
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
    //ObjectOutputStream out;
    ObjectInputStream in;
    Socket socket;
    private Handler handler;
    public void setHandler(Handler handler) {
        this.handler = handler;
        this.socket = new Socket();
    }

    private String playerId;
    private SocketChannel sc;
    private Selector selector;
    //public Queue<ByteBuffer> queue = new ConcurrentLinkedDeque<>();//

    private void connect(String host,int port){
        try {
            sc = SocketChannel.open();
            //selector = Selector.open();
            //sc.configureBlocking(false);
            //sc.register(selector, SelectionKey.OP_CONNECT);
            sc.connect(new InetSocketAddress(host, port));
            handler.connect();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    private void runWithSocket(){
        try {
            socket.connect(new InetSocketAddress("localhost", 8090));
            if (socket.isConnected()) {
                handler.connect();
                while(true){
                    //out = new ObjectOutputStream(socket.getOutputStream());
                    in = new ObjectInputStream(socket.getInputStream());

                    //out.writeObject("Hello!");
                    //out.flush();
                    Object s;
                    if ((s = in.readObject()) != null) {
                        //System.out.print(s);
                        handler.handle(s);
                    }
                    //out.close();
                    //in.close();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        connect("localhost",8090);
        try{
            while(true){
                readBuffer.clear();
                sc.read(readBuffer);
                if(readBuffer.hasRemaining()){
                    handler.handle(readBuffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
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
        }*/
    }
    public void send(SendAble o){
        try{
            //ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            //out.write(ByteUtil.getBytes(o));
            //out.flush();
            //out.close();
            //ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
            sc.write(ByteUtil.getByteBuffer(o));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
