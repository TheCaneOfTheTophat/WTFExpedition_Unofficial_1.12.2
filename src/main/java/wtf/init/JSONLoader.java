package wtf.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import wtf.Core;
import wtf.config.BlockEntry;
import wtf.config.OreEntry;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JSONLoader {
    public static ArrayList<OreEntry> oreEntries = new ArrayList<>();
    public static ArrayList<BlockEntry> blockEntries = new ArrayList<>();

    public static void loadJsonContent(FMLPreInitializationEvent event) throws IOException {
        List<Path> oreJsons = Files.walk(Paths.get(event.getModConfigurationDirectory().toString(), "WTF-Expedition", "ores"), 1).filter(Files::isRegularFile).filter(path -> path.getFileName().toString().endsWith(".json")).collect(Collectors.toList());
        // List<Path> blockJsons =  Files.walk(Paths.get(event.getModConfigurationDirectory().toString(), "WTF-Expedition", "blocks"),1).filter(Files::isRegularFile).filter(path-> path.getFileName().toString().endsWith(".json")).collect(Collectors.toList());

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        for(Path jsonPath : oreJsons) {
            OreEntry entry = gson.fromJson(new FileReader(jsonPath.toString()), OreEntry.class);
            oreEntries.add(entry);
        }

//        for(Path jsonPath : blockJsons) {
//            BlockEntry entry = gson.fromJson(new FileReader(jsonPath.toString()), BlockEntry.class);
//            blockEntries.add(entry);
//        }
    }
}
