package app.client.ui.screen;

import javafx.scene.Scene;

public class WinScreen extends RestartScreen{
    public WinScreen(){
        super();
    }
    @Override
    public Scene restartScene() {
        return super.restartScene("你赢啦\n\n勿以赢小而不麻");
    }
}
