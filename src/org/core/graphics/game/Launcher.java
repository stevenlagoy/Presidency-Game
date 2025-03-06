package src.org.core.graphics.game;

import src.org.core.graphics.EngineManager;
import src.org.core.graphics.WindowManager;
import src.org.core.graphics.utils.Consts;

public class Launcher {

    private static WindowManager window;
    private static TestGame game;

    public static void main(String[] args) {
        window = new WindowManager(Consts.TITLE, 0, 0, false);
        game = new TestGame();
        EngineManager engine = new EngineManager();

        try {
            engine.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WindowManager getWindow() {
        return window;
    }
    
    public static TestGame getGame() {
        return game;
    }

}
