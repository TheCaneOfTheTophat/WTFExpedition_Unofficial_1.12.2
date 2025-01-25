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
        Path oreDirectory = Paths.get(WTFExpedition.configDirectory, "ores"); // Get and/or make config directory for ores
        oreDirectory.toFile().mkdir();


        Path blockDirectory = Paths.get(WTFExpedition.configDirectory, "blocks"); // Get and/or make config directory for blocks
        blockDirectory.toFile().mkdir();

        final String guideFilename = "Configuration_Guide.txt";

        List<Path> allJsons = new ArrayList<>();
        List<Path> defaultJsons;
        Path defaultGuide;
        Path defaultGuideConfigEquivalent = Paths.get(WTFExpedition.configDirectory, guideFilename);
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
                Path pathFromUri = Paths.get(classUrl.toURI());
                fileSystem = FileSystems.getDefault();
                defaultsDirectory = Paths.get(pathFromUri.getRoot().toString(), pathFromUri.subpath(0, pathFromUri.getNameCount() - 2).toString(), "assets", WTFExpedition.modID, "defaults");
            }

            // Get default files
            try (Stream<Path> defaults = Files.walk(fileSystem.getPath(defaultsDirectory.toString()), 2)) {
                defaultJsons = defaults.filter(Files::isRegularFile).filter(path -> path.getFileName().toString().endsWith(".json")).collect(Collectors.toList());
                defaultGuide = fileSystem.getPath(defaultsDirectory.toString(), guideFilename);

                // Copy over default JSONs
                for (Path json : defaultJsons) {
                    String filename = fileSystem.getPath(json.getName(json.getNameCount() - 2).toString(), json.getFileName().toString()).toString();
                    Path jsonConfigEquivalent = Paths.get(WTFExpedition.configDirectory, filename);

                    if (Files.notExists(jsonConfigEquivalent)) {
                        if (fromJar) {
                            try (Reader reader = new BufferedReader(new InputStreamReader(WTFExpedition.class.getClassLoader().getResourceAsStream(json.toString().substring(1)))); Writer writer = new BufferedWriter(new FileWriter(jsonConfigEquivalent.toString()))) {
                                int data;

                                while ((data = reader.read()) != -1)
                                    writer.write(data);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try (Reader reader = new BufferedReader(new FileReader(json.toString())); Writer writer = new BufferedWriter(new FileWriter(jsonConfigEquivalent.toString()))) {
                                int data;

                                while ((data = reader.read()) != -1)
                                    writer.write(data);
                            }
                        }
                    }
                }

                // Copy over readme
                if (Files.notExists(defaultGuideConfigEquivalent)) {
                    if (fromJar) {
                        WTFExpedition.wtfLog.fatal(defaultGuide.toString());
                        WTFExpedition.wtfLog.fatal(WTFExpedition.class.getClassLoader().getResourceAsStream(defaultGuide.toString()));
                        try (Reader reader = new BufferedReader(new InputStreamReader(WTFExpedition.class.getClassLoader().getResourceAsStream(defaultGuide.toString()))); Writer writer = new BufferedWriter(new FileWriter(defaultGuideConfigEquivalent.toString()))) {
                            int data;

                            while ((data = reader.read()) != -1)
                                writer.write(data);
                        }
                    } else {
                        try (Reader reader = new BufferedReader(new FileReader(defaultGuide.toString())); Writer writer = new BufferedWriter(new FileWriter(defaultGuideConfigEquivalent.toString()))) {
                            int data;

                            while ((data = reader.read()) != -1)
                                writer.write(data);
                        }
                    }
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            if(fromJar && fileSystem != null) {
                try { // Put the poor thing to rest
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

            String subfolder = jsonPath.getName(jsonPath.getNameCount() - 2).toString();

            try (BufferedReader reader = new BufferedReader(new FileReader(jsonPath.toString()))) {
                root = parser.parse(reader);
            } catch (IOException e) {
                throw new RuntimeException("Path \"" + jsonPath + "\" of JSON file somehow does not exist or cannot be read", e);
            } catch (JsonSyntaxException e) {
                throw new RuntimeException("Incorrect JSON syntax in JSON file at path: " + jsonPath, e);
            } catch (JsonParseException e) {
                throw new RuntimeException("Error parsing JSON at path: " + jsonPath, e);
            }

            if(root.isJsonArray()) {
                for (JsonElement element : (JsonArray) root) {
                    if(subfolder.equals("ores")) {
                        entryOre = gson.fromJson(element.toString(), OreEntry.class);
                        oreEntries.add(entryOre);
                    } else {
                        entryBlock = gson.fromJson(element.toString(), BlockEntry.class);
                        blockEntries.add(entryBlock);
                        identifierToBlockEntry.put(entryBlock.getName(), entryBlock);
                        identifierToBlockEntry.put(entryBlock.getBlockId().contains("@") ? entryBlock.getBlockId() : entryBlock.getBlockId() + "@0", entryBlock);
                    }
                }
            } else {
                if(subfolder.equals("ores")) {
                    entryOre = gson.fromJson(root.toString(), OreEntry.class);
                    oreEntries.add(entryOre);
                } else {
                    entryBlock = gson.fromJson(root.toString(), BlockEntry.class);
                    blockEntries.add(entryBlock);
                    identifierToBlockEntry.put(entryBlock.getName(), entryBlock);
                    identifierToBlockEntry.put(entryBlock.getBlockId().contains("@") ? entryBlock.getBlockId() : entryBlock.getBlockId() + "@0", entryBlock);
                }
            }
        }
    }
}
