package app.client;

import app.base.*;
import app.base.request.*;
import app.util.ThreadPoolUtil;
import app.util.UIHelper;
import app.util.ByteUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import static app.base.request.LoginResponse.*;
import static app.base.request.ResultResponse.Result_LOSE;
import static app.base.request.ResultResponse.Result_WIN;

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


    void connect(){
        System.out.println("连接成功");
        game.connectSucceed();
    }
    private class RecvHandler extends Thread{
        private ByteBuffer byteBuffer;
        private Response o;
        RecvHandler(ByteBuffer buffer){
            this.byteBuffer = buffer;
        }
        @Override
        public void run() {
            o = (Response) ByteUtil.getObject(byteBuffer);
            int size = ByteUtil.getBytes(o).length;
            if(size > Client.BUFFER_SIZE/4 * 3){
                System.out.println("dilatation");
                Client.BUFFER_SIZE <<= 1;
            }
            switch (o.getMask()){
                case Response_World: {
                    game.getWorld().setWorld((World) o);
                    break;
                }
                case Response_Login: {
                    handleLoginResponse((LoginResponse) o);
                    break;
                }
                case Response_Result: {
                    handleResultResponse((ResultResponse) o);
                    break;
                }
                default:
                    break;
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
    private void handleLoginResponse(LoginResponse o){
        String id = o.getId();
        if (!id.equals(game.playerId)) {
            return;
        }
        int type = o.getType();
        switch (type) {
            case LOGIN_SUCCEED:
                game.play();
                new Handler.StateRequest().start();
                System.out.println("登陆成功");
                break;
            case LOGIN_LIMIT:
                UIHelper.prompt("提示", "当前游戏已满员，请稍后再来");
                break;
            case LOGIN_ALREADY:
                UIHelper.prompt("提示", id + "已经登录，请不要重复登录。");
                break;
            case LOGIN_LOSE:
                UIHelper.prompt("你输啦", "你已经出局。当前游戏尚未结束，请稍后再来。");
                break;
            default:
                break;
        }
    }

    private void handleResultResponse(ResultResponse o){
        int state = o.getType();
        switch (state){
            case Result_WIN:
                game.win();
                break;
            case Result_LOSE:
                game.lose();
                break;
            default:
                break;
        }
    }
    public void connectionClose(){
        if(game.state == State.PLAY){
            UIHelper.prompt("断线", "服务器已断开，请退出重登");
        }
    }
}
