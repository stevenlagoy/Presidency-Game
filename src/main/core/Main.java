/*
 * Main.java
 * Steven LaGoy
 * 
 */

package main.core;

import main.core.graphics.*;
import main.core.graphics.game.*;

import java.util.ArrayList;
import java.util.List;

import main.core.characters.Character;
import main.core.characters.CharacterManager;

public class Main
{
    public static WindowManager window;
    public static TestGame game;

    public static void main(String[] args){

        List<String> names = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            try {
                Character character = new Character();
                IOUtil.stdout.println(character.getName().getBiographicalName());
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        CharacterManager.generateBlocsReport();

        boolean active = false;
        try {
            // Engine.language = Engine.Language.EN;
            active = active && Engine.init();
            if (active) Engine.run();
        }
        catch (Exception e) {
            e.printStackTrace();
            active = false;
        }

        while (active) {
            try {
                active = Engine.tick();
                if (!active) break;
                Thread.sleep(Engine.getTickSpeed());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

        // Engine.writeSave();
        // Engine.stop();
        System.out.print("Main Done\n");
        System.exit(0);
    }
}