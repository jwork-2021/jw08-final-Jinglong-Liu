package app.client;

import app.base.request.SendAble;
import app.util.ByteUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client extends Thread {
    String host;
    int port;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private Handler handler;
    public static int BUFFER_SIZE = 16384;
    public void setHandler(Handler handler) {
        this.handler = handler;
        handler.setClient(this);
    }
    SocketChannel sc;

    public void connect(String host,int port){
        try {
            sc = SocketChannel.open();
            sc.connect(new InetSocketAddress(host, port));
            //block until connect.
            handler.connect();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            connect(host, port);
            while (sc.isConnected()) {
                readBuffer.clear();
                sc.read(readBuffer);
                if (readBuffer.hasRemaining()) {
                    handler.handle(readBuffer);
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
           handler.connectionClose();
        }
    }
    public void send(SendAble o){
        try{
            sc.write(ByteUtil.getByteBuffer(o));
        }
        catch (IOException e){
            return;
        }
    }

}
