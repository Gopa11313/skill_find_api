package com.gopal.skillfind.skill_find_api.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderCreation {
    public static void createFolder(String folderPath) throws Exception {
        Path path = Paths.get(folderPath);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        } else {
            throw new Exception("Folder already exists.");
        }
    }
}
