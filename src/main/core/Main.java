package main.core;

import main.core.characters.CharacterManager;
import main.core.characters.Name;
import main.core.demographics.Bloc;
import main.core.demographics.Demographics;
import main.core.graphics.WindowManager;
import main.core.graphics.game.TestGame;

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

        Demographics d1 = new Demographics(Bloc.matchBlocName("Generation X"), Bloc.matchBlocName("Evangelical"), Bloc.matchBlocName("White"), Bloc.matchBlocName("Man"));
        Demographics d2 = new Demographics(Bloc.matchBlocName("Generation X"), Bloc.matchBlocName("Evangelical"), Bloc.matchBlocName("White"), Bloc.matchBlocName("Woman"));
        Name name;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            name = CharacterManager.generateName(d1);
            System.out.println(name.toString());
            name = CharacterManager.generateName(d1);
            System.out.println(name.toString());
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
