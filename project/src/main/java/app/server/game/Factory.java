package app.server.game;

import app.base.Player;

public class Factory {
    public static Player createPlayer(String id){
        return new Player(id,25,25);
    }
}
