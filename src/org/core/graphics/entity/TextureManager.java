package src.org.core.graphics.entity;

import java.util.HashMap;
import java.util.Map;

import src.org.core.graphics.ObjectLoader;

public class TextureManager {
    
    public static final String texturesLoc = "textures/";
    public static final String pngExtension = ".png";
    public static ObjectLoader loader = new ObjectLoader();

    private static final Map<String, String> modelFilesList = new HashMap<String, String>() {{
        put("green", "green");
        put("red", "red");
        put("white", "white");
        put("black", "black");
        put("yellow", "yellow");
        put("purple", "purple");
        put("orange", "orange");
        put("flowers", "flowers");
        put("blue", "blue");
        put("dirt", "dirt");
        put("obamaface", "obamaface");
        put("stone", "stone");
        put("grass", "terrain");
    }};
    private static Map<String, Texture> textures = new HashMap<String, Texture>();

    public static void loadTextureFiles() throws Exception {
        for (Map.Entry<String, String> entry : modelFilesList.entrySet()) {
            textures.put(entry.getKey(), new Texture(loader.loadTexture(texturesLoc + entry.getValue() + pngExtension)));
        }
    }

    public static Map<String, String> getTextureFilesList () {
        return modelFilesList;
    }

    public static void addTexture(String textureName, String textureFile) throws Exception {
        modelFilesList.put(textureName, textureFile);
        loadTextureFiles();
    }

    public static Texture getTexture(String textureName) {
        return textures.get(textureName);
    }
}
