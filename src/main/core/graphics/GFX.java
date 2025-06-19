package main.core.graphics;

import java.util.HashMap;
import java.util.Map;

import main.core.Engine;
import main.core.graphics.entity.TextureManager;

public class GFX {

    private static final Map<String, String> gfx = new HashMap<>() {{
        put("title_bg", "title_background");
        put("title_logo", "title_logo");
        put("container_base", "container_base");

        put("new_game_button_bg", "new_game_button");
        put("new_game_button_hover_bg", "new_game_button_hover");
        put("new_game_button_click_bg", "new_game_button_click");

        put("load_game_button_bg", "load_game_button");
        put("load_game_button_hover_bg", "load_game_button_hover");
        put("load_game_button_click_bg", "load_game_button_click");

        put("title_settings_button_bg", "title_settings_button");
        put("title_settings_button_hover_bg", "title_settings_button_hover");
        put("title_settings_button_click_bg", "title_settings_button_click");

        put("title_nudge_button_bg", "title_nudge_button");
        put("title_nudge_button_hover_bg", "title_nudge_button_hover");
        put("title_nudge_button_click_bg", "title_nudge_button_click");
    }};

    static {
        for (String graphic : gfx.keySet()) {
            try {
                TextureManager.addTexture(graphic, gfx.get(graphic));
            }
            catch (Exception e) {
                Engine.log(e);
            }
        }
        System.out.println("Graphics Loaded!");
    }
}
