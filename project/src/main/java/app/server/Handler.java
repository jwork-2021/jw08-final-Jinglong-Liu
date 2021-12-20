package app.server;

import app.base.Direction;
import app.base.PlayerState;
import app.base.World;
import app.base.request.*;
import app.base.Player;
import app.server.game.Factory;
import app.util.ByteUtil;
import app.util.FetchUtil;
import app.util.SaveUtil;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class Handler{
    public Game game;
    private static int playerNum = 0;
    public void handle(SocketChannel channel,ByteBuffer buffer){
        new RecvHandler(server,channel,buffer).start();
    }
    private Server server;
    volatile HashMap<SocketChannel,String> channelIdHashMap = new HashMap<>();
    /**
     * constructor
     * @param server
     */
    public Handler(Server server){
        this.server = server;
        this.game = new Game();
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
                synchronized (game.world.getPlayers()){
                    String id = ((LoginRequest) o).getId();
                    System.out.println("用户 " + id +  " 登录成功");
                    //server.queue.offer(buffer);//登录成功信息
                    //登录成功，发回player.
                    if(game.state!=0){
                        game.restart();
                    }
                    Player player = game.getPlayer(id);
                    if(player!= null){
                        if(player.isOnline()){
                            System.out.println("重复登录");
                            try {
                                //登录信息反馈
                                //server.channelQueueHashMap.get(channel).add(ByteUtil.getByteBuffer(player));
                                channel.write(ByteUtil.getByteBuffer(new AlreadyLoginResponse(id)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        player.setOnline(true);
                        switch (player.getState()){
                            case LOSE:
                            case WIN:
                                player.setState(PlayerState.INIT);
                                player = Factory.createPlayer(game.world, id);
                                break;
                        }
                    }
                    else{
                        player = Factory.createPlayer(game.world, id);
                        game.world.getPlayers().add(player);
                    }

                    //game.players.put(id,player);
                    //game.world.getPlayers().put(id,player);
                    channelIdHashMap.put(channel,id);
                    try {
                        //登录信息反馈
                        //server.channelQueueHashMap.get(channel).add(ByteUtil.getByteBuffer(player));
                        channel.write(ByteUtil.getByteBuffer(player));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(o instanceof StateRequest){
                try {
                    server.channelQueueHashMap.get(channel).add(ByteUtil.getByteBuffer(game.world));
                } catch (IOException e) {
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
                        //attack.
                        break;
                    default:
                        break;
                }
            }
        }
    }
    public int checkState(SocketChannel channel){
        String id = channelIdHashMap.getOrDefault(channel,null);
        if(id == null || game.getPlayer(id) == null){
            return 0;
        }
        if(game.getPlayer(id).getHp() <= 0){
            return -1;
        }
        else if(game.getTheOtherPlayer(id)!=null && game.getTheOtherPlayer(id).getHp() <= 0){
            return 1;
        }
        return 0;
    }
    public void setGameState(int state){
        game.state = state;
    }
    public void handleOffline(SocketChannel channel){
        String id = channelIdHashMap.getOrDefault(channel,null);
        if(id != null){
            Player player = game.getPlayer(id);
            player.setOnline(false);
        }
    }
    public void saveWorld(){
        SaveUtil.saveWorld(game.world, "world");
    }
    public World loadWorld(){
        return FetchUtil.fetchWorld("world");
    }
}

