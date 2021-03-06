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
        System.out.println("θΏζ₯ζε");
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
                System.out.println("η»ε½ζε");
                break;
            case LOGIN_LIMIT:
                UIHelper.prompt("ζη€Ί", "ε½εζΈΈζε·²ζ»‘εοΌθ―·η¨εεζ₯");
                break;
            case LOGIN_ALREADY:
                UIHelper.prompt("ζη€Ί", id + "ε·²η»η»ε½οΌθ―·δΈθ¦ιε€η»ε½γ");
                break;
            case LOGIN_LOSE:
                UIHelper.prompt("δ½ θΎε¦", "δ½ ε·²η»εΊε±γε½εζΈΈζε°ζͺη»ζοΌθ―·η¨εεζ₯γ");
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
        if(game.state == State.PLAY && game.getStage()!=null){
            UIHelper.prompt("ζ­ηΊΏ", "ζε‘ε¨ε·²ζ­εΌοΌθ―·ιεΊιη»");
        }
    }
}
