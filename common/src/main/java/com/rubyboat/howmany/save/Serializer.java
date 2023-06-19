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

        Files.writeString(filePath, builder);
    }

    public static void Load() throws IOException {
        var filePath = Path.of(saveFile);
        if(Files.exists(filePath)) {
            var list = Files.readString(filePath).trim().split("\n");

            for(var identifier : list) {
                if(identifier.isEmpty()) {
                    continue;
                }
                var id = new Identifier(identifier);
                if(id.getPath().isEmpty()) {
                    continue;
                }
                HowManyGUI.trackedItems.add(id);
            }
        }
    }
}
