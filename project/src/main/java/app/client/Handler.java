package app.client;

import app.base.*;
import app.util.ByteUtil;
import javafx.application.Platform;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Handler {
    private Game game;
    private Client client;
    //private Player player;
    public void setClient(Client client) {
        this.client = client;
    }

    public Handler(Game game){
        this.game = game;
    }
    public void handle(ByteBuffer buffer){
        new RecvHandler(buffer).start();
    }
    public void send(){

    }
    public void read(ByteBuffer buffer){

    }
    public void connect(){
        System.out.println("连接成功");
        game.connectSucceed();
    }
    private class RecvHandler extends Thread{
        private ByteBuffer byteBuffer;
        RecvHandler(ByteBuffer buffer){
            this.byteBuffer = buffer;
        }
        @Override
        public void run() {
            SendAble o = null;
            try {
                o = (SendAble) ByteUtil.getObject(byteBuffer);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
            /*
            if(o instanceof LoginRequest){
                String player = ((LoginRequest) o).getId();
                if(player.equals(game.playerId)){
                    game.play();
                }
                else{
                    System.out.println("玩家 " + player + " 登录");
                }
            }*/
            if(o instanceof World){
                //game.getWorld().setThings(((World) o).getThings());
                //System.out.println(game.getWorld().things().size());
            }
            else if(o instanceof Player){
                String id = ((Player) o).getPlayerId();
                if(id.equals(game.playerId)){
                    game.play();
                    game.setPlayer((Player) o);
                }
                else{
                    System.out.println("玩家 " + id + " 登录成功");
                }
            }
        }
    }
}
