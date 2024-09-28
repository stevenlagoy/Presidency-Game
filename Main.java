import java.io.IOException;
// import java.util.Scanner;
import javafx.application.Application;
// import javafx.application.Platform;
// import javafx.stage.Stage;
// import javafx.scene.Scene;
// import javafx.scene.control.*;
// import javafx.scene.layout.*;

public class Main
{
    static Engine engine = new Engine();
    public static void main(String[] args) throws IOException {
        engine.reset();

        Application.launch(MapApplication.class, args);
    }
}