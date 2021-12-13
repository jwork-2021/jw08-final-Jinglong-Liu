package app.server.game;

import app.base.Player;
import app.base.World;

public class Factory {
    public static Player createPlayer(World world,String id){
        return new Player(world,id,25,25);
    }
}
