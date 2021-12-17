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

    private Handler handler;
    public void setHandler(Handler handler) {
        this.handler = handler;

    }

    private String playerId;
    SocketChannel sc;
    private Selector selector;


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
            //e.printStackTrace();
            return;
        }
    }
    public void send(SendAble o){
        try{
            sc.write(ByteUtil.getByteBuffer(o));
        }
        catch (IOException e){
            //e.printStackTrace();
            return;
        }
    }

}
