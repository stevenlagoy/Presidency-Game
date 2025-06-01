/*
 * IOUtil.java
 * Steven LaGoy
 * Imported as FileOperations.java from json-java-objectifier https://github.com/stevenlagoy/json-java-objectifier.git
 * Combined/Refactored from ScannerUtil from Engine.java
 * Created: 16 May 2025 at 12:15 AM
 * Modified: 01 June 2025
 */

package main.core;

// IMPORTS ----------------------------------------------------------------------------------------

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * IOUtil provides methods and constants for Input and Output interactions with Files, Consoles, etc.
 * <p>
 * This class is final and has no instance variables, and is not designed to be instantiated.
 */
public class IOUtil {

    /** This class is non-instantiable. All values and functions should be accessed in a static way. */
    private IOUtil() {} // Non-Instantiable

    // FILE EXTENSIONS ----------------------------------------------------------------------------

    public static enum Extension {
        
        ALL (""),
        HTML (".html"),
        JSON (".json"),
        JAVA (".java"),
        OUT (".out"),
        IN (".in"),
        TEXT (".txt");

        public final String extension;

        Extension(String extension) {
            this.extension = extension != null ? extension : "";
        }
    }

    // SCANNERS AND WRITERS -----------------------------------------------------------------------

    /** Standard input from System.in. @see IOUtil#createScanner(InputStream) */
    public static final Scanner stdin = IOUtil.createScanner(System.in);
    /** Standard output to System.out. @see IOUtil#createWriter(OutputStream) */
    public static final PrintWriter stdout = IOUtil.createWriter(System.out);
    /** Standard output to System.err. @see IOUtil#createWriter(OutputStream) */
    public static final PrintWriter stderr = IOUtil.createWriter(System.err);

    public static Scanner createScanner(InputStream inputStream) {
        return new Scanner(inputStream, StandardCharsets.UTF_8.name());
    }
    public static Scanner createScanner(File file) throws FileNotFoundException {
        return new Scanner(file, StandardCharsets.UTF_8.name());
    }
    public static PrintWriter createWriter(OutputStream outputStream) {
        return new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);
    }
    public static PrintWriter createWriter(File file) throws IOException {
        return new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8), true);
    }
    public static PrintWriter createWriter(File file, boolean append) throws IOException {
        return new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8, append), true);
    }
    public static void closeQuietly(Closeable c) {
        if (c != null) try { c.close(); } catch (IOException ignored) {}
    }

    // FILE AND DIRECTORY OPERATIONS --------------------------------------------------------------

    /**
     * Returns a Set of Paths for all the files in the specified directory.
     * <p>
     * Equivalent to {@link IOUtil#listFiles(Path, FileExtension) listFiles(dir, FileExtension.ALL)}
     *
     * @param dir
     *            The path to the directory to list the files within
     *
     * @return A Set of Paths to each file within the directory
     *
     * @throws IOException
     *             If the directory path is invalid or unable to be located
     *
     * @see FileExtension#ALL
     */
    public static Set<Path> listFiles(Path dir) throws IOException {
        try {
            Set<Path> pathSet = listFiles(dir, Extension.ALL);
            return pathSet;
        }
        catch (IOException e) {
            throw e;
        }
    }

    /**
     * Returns a Set of Paths for all the files in the specificed directory with the given extension.
     *
     * @param dir
     *            The path to the directory to list the files within.
     * @param extension
     *            A FileOperations.FileExtension to filter the Path results by.
     *
     * @return A Set of Paths to each file within the directory with the extension.
     *
     * @throws IOException
     *             If the directory path is invalid or unable to be located.
     */
    public static Set<Path> listFiles(Path dir, Extension extension) throws IOException {
        if (dir == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        Set<Path> pathSet = new HashSet<>();
        dir = dir.normalize();
        if (!Files.exists(dir)) {
            throw new IOException("The specified path, " + dir.toString() + ", was not found.");
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if (path == null || path.getFileName() == null) {
                    continue; // Skip null paths
                }
                Path fileName = path.getFileName();
                if (!Files.isDirectory(path) && !FilePaths.IGNORED_PATHS.contains(path)
                        && fileName.endsWith(extension.extension)) {
                    pathSet.add(dir.resolve(fileName));
                }
            }
            return pathSet;
        }
        catch (IOException e) {
            System.err.println("Error accessing directory: " + dir + " - " + e.getMessage());
            throw e;
        }
    }

    /**
     * Empties a directory of all files with the given extension. Searches only the directory itself, not any subdirectories (non-recursive).
     * @param dir The directory to empty.
     * @param extension The extension to target.
     * @throws IOException When the directory is invalid or inaccessable.
     */
    public static void emptyFiles(Path dir, Extension extension) throws IOException {
        Set<Path> paths = listFiles(dir); // does not include ignored files
        for (Path path : paths) {
            // Delete if extension matches or if wildcard
            if (extension.extension.equals("*") || path.toString().endsWith(extension.extension)) {
                try {
                    Files.delete(path);
                }
                catch (IOException e) {
                    System.err.println("Failed to delete file: " + path.toString());
                    throw e;
                }
            }
        }
    }

    /**
     * Reads a file and returns its content lines as a List of Strings.
     * @param path Path to the file to read.
     * @return A List of Strings containing the lines in the file.
     */
    public static List<String> readFile(Path path) {
        try {
            Scanner scanner = IOUtil.createScanner(path.toFile());
            List<String> result = new ArrayList<>();
            while (scanner.hasNextLine()) {
                result.add(scanner.nextLine());
            }
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Writes a String line to a file with the given directory, name, and extension.
     * @param filename String name of the file to create / write to. Should not contain the extension of the file.
     * @param extension Extension of the file.
     * @param destination Path to the Directory which will contain the created / written file.
     * @param content String to write into the file. 
     * @see #writeFile(String, Extension, Path, List)
     */
    public static void writeFile(String filename, Extension extension, Path destination, String content) {
        writeFile(filename, extension, destination, Collections.singletonList(content));
    }

    /**
     * Writes String lines to a file with the given directory, name, and extension.
     * @param filename String name of the file to create / write to. Should not contain the extension of the file.
     * @param extension Extension of the file.
     * @param destination Path to the Directory which will contain the created / written file.
     * @param content List of Strings to write into the file. Each String will be put on a new line.
     * @see #writeFile(File, List)
     */
    public static void writeFile(String filename, Extension extension, Path destination, List<String> content) {
        Path filePath = destination.resolve(filename + extension);
        File file = filePath.toFile();
        writeFile(file, content);
    }

    /**
     * Writes String lines to a file.
     * @param file Valid File to be written into.
     * @param content List of Strings to write into the file. Each String will be put on a new line.
     */
    public static void writeFile(File file, List<String> content) {
        try {
            Files.createDirectories(file.getParentFile().toPath());
            if (!file.createNewFile() && !file.exists()) {
                throw new IOException("Failed to create new file: " + file.getAbsolutePath());
            }
            try (PrintWriter writer = IOUtil.createWriter(file, false)) {
                for (String line : content) {
                    writer.println(line);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
