package com.rubyboat.howmany.save;

import com.google.gson.*;
import com.rubyboat.howmany.entry.TrackedEntry;
import com.rubyboat.howmany.entry.TrackedItemEntry;
import com.rubyboat.howmany.gui.HowManyGUI;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Serializer {
    public static HashMap<String, Class<? extends TrackedEntry>> typeToEntry = new HashMap<>();
    private static final String saveFolder = "./config/howmany/";
    private static final String saveFile = saveFolder + "config.json";

    public static void Save() throws IOException {
        var filePath = Path.of(saveFile);
        if(!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
        }

        var arr = new JsonArray();
        for(var item : HowManyGUI.trackedItems) {
            arr.add(item.toJSON());
        }

        Files.writeString(filePath, new GsonBuilder().setPrettyPrinting().create().toJson(arr));
    }

    public static void Load() throws IOException {
        HowManyGUI.trackedItems.clear();

        var filePath = Path.of(saveFile);
        var gson = new Gson();
        if(Files.exists(filePath)) {
            var arr = gson.fromJson(Files.readString(filePath), JsonArray.class);

            for(var line : arr) {
                var entryType = typeToEntry.get(line.getAsJsonObject().get("type").getAsString());
                try {
                    var constructor = entryType.getConstructor(JsonObject.class);

                    var gsonObject = line.getAsJsonObject();

                    TrackedEntry entry = constructor.newInstance(gsonObject);

                    HowManyGUI.trackedItems.add(entry);
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

