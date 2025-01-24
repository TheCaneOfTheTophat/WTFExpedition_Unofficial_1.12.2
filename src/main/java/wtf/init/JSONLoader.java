package wtf.init;

import com.google.gson.*;
import wtf.WTFExpedition;
import wtf.config.BlockEntry;
import wtf.config.OreEntry;

import java.io.*;
import java.nio.file.Files;
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
            oreJsons = Files.walk(Paths.get(WTFExpedition.configDirectory, "ores"), 4).filter(Files::isRegularFile).filter(path -> path.getFileName().toString().endsWith(".json")).collect(Collectors.toList());
            blockJsons =  Files.walk(Paths.get(WTFExpedition.configDirectory, "blocks"),4).filter(Files::isRegularFile).filter(path-> path.getFileName().toString().endsWith(".json")).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
        JsonParser parser = new JsonParser();
        JsonElement root;

        for(Path jsonPath : oreJsons) {
            OreEntry entry;

            try {
                root = parser.parse(new BufferedReader(new FileReader(jsonPath.toString())));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Path \"" + jsonPath + "\" of ore definition JSON file somehow does not exist or cannot be read", e);
            } catch (JsonSyntaxException e) {
                throw new RuntimeException("Incorrect JSON syntax in JSON file at path: " + jsonPath, e);
            } catch (JsonParseException e) {
                throw new RuntimeException("Error parsing JSON at path: " + jsonPath, e);
            }

            if(root.isJsonArray()) {
                for (JsonElement element : (JsonArray) root) {
                    entry = gson.fromJson(element.toString(), OreEntry.class);
                    oreEntries.add(entry);
                }
            } else {
                entry = gson.fromJson(root.toString(), OreEntry.class);
                oreEntries.add(entry);
            }
        }

        for(Path jsonPath : blockJsons) {
            BlockEntry entry;

            try {
                root = parser.parse(new BufferedReader(new FileReader(jsonPath.toString())));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Path \"" + jsonPath + "\" of block definition JSON file somehow does not exist or cannot be read", e);
            } catch (JsonSyntaxException e) {
                throw new RuntimeException("Incorrect JSON syntax in JSON file at path: " + jsonPath, e);
            }  catch (JsonParseException e) {
                throw new RuntimeException("Error parsing JSON at path: " + jsonPath, e);
            }

            if(root.isJsonArray()) {
                for (JsonElement element : (JsonArray) root) {
                    entry = gson.fromJson(element.toString(), BlockEntry.class);
                    blockEntries.add(entry);
                    identifierToBlockEntry.put(entry.getName(), entry);
                    identifierToBlockEntry.put(entry.getBlockId().contains("@") ? entry.getBlockId() : entry.getBlockId() + "@0", entry);
                }
            } else {
                entry = gson.fromJson(root.toString(), BlockEntry.class);
                blockEntries.add(entry);
                identifierToBlockEntry.put(entry.getName(), entry);
                identifierToBlockEntry.put(entry.getBlockId().contains("@") ? entry.getBlockId() : entry.getBlockId() + "@0", entry);
            }
        }
    }
}
