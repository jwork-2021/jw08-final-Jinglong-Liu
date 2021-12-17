package app.client;

import app.base.*;
import app.base.request.KeyCodeRequest;
import app.base.request.SendAble;
import app.base.request.StateRequest;
import app.util.ByteUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class Handler {
    private app.base.request.StateRequest srq = new app.base.request.StateRequest();
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
    public void handle(Object o){
        new RecvHandler(o).start();
    }

    public void connect(){
        System.out.println("连接成功");
        game.connectSucceed();
    }
    private class RecvHandler extends Thread{
        private ByteBuffer byteBuffer;
        private SendAble o;
        RecvHandler(ByteBuffer buffer){
            this.byteBuffer = buffer;
        }
        RecvHandler(Object o){
            this.o = (SendAble) o;
        }
        @Override
        public void run() {
            if(o == null){
                try {
                    o = (SendAble) ByteUtil.getObject(byteBuffer);
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }

            if(o instanceof World){
                game.getWorld().setThings(((World) o).getThings());
                //System.out.println(game.getWorld().things().size());
                game.player = ((World) o).getPlayer(game.playerId);
                if(game.player == null){
                    System.out.println("你输啦.");
                    try {
                        client.sc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(o instanceof Player){
                String id = ((Player) o).getPlayerId();
                if(id.equals(game.playerId)){
                    game.play();
                    game.setPlayer((Player) o);
                    new Handler.StateRequest().start();
                }
                else{
                    System.out.println("玩家 " + id + " 登录成功");
                }
            }
        }
    }

    private class StateRequest extends Thread{
        @Override
        public void run() {
            while(game.state == app.client.State.PLAY){
                try{
                    client.send(srq);
                    TimeUnit.MILLISECONDS.sleep(60);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
