package app.client.ui.screen;

import javafx.scene.Scene;

public class LoseScreen extends RestartScreen{
    public LoseScreen(){
        super();
    }

    @Override
    public Scene restartScene() {
        return super.restartScene("你输啦\n\n勿以输小而不麻");
    }
}
