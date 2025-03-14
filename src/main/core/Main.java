package main.core;

import main.core.characters.CharacterManager;
import main.core.graphics.WindowManager;
import main.core.graphics.game.TestGame;
import main.core.graphics.utils.Consts;

public class Main
{
    public static WindowManager window;
    public static TestGame game;

    public static void main(String[] args){

        // window = new WindowManager(Consts.TITLE, 0, 0, false);
        // game = new TestGame();

        boolean active;
        try {
            Engine.language = Engine.Language.EN;
            Engine.init();
            Engine.run();
            active = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            active = false;
        }

        

        while (active) {
            try {
                active = Engine.tick();
                if (!active) break;
                Thread.sleep(Engine.tickSpeed);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

        System.out.print("Main Done\n");
        Engine.stop();
    }
}
