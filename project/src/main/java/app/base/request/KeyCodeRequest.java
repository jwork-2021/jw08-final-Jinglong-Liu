package app.base.request;

import app.base.Player;
import javafx.scene.input.KeyCode;

public class KeyCodeRequest implements Request{
    private static final long serialVersionUID = 1005L;
    private KeyCode keyCode;
    private String playerId;
    public KeyCodeRequest(String playerId,KeyCode keyCode){
        this.keyCode = keyCode;
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }

    @Override
    public int getMask() {
        return Request_KeyCode;
    }
}
