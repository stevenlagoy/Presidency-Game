package main.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import main.core.characters.CharacterManager;
import main.core.characters.Name;
import main.core.demographics.Bloc;
import main.core.demographics.Demographics;
import main.core.demographics.DemographicsManager;
import main.core.graphics.WindowManager;
import main.core.graphics.game.TestGame;
import main.core.map.MapManager;

public class Main
{
    public static WindowManager window;
    public static TestGame game;

    public static void main(String[] args){
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

        Engine.stop();
        System.out.print("Main Done\n");
        System.exit(0);
    }
}