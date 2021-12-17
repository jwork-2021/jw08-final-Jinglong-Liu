package app.server;

import app.base.Direction;
import app.base.request.*;
import app.base.Player;
import app.server.game.Factory;
import app.util.ByteUtil;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Handler{
    public Game game;
    private static int playerNum = 0;
    public void handle(SocketChannel channel,ByteBuffer buffer){
        new RecvHandler(server,channel,buffer).start();
    }
    private Server server;

    /**
     * constructor
     * @param server
     */
    public Handler(Server server){
        this.server = server;
        this.game = new Game(this);
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
            if(o instanceof SimpleRequest){
                String request = ((SimpleRequest) o).getRequest();
                switch (request){
                    case "player1":
                    case "player2":
                        game.registerPlayer(request);
                    default:
                        break;
                }
            }
            else if(o instanceof LoginRequest){
                String id = ((LoginRequest) o).getId();
                server.map.put(id,channel);
                System.out.println("用户 " + id +  " 登录成功");
                //server.queue.offer(buffer);//登录成功信息
                //登录成功，发回player.
                Player player = null;
                if(game.players.containsKey(id)){
                    player = game.players.get(id);
                }
                else{
                    player = Factory.createPlayer(game.world, id);
                }
                //Player player = game.players.getOrDefault(id,Factory.createPlayer(game.world, id));
                game.players.put(id,player);
                try {
                    server.channelQueueHashMap.get(channel).add(ByteUtil.getByteBuffer(player));
                    //server.broadcast(player);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(o instanceof StateRequest){
                try {
                    server.channelQueueHashMap.get(channel).add(ByteUtil.getByteBuffer(game.world));
                    //server.queue.add(Server.MyNode.allocate(ByteUtil.getByteBuffer(game.world),channel));
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
}

