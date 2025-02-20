package wtf.init;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import wtf.WTFExpedition;
import wtf.config.BlockEntry;
import wtf.config.OreEntry;
import wtf.config.WTFExpeditionConfig;

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

        if (WTFExpeditionConfig.loadDefaultFiles) {
            try {
                URL classUrl = WTFExpedition.class.getProtectionDomain().getCodeSource().getLocation(); // Get URL of this class

                Path defaultsDirectory;

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

            int index = 0;

            for (JsonElement element : (JsonArray) root) {
                JsonObject object = (JsonObject) element;

                if(parent.contains("ores")) {
                    // TODO Better JSON parsing, use array of "generator" objects
                    OreEntry entryOre = gson.fromJson(element.toString(), OreEntry.class);
                    oreEntries.add(entryOre);
                } else {
                    String blockId = "";
                    String name = "";
                    String fracturedBlockId = "";
                    String texture = "";
                    byte flags = 0;
                    int percentageMineSpeedModifier = 100;
                    int percentageStability = 100;

                    if(object.has("blockId"))
                        blockId = object.get("blockId").getAsString();

                    if(blockId.isEmpty()) {
                        WTFExpedition.wtfLog.error("Undefined block ID in block entry " + index + " at path \"" + jsonPath + "\", skipping!");
                        index++;
                        continue;
                    }

                    if(object.has("name"))
                        name = object.get("name").getAsString();

                    if(name.isEmpty()) {
                        WTFExpedition.wtfLog.error("Undefined name in block entry " + index + " at path \"" + jsonPath + "\", skipping!");
                        index++;
                        continue;
                    }

                    blockId = blockId.contains("@") ? blockId : blockId + "@0";

                    if(object.has("fracturedBlockId")) {
                        fracturedBlockId = object.get("fracturedBlockId").getAsString();
                        fracturedBlockId = fracturedBlockId.contains("@") ? fracturedBlockId : fracturedBlockId + "@0";
                    }

                    if(object.has("texture"))
                        texture = object.get("texture").getAsString();

                    if(object.has("fracturesFirstWhenMined") && object.get("fracturesFirstWhenMined").getAsBoolean())
                        flags = (byte) (flags | 1);

                    if(object.has("percentageMineSpeedModifier"))
                        percentageMineSpeedModifier = Math.max(Math.min(object.get("percentageMineSpeedModifier").getAsInt(), 100), 0);

                    if(object.has("percentageStability"))
                        percentageStability = Math.max(Math.min(object.get("percentageStability").getAsInt(), 100), 0);

                    if(object.has("decoration")) {
                        JsonObject decoration = object.getAsJsonObject("decoration");

                        if(decoration.has("mossy"))
                            flags = (byte) (flags | 2);

                        if(decoration.has("soul"))
                            flags = (byte) (flags | 4);

                        if(decoration.has("cracked"))
                            flags = (byte) (flags | 8);

                        if(decoration.has("lava_crust"))
                            flags = (byte) (flags | 16);

                        if(decoration.has("water_drip"))
                            flags = (byte) (flags | 32);

                        if(decoration.has("lava_drip"))
                            flags = (byte) (flags | 64);

                        if(decoration.has("speleothems"))
                            flags = (byte) (flags | -128);
                    }

                    BlockEntry entryBlock = new BlockEntry(blockId, fracturedBlockId, name, texture, flags, percentageMineSpeedModifier, percentageStability);

                    blockEntries.add(entryBlock);
                    identifierToBlockEntry.put(name, entryBlock);
                    identifierToBlockEntry.put(blockId, entryBlock);

                    index++;
                }
            }
        }
    }

    public static BlockEntry getEntryFromState(IBlockState state) {
        return identifierToBlockEntry.get(state.getBlock().getRegistryName() + "@" + state.getBlock().getMetaFromState(state));
    }

    public static IBlockState getStateFromId(String id) {
        String[] idSplit = id.split("@");

        if (Block.getBlockFromName(idSplit[0]) == null)
            return null;

        return Block.getBlockFromName(idSplit[0]).getStateFromMeta(Integer.parseInt(idSplit[1]));
    }
}
