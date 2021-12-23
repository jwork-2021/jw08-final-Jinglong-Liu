package app.client;

import app.base.*;
import app.base.request.*;
import app.util.ThreadPoolUtil;
import app.util.UIHelper;
import app.util.ByteUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class Handler {
    private app.base.request.StateRequest srq = new app.base.request.StateRequest();
    private Game game;
    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Handler(Game game){
        this.game = game;
    }
    public void handle(ByteBuffer buffer){
        //new RecvHandler(buffer).start();
        ThreadPoolUtil.execute(new RecvHandler(buffer));
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
        @Override
        public void run() {
            try {
                o = (SendAble) ByteUtil.getObject(byteBuffer);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

            try {
                System.out.println(ByteUtil.getBytes(o).length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(o instanceof Player){
                String id = ((Player) o).getPlayerId();
                if(id.equals(game.playerId)){
                    game.play();
                    game.setPlayer((Player) o);
                    new Handler.StateRequest().start();
                }
            }

            else if(o instanceof GameResult){
                String state = ((GameResult) o).get(game.playerId);
                if("win".equals(state)){
                    game.win();
                }
                else if("lose".equals(state)){
                    game.lose();
                }
            }
            else if(o instanceof World){
                //System.out.println(((World) o).getPlayers().size());
                //game.getWorld().setThings(((World) o).getThings());
                //((World) o).render(game.getGraphicsContext());
                //game.getWorld().setPlayers(((World) o).getPlayers());
                game.player = ((World) o).getPlayer(game.playerId);
                game.getWorld().setWorld((World) o);
                if(game.player.getHp() <= 0){
                    System.out.println("你输啦.");
                    game.lose();
                    try {
                        client.sc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //System.out.println("bullets " + ((World) o).countThing(Bullet.class));
                //System.out.println("npc: " + ((World) o).countThing(NPTank.class));

            }

            else if(o instanceof LoginFailResponse){
                String type = ((LoginFailResponse) o).type();
                String id = ((LoginFailResponse) o).getId();
                if(type.equals("already")){
                    UIHelper.prompt("提示",id + "已经登录，请不要重复登录。");
                }
                else if(type.equals("limit")){
                    UIHelper.prompt("提示","当前游戏已满员，请稍后再来");
                }
                else if(type.equals("fail")){
                    UIHelper.prompt("你输啦","你已经出局。当前游戏尚未结束，请稍后再来。");
                }
            }
            else if(o instanceof MessageResponse){
                String message = ((MessageResponse) o).getMessage();
                game.addMessage(message);
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
