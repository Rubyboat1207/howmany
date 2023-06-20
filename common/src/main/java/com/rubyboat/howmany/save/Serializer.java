package com.rubyboat.howmany.save;

import com.rubyboat.howmany.gui.HowManyGUI;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Serializer {
    private static final String saveFolder = "./config/howmany/";
    private static final String saveFile = saveFolder + "config.txt";

    public static void Save() throws IOException {
        var filePath = Path.of(saveFile);
        if(!Files.exists(filePath)) {
            Files.createDirectories(Path.of(saveFolder));
            Files.createFile(filePath);
        }
        StringBuilder builder = new StringBuilder();

        for(var item : HowManyGUI.trackedItems) {
            builder.append(item.toString()).append("\n");
        }

        for(var script : HowManyGUI.scripts) {
            builder.append(script).append("\n");
        }

        Files.writeString(filePath, builder);
    }

    public static void Load() throws IOException {
        HowManyGUI.scripts.clear();
        HowManyGUI.trackedItems.clear();

        var filePath = Path.of(saveFile);
        if(Files.exists(filePath)) {
            var list = Files.readString(filePath).trim().split("\n");

            for(var line : list) {
                if(line.isEmpty()) {
                    continue;
                }
                if(line.contains("!>")) {
                    HowManyGUI.scripts.add(line);
                    continue;
                }

                var identifier = new Identifier(line);
                if(identifier.getPath().isEmpty()) {
                    continue;
                }
                HowManyGUI.trackedItems.add(identifier);
            }
        }
    }
}
