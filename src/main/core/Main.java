package main.core;

import main.core.characters.CharacterManager;
import main.core.characters.Name;
import main.core.demographics.Bloc;
import main.core.demographics.Demographics;
import main.core.demographics.DemographicsManager;
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
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Demographics demographics = DemographicsManager.generateDemographics();
            Name name = CharacterManager.generateName(demographics);
            main.core.characters.Character character = new main.core.characters.Character(name, demographics);
            //name = CharacterManager.generateName(d1);
            System.out.println(character.getName().toString());
            System.out.println(character.getDemographics().toRepr());
            System.out.print("\n\n\n\n\n\n");
            //name = CharacterManager.generateName(d2);
            //System.out.println(name.toString());
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
