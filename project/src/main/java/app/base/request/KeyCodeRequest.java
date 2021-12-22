package app.base.request;

import app.base.Player;
import javafx.scene.input.KeyCode;

public class KeyCodeRequest implements SendAble{
    private static final long serialVersionUID = 111112L;
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
        return 1166;
    }
}
