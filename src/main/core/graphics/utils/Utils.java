package main.core.graphics.utils;

import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.lwjgl.system.MemoryUtil;

import main.core.IOUtil;
import main.core.Main;

public class Utils {
    
    public static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static String loadResource(Path path) throws Exception {
        Scanner scanner = IOUtil.createScanner(path.toFile());
        return scanner.useDelimiter("\\A").next();
    }

    public static String loadResource(String filename) throws Exception {
        Scanner scanner = IOUtil.createScanner(new File(filename));
        return scanner.useDelimiter("\\A").next();
    }

    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> lines = new ArrayList<>();
        try (InputStream in = Utils.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in == null)
                throw new Exception("Resource not found: " + fileName); 
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Class.forName(Main.class.getName()).getResourceAsStream(fileName)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
        }
        return lines;
    }
}
