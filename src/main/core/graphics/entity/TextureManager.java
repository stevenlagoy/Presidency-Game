package main.core.graphics.entity;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import main.core.FilePaths;
import main.core.IOUtil;
import main.core.graphics.ObjectLoader;

public class TextureManager {
    
    private static final Map<String, String> modelFilesList = new HashMap<String, String>();

    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, AnimatedTexture> animatedTextures = new HashMap<>();

    public static void loadTextureFiles() throws Exception {
        for (Map.Entry<String, String> entry : modelFilesList.entrySet()) {
            textures.put(entry.getKey(), new Texture(ObjectLoader.loadTexture(FilePaths.TEXTURES_GFX_LOC.resolve(entry.getValue() + IOUtil.Extension.PNG.extension).toString())));
        }
    }

    public static Map<String, String> getTextureFilesList () {
        return modelFilesList;
    }

    public static void addTexture(String textureName, String textureFile) throws Exception {
        modelFilesList.put(textureName, textureFile);
        loadTextureFiles();
    }
    public static void addAnimatedTexture(String textureName, String textureFile, int frameCount, float frameTime) throws Exception {
        AnimatedTexture texture = ObjectLoader.loadAnimatedTexture(FilePaths.TEXTURES_GFX_LOC.resolve(textureFile), frameCount, frameTime);
        animatedTextures.put(textureName, texture);
    }

    public static Texture getTexture(String textureName) {
        return textures.get(textureName);
    }

    public static AnimatedTexture getAnimatedTexture(String textureName) {
        return animatedTextures.get(textureName);
    }
}
