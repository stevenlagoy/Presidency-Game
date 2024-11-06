import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {
    private Stage stage;

    public SceneController(Stage stage){
        this.stage = stage;
    }

    public void switchScene(Scene scene){
        stage.setScene(scene);
        stage.setMaximized(true);
    }
}