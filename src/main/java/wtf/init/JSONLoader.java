package wtf.init;

import com.google.gson.*;
import wtf.WTFExpedition;
import wtf.config.BlockEntry;
import wtf.config.OreEntry;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JSONLoader {
    public static ArrayList<OreEntry> oreEntries = new ArrayList<>();
    public static ArrayList<BlockEntry> blockEntries = new ArrayList<>();
    public static Map<String, BlockEntry> identifierToBlockEntry = new HashMap<>();
    public static Path defaultsDirectory;

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
    public static void loadJsonContent() {
        Path oreDirectory = Paths.get(WTFExpedition.configDirectoryString, "ores"); // Get and/or make config directory for ores
        oreDirectory.toFile().mkdir();


        Path blockDirectory = Paths.get(WTFExpedition.configDirectoryString, "blocks"); // Get and/or make config directory for blocks
        blockDirectory.toFile().mkdir();

        final String guideFilename = "Configuration_Guide.txt";

        List<Path> allJsons = new ArrayList<>();
        List<Path> defaultFiles;
        FileSystem fileSystem = null;
        boolean fromJar = false;

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonParser parser = new JsonParser();
        JsonElement root;

        try {
            URL classUrl = WTFExpedition.class.getProtectionDomain().getCodeSource().getLocation(); // Get URL of this class

            if (classUrl.getProtocol().equals("jar")) { // Use jar as filesystem if it's loaded as a jar file
                fileSystem = FileSystems.newFileSystem(URI.create(classUrl.toString()), Collections.emptyMap());
                fromJar = true;
                defaultsDirectory = fileSystem.getPath("assets", WTFExpedition.modID, "defaults");
            } else { // Remain with original filesystem
                fileSystem = FileSystems.getDefault();
                Path pathFromUri = Paths.get(classUrl.toURI());
                defaultsDirectory = Paths.get(pathFromUri.getRoot().toString(), pathFromUri.subpath(0, pathFromUri.getNameCount() - 2).toString(), "assets", WTFExpedition.modID, "defaults");
            }

            // Get default files
            try (Stream<Path> defaults = Files.walk(fileSystem.getPath(defaultsDirectory.toString()))) {
                defaultFiles = defaults.filter(Files::isRegularFile).collect(Collectors.toList());

                // Copy over default files
                for (Path file : defaultFiles) {
                    String filename = fileSystem.getPath(file.getName(file.getNameCount() - 2).toString(), file.getFileName().toString()).toString();
                    Path configEquivalent = Paths.get(WTFExpedition.configDirectoryString, file.getFileName().toString().equals(guideFilename) ? guideFilename : filename);

                    if (Files.notExists(configEquivalent)) {
                        if (fromJar) {
                            try (Reader reader = new BufferedReader(new InputStreamReader(WTFExpedition.class.getClassLoader().getResourceAsStream(file.toString().substring(1)))); Writer writer = new BufferedWriter(new FileWriter(configEquivalent.toString()))) {
                                int data;

                                while ((data = reader.read()) != -1)
                                    writer.write(data);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try (Reader reader = new BufferedReader(new FileReader(file.toString())); Writer writer = new BufferedWriter(new FileWriter(configEquivalent.toString()))) {
                                int data;

                                while ((data = reader.read()) != -1)
                                    writer.write(data);
                            }
                        }
                    }
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            if(fromJar && fileSystem != null) {
                try { // Close JAR filesystem
                    fileSystem.close();
                } catch (IOException ignored) {}
            }
        }

        // Get all JSONs in config
        try (Stream<Path> orePath = Files.walk(oreDirectory); Stream<Path> blockPath = Files.walk(blockDirectory)) {
            allJsons.addAll(orePath.filter(Files::isRegularFile).filter(path -> path.getFileName().toString().endsWith(".json")).collect(Collectors.toList()));
            allJsons.addAll(blockPath.filter(Files::isRegularFile).filter(path-> path.getFileName().toString().endsWith(".json")).collect(Collectors.toList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Parse JSONs
        for(Path jsonPath : allJsons) {
            OreEntry entryOre;
            BlockEntry entryBlock;

            try (BufferedReader reader = new BufferedReader(new FileReader(jsonPath.toString()))) {
                root = parser.parse(reader);
            } catch (IOException e) {
                throw new RuntimeException("Path \"" + jsonPath + "\" of JSON file somehow does not exist or cannot be read", e);
            } catch (JsonSyntaxException e) {
                throw new RuntimeException("Incorrect JSON syntax in JSON file at path: " + jsonPath, e);
            } catch (JsonParseException e) {
                throw new RuntimeException("Error parsing JSON at path: " + jsonPath, e);
            }

            String parent = jsonPath.getParent().subpath(WTFExpedition.configDirectory.getNameCount(), jsonPath.getParent().getNameCount()).getName(0).toString();

            WTFExpedition.wtfLog.fatal(jsonPath);
            WTFExpedition.wtfLog.fatal(parent);

            for (JsonElement element : (JsonArray) root) {
                if(parent.contains("ores")) {
                    entryOre = gson.fromJson(element.toString(), OreEntry.class);
                    oreEntries.add(entryOre);
                } else {
                    entryBlock = gson.fromJson(element.toString(), BlockEntry.class);
                    blockEntries.add(entryBlock);
                    identifierToBlockEntry.put(entryBlock.getName(), entryBlock);
                    identifierToBlockEntry.put(entryBlock.getBlockId().contains("@") ? entryBlock.getBlockId() : entryBlock.getBlockId() + "@0", entryBlock);
                }
            }
        }
    }
}
