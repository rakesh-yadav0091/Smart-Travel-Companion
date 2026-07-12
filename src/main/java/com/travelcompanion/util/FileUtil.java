package com.travelcompanion.util;

import java.io.IOException;
import java.nio.file.*;

public class FileUtil {

    private static final String DATA_DIR = "./data/";

    public static void ensureDataDirectoryExists() {
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
                System.out.println("Data directory created: " + DATA_DIR);
            }
        } catch (IOException error) {
            System.err.println("Failed to create data directory: " + error.getMessage());
        }
    }

    public static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException error) {
            System.err.println("Failed to read file: " + error.getMessage());
            return "";
        }
    }

    public static void writeFile(String filePath, String content) {
        try {
            Files.write(Paths.get(filePath), content.getBytes());
            System.out.println("File written: " + filePath);
        } catch (IOException error) {
            System.err.println("Failed to write file: " + error.getMessage());
        }
    }

    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    public static boolean deleteFile(String filePath) {
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException error) {
            System.err.println("Failed to delete file: " + error.getMessage());
            return false;
        }
    }
}
