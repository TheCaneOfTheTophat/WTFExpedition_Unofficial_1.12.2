package wtf.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import wtf.WTFExpedition;
import wtf.config.BlockEntry;
import wtf.config.OreEntry;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JSONLoader {
    public static ArrayList<OreEntry> oreEntries = new ArrayList<>();
    public static ArrayList<BlockEntry> blockEntries = new ArrayList<>();
    public static Map<String, BlockEntry> identifierToBlockEntry = new HashMap<>();

    public static void loadJsonContent() {
        List<Path> oreJsons;
        List<Path> blockJsons;

        try {
            oreJsons = Files.walk(Paths.get(WTFExpedition.configDirectory, "ores"), 1).filter(Files::isRegularFile).filter(path -> path.getFileName().toString().endsWith(".json")).collect(Collectors.toList());
            blockJsons =  Files.walk(Paths.get(WTFExpedition.configDirectory, "blocks"),1).filter(Files::isRegularFile).filter(path-> path.getFileName().toString().endsWith(".json")).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();

        for(Path jsonPath : oreJsons) {
            OreEntry entry = null;

            try {
                entry = gson.fromJson(new FileReader(jsonPath.toString()), OreEntry.class);
            } catch (FileNotFoundException e) {
                WTFExpedition.wtfLog.error("Path \"" + jsonPath + "\" of ore definition JSON file somehow does not exist or cannot be read");
            }

            oreEntries.add(entry);
        }

        for(Path jsonPath : blockJsons) {
            BlockEntry entry = null;

            try {
                entry = gson.fromJson(new FileReader(jsonPath.toString()), BlockEntry.class);
            } catch (FileNotFoundException e) {
                WTFExpedition.wtfLog.error("Path \"" + jsonPath + "\" of block definition JSON file somehow does not exist or cannot be read");
            }

            blockEntries.add(entry);
            identifierToBlockEntry.put(entry.getName(), entry);
            identifierToBlockEntry.put(entry.getBlockId().contains("@") ? entry.getBlockId() : entry.getBlockId() + "@0", entry);
        }
    }
}
