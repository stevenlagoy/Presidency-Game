package main.core;

import java.util.Date;

import main.core.characters.*;
import main.core.demographics.*;
import main.core.graphics.*;
import main.core.graphics.game.*;
import main.core.map.*;

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

        Name name = new Name("Steven", "Michael", "LaGoy");
        Demographics demographics = new Demographics("Generation Z", "Christian", "White", "Other / Non-Binary");
        Engine.playerCandidate = new Candidate(name, demographics);
        Engine.playerCandidate.setBirthplaceCity(MapManager.matchCity("New York", "NY"));
        Engine.playerCandidate.setCurrentLocationCity(MapManager.matchCity("New York", "NY"));
        Engine.playerCandidate.setResidenceCity(MapManager.matchCity("New York", "NY"));
        Engine.playerCandidate.setBirthday(new Date(1093838400000L));
        Engine.playerCandidate.setAppearance(new CharacterModel());
        Engine.playerCandidate.setPersonality(new Personality());

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

        Engine.writeSave();
        Engine.stop();
        System.out.print("Main Done\n");
        System.exit(0);
    }
}