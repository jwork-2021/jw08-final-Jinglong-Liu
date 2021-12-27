package app.server;

import app.base.NPTank;
import app.base.World;
import app.server.game.Factory;
import app.server.ui.ServerScreen;
import app.util.SaveUtil;
import app.util.UIHelper;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class Main{
    private ServerScreen screen = new ServerScreen();
    Main(Stage stage){
        server = new Server(8090);
        game = new Game(new World(),2);
        handler = new Handler(server,game);
        stage.setScene(screen.scene());
        stage.setOnCloseRequest((e)->{
            System.out.println("服务器已断开");
            disconnectAction();
            System.exit(0);
        });
        stage.show();
    }
    public void start(){
        registerButton();
    }
    private void registerButton(){
        screen.listenButton.setOnAction((e)->{
            screen.listenButton.setDisable(restart());
        });
        screen.resetButton.setOnAction((e)->{
            game.setLimit((int)screen.limit.getValue());
            UIHelper.prompt("提示","修改人数成功");
        });

        screen.loadButton.setOnAction((e)->{
            String text = screen.loadButton.getText();
            int count = game.getWorld().onlinePlayerCount();
            if(count > 0){
                UIHelper.prompt("提示","游戏正在进行，不可更改地图");
                return;
            }
            if(text.equals("加载地图")){
                game.setWorld(Factory.loadWorld("world"));
                UIHelper.prompt("提示","地图已加载");
                screen.loadButton.setText("重置地图");
            }
            else{
                screen.loadButton.setText("加载地图");
                game.setWorld(new World());
                UIHelper.prompt("提示","地图已重置");
            }
        });
        screen.saveButton.setOnAction((e)->{
            SaveUtil.saveWorld(getWorld(),"world");
            UIHelper.prompt("提示","保存地图成功");
        });
        screen.npcButton.setOnAction((e)->{
            if(screen.npcButton.getText().equals("开始加入npc")) {
                npcThread = new AddNPCThread();
                npcThread.start();
                screen.npcButton.setText("停止加入npc");
            }
            else{
                npcThread.interrupt();
                screen.npcButton.setText("开始加入npc");
            }
        });
    }
    private Server server;
    private Game game;
    private Handler handler;
    private World getWorld(){
        return game.getWorld();
    }
    private boolean restart(){
        int number = 2;
        int port = 8090;
        try {
            number = (int)screen.limit.getValue();
            port = Integer.parseInt(screen.port.getText());
            server.setPort(port);
            game.setLimit(number);
        }
        catch (Exception e){
            UIHelper.prompt("提醒","请输入合法的端口号");
            return false;
        }
        server.start();
        return true;
    }
    private void disconnectAction(){
        game.saveWorld();
    }
    private class AddNPCThread extends Thread{
        @Override
        public void run() {
            while(!Thread.currentThread().interrupted()){
                try {
                    TimeUnit.MILLISECONDS.sleep(5000);
                }
                catch (InterruptedException e){
                    break;
                }
                if(game.getWorld().countThing(NPTank.class) >= 3){
                    continue;
                }
                else{
                    game.addNPC();
                }
            }
        }
    }
    private AddNPCThread npcThread = new AddNPCThread();
}
