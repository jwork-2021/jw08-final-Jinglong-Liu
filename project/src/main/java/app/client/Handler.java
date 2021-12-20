package app.client;

import app.base.*;
import app.base.request.*;
import app.client.ui.util.UIHelper;
import app.util.ByteUtil;
import javafx.scene.control.Alert;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
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
            if(o instanceof Player){
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
            else if(o instanceof GameResult){
                String state = ((GameResult) o).get(game.playerId);
                System.out.println(state);
                if("win".equals(state)){
                    game.win();
                }
                else if("lose".equals(state)){
                    game.lose();
                }
            }
            else if(game.state == app.client.State.LOSE || game.state == app.client.State.WIN){
                //return;
            }
            else if(o instanceof World){
                game.getWorld().setThings(((World) o).getThings());
                //game.getWorld().setPlayers(((World) o).getPlayers());
                game.player = ((World) o).getPlayer(game.playerId);

                if(game.player.getHp() <= 0){
                    System.out.println("你输啦.");
                    game.lose();
                    try {
                        client.sc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //else if (o instanceof AlreadyLoginResponse){
            //    String id = ((AlreadyLoginResponse) o).getId();
            //    System.out.println(id + "已经登录，请不要重复登录。");
            //    UIHelper.prompt("提示",id + "已经登录，请不要重复登录。");
            //}
            else if(o instanceof LoginFailResponse){
                String type = ((LoginFailResponse) o).type();
                String id = ((LoginFailResponse) o).getId();
                if(type.equals("already")){
                    UIHelper.prompt("提示",id + "已经登录，请不要重复登录。");
                }
                else{
                    UIHelper.prompt("提示","当前游戏已满员，请稍后再来");
                }
            }
            else if(o instanceof Handler.StateRequest){

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
            game.state = app.client.State.INIT;
        }
    }
}
