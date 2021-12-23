package app.server;

import app.base.*;
import app.base.request.*;
import app.server.game.Factory;
import app.util.ByteUtil;
import app.util.FetchUtil;
import app.util.SaveUtil;
import app.util.ThreadPoolUtil;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Handler{
    public Game game;
    private static int playerNum = 0;
    public void handle(SocketChannel channel,ByteBuffer buffer){
        new RecvHandler(server,channel,buffer).start();
    }
    private Server server;
    volatile HashMap<SocketChannel,String> channelIdHashMap = new HashMap<>();
    volatile HashMap<SocketChannel, Queue<ByteBuffer>>channelQueueHashMap = new HashMap<>();
    /**
     * constructor
     * @param server
     */

    public Handler(Server server,Game game){
        this.server = server;
        server.setHandler(this);
        this.game = game;
    }
    private class RecvHandler extends Thread{
        private SocketChannel channel;
        private ByteBuffer byteBuffer;
        private Server server;
        RecvHandler(Server server,SocketChannel channel,ByteBuffer buffer){
            this.server = server;
            this.byteBuffer = buffer;
            this.channel = channel;
        }
        @Override
        public void run() {
            handle(channel,byteBuffer);
        }
        private void handle(SocketChannel channel,ByteBuffer buffer){
            SendAble o = null;
            try {
                o = (SendAble) ByteUtil.getObject(buffer);
            } catch (ClassNotFoundException  | IOException e) {
                e.printStackTrace();
            }

            if(o instanceof LoginRequest){
                handleLoginRequest(channel, (LoginRequest) o,buffer);
            }
            else if(o instanceof StateRequest){
                try {
                    channelQueueHashMap.get(channel).add(ByteUtil.getByteBuffer(game.getWorld()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(o instanceof KeyCodeRequest){
                String id = ((KeyCodeRequest) o).getPlayerId();
                KeyCode keyCode = ((KeyCodeRequest) o).getKeyCode();
                Player player = game.getPlayer(id);
                switch (keyCode){
                    case W:
                        player.setDirection(Direction.UP);
                        player.moveBy(0,-5);
                        break;
                    case S:
                        player.setDirection(Direction.DOWN);
                        player.moveBy(0,5);
                        break;
                    case A:
                        player.setDirection(Direction.LEFT);
                        player.moveBy(-5,0);
                        break;
                    case D:
                        player.setDirection(Direction.RIGHT);
                        player.moveBy(5,0);
                        break;
                    case J:
                        player.shoot();
                        break;
                    default:
                        break;
                }
            }

            else if(o instanceof MessageRequest){
                SendAble response = new MessageResponse(((MessageRequest) o).getMessage());
                broadcast(response);
            }
        }
    }
    public int checkState(SocketChannel channel){
        String id = channelIdHashMap.getOrDefault(channel,null);
        if(id == null || game.getPlayer(id) == null){
            return 0;
        }
        if(game.getPlayer(id).getHp() <= 0){
            return 1;
        }
        //else if(game.getTheOtherPlayer(id)!=null
        //        && game.getTheOtherPlayer(id).getHp() <= 0){
        //   return 1;
        //}
        else if(game.getWorld().getPlayers().size() >= 2 &&
                game.getWorld().getPlayers().stream()
                        .filter(player -> player.getHp() > 0 && !player.getPlayerId().equals(id))
                        .collect(Collectors.toList()).isEmpty()){
            return 2;
        }
        return 0;
    }
    public void setGameState(int state){
        game.getWorld().setState(state);
    }
    public void handleOffline(SocketChannel channel){
        channelQueueHashMap.remove(channel);
        String id = channelIdHashMap.getOrDefault(channel,null);
        if(id != null){
            System.out.println(id);
            Player player = game.getPlayer(id);
            player.setOnline(false);
            SaveUtil.saveWorld(game.getWorld(),"world");
            broadcast(new MessageResponse(id + " 离线"));
        }
    }
    public void saveWorld(){
        game.saveWorld();
    }
    public void write(SocketChannel sc){
        int state = checkState(sc);
        setGameState(Math.max(game.getWorld().getState(),state));
        String id = channelIdHashMap.getOrDefault(sc,null);
        if(state == 1){
            try {
                sc.write(ByteUtil.getByteBuffer(GameResult.loserResult(id)));
                game.getWorld().getPlayer(id).setOnline(false);
                //game.getWorld().removePlayer(game.getPlayer(id));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(state == 2){
            try {
                sc.write(ByteUtil.getByteBuffer(GameResult.winnerResult(id)));
                game.getWorld().getPlayer(id).setOnline(false);
                //game.getWorld().removePlayer(game.getPlayer(id));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(channelQueueHashMap.containsKey(sc) && !channelQueueHashMap.get(sc).isEmpty()){
            try {
                sc.write(channelQueueHashMap.get(sc).poll());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void handleLoginRequest(SocketChannel channel,LoginRequest o,ByteBuffer buffer){
        synchronized (game.getWorld()){
            String id = o.getId();
            //登录成功，发回player.
            if(game.getWorld().getState() == 2){
                game.setWorld(new World());
                System.out.println("reset");
                handle(channel,buffer);
                return;
            }
            Player player = game.getPlayer(id);
            if(player!= null){
                if(player.getHp() <= 0){
                    System.out.println("挂了");
                    try {
                        channel.write(ByteUtil.getByteBuffer(new LoginFailResponse(id,"fail")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if(player.isOnline()){
                    System.out.println(id + "重复登录");
                    try {
                        channel.write(ByteUtil.getByteBuffer(new LoginFailResponse(id,"already")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                player.setOnline(true);
            }
            else{
                if(game.getWorld().getPlayers().size() >= game.getLimit()){
                    System.out.println("超过人数上限");
                    try {
                        channel.write(ByteUtil.getByteBuffer(new LoginFailResponse(id,"limit")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                player = Factory.createPlayer(game.getWorld(), id);

                game.getWorld().getPlayers().add(player);
            }
            channelIdHashMap.put(channel,id);
            try {
                System.out.println("用户 " + id +  " 登录成功");
                channel.write(ByteUtil.getByteBuffer(player));
                //for(Queue queue:channelQueueHashMap.values()){
                //    queue.offer(ByteUtil.getByteBuffer(new MessageResponse("用户 " + id + "登录成功")));
                //}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void broadcast(SendAble o){
        for(Queue queue:channelQueueHashMap.values()){
            try {
                queue.offer(ByteUtil.getByteBuffer(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void broadcast(ByteBuffer buffer){
        for(Queue queue:channelQueueHashMap.values()){
            queue.offer(buffer);
        }
    }
}

