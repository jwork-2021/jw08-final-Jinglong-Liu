package app.client.screen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class LoseScreen extends RestartScreen{
    public LoseScreen(){
        super();
    }

    @Override
    public Scene restartScene() {
        return super.restartScene("你输啦\n\n勿以输小而不麻");
    }
}
