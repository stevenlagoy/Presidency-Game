/*
 * Main.java
 * Steven LaGoy
 * 
 */

package main.core;

import main.core.characters.names.NameManager;
import main.core.demographics.DemographicsManager;

public class Main
{

    public static void main(String[] args){
        boolean active = true;
        try {
            // Engine.language = Engine.Language.EN;
            active = active && Engine.init();
            //if (active) Engine.run();
        }
        catch (Exception e) {
            e.printStackTrace();
            active = false;
        }

        for (int i = 0; i >= 0; i++) {
            IOUtil.stdout.println(NameManager.generateName(DemographicsManager.randomDemographics()).getBiographicalName());
        }

        while (active) {
            try {
                active = Engine.tick();
                if (!active) break;
                Thread.sleep(Engine.getTickSpeed());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                active = false;
            }
        }
        Engine.writeSave();
        Engine.stop();
        System.out.print("Main Done\n");
        System.exit(0);
    }
}