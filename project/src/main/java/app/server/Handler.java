package app.server;

import app.base.request.KeyCodeRequest;
import app.base.request.LoginRequest;
import app.base.Player;
import app.base.request.SendAble;
import app.base.request.SimpleRequest;
import app.server.game.Factory;
import app.util.ByteUtil;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Handler{
    public Game game;
    private static int playerNum = 0;
    public void handle(SocketChannel channel,ByteBuffer buffer){
        new RecvHandler(server,channel,buffer).run();
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


    private class RecvHandler{
        private SocketChannel channel;
        private ByteBuffer byteBuffer;
        private Server server;
        RecvHandler(Server server,SocketChannel channel,ByteBuffer buffer){
            this.server = server;
            this.byteBuffer = buffer;
            this.channel = channel;
        }
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
                Player player = Factory.createPlayer(id);//create player
                game.world.add(player);
                game.players.put(id,player);
                try {
                    server.queue.offer(ByteUtil.getByteBuffer(player));
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
                        player.moveBy(0,-5);
                        break;
                    case S:
                        player.moveBy(0,5);
                        break;
                    case A:
                        player.moveBy(-5,0);
                        break;
                    case D:
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

    public void broadcast(SendAble o){
        try {
            server.queue.offer(ByteUtil.getByteBuffer(o));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized void send(SendAble o,SocketChannel channel){

    }
}

