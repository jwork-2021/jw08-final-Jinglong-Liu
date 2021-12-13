package app.server;

import app.base.SendAble;
import app.base.SimpleRequest;
import app.base.Thing;
import app.util.ByteUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class Server extends Thread{
    private ServerSocketChannel ssc;
    private Selector selector;

    public Queue<ByteBuffer> queue = new ConcurrentLinkedDeque<>();
    private volatile List<SocketChannel> clientChannels = new LinkedList<>();

    /**
     * 私聊
     */
    public Queue<ByteBuffer> pmQueue = new ConcurrentLinkedDeque<>();
    public Queue<SocketChannel>pmChannels = new ConcurrentLinkedDeque<>();

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

        //frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        //animation = new Timeline();
        //animation.setCycleCount(Timeline.INDEFINITE);
        //animation.getKeyFrames().add(frame);
        //animation.play();
        new Step().start();
    }
    private class Step extends Thread{
        @Override
        public void run() {
            while(true){
                if(!map.isEmpty()){
                    try {
                        queue.offer(ByteUtil.getByteBuffer(game.world));
                        TimeUnit.MILLISECONDS.sleep(60);
                    } catch (IOException|InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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
                        System.out.println("用户连接成功");

                    }
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);
                        buffer.flip();
                        //String recv = new String(buffer.array());
                        //queue.offer(recv);
                        //channels.offer(sc);
                        //System.out.println(sc.getRemoteAddress() + recv);
                        //System.out.println(recv);
                        //new RecvHandler(sc.getRemoteAddress(),buffer).start();
                        handler.handle(sc,buffer);
                        sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    }

                    if (key.isWritable()) {
                        //synchronized (this){
                        //    if(!queue.isEmpty()){
                        //        SocketChannel sc = channels.poll();
                        //        sc.write(ByteBuffer.wrap(queue.poll().getBytes()));
                        //        sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        //    }
                        //}
                        broadcast();
                        send();
                    }
                    it.remove();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * broadcast
     * @throws IOException
     */
    private void broadcast() throws IOException {
        if(!queue.isEmpty()){
            ByteBuffer byteBuffer = queue.poll();
            for(SocketChannel sc:clientChannels){
                sc.write(byteBuffer);
                sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            }
        }
    }

    /**
     * private message
     * @throws IOException
     */
    private void send() throws IOException{
        if(!pmQueue.isEmpty()){
            ByteBuffer byteBuffer = pmQueue.poll();
            SocketChannel sc = pmChannels.poll();
            sc.write(byteBuffer);
            sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
    }

    private class RecvHandler extends Thread{
        private SocketAddress address;
        private ByteBuffer byteBuffer;
        RecvHandler(SocketAddress address,ByteBuffer buffer){
            this.byteBuffer = buffer;
            this.address = address;
        }
        @Override
        public void run() {
            handle(address,byteBuffer);
        }
        private void handle(SocketAddress address,ByteBuffer buffer){
            SendAble o = null;
            try {
                o = (SendAble) ByteUtil.getObject(buffer);
            } catch (ClassNotFoundException  | IOException e) {
                e.printStackTrace();
            }
            if(o instanceof SimpleRequest){
                switch (((SimpleRequest) o).getRequest()){
                    case "player1":
                    case "player2":

                    default:
                        break;
                }
            }
        }
        private void registerPlayer(String player){

        }
    }
}

