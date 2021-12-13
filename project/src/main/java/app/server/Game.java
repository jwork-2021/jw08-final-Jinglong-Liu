package app.server;

import app.base.Player;
import app.base.SendAble;
import app.base.World;

public class Game {
    private Handler handler;
    Player player1;
    Player player2;
    public int state = 0;
    public World world = new World();
    public Game(Handler handler){
        this.handler = handler;
    }
    public void over(){
        player1 = null;
        player2 = null;
    }

    public boolean registerPlayer(String playerId){
        return false;
    }
    private void send(SendAble o){

    }
}
