package wtf.init;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import wtf.WTFExpedition;
import wtf.enums.Modifier;
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

        List<Path> allJsons = new ArrayList<>();

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonParser parser = new JsonParser();
        JsonElement root;

        if (WTFExpeditionConfig.loadDefaultFiles) {
            final String guideFilename = "Configuration_Guide.txt";
            List<Path> defaultFiles;
            FileSystem fileSystem = null;
            boolean fromJar = false;

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

                String blockId = "";
                String name = "";

                if(object.has("blockId"))
                    blockId = object.get("blockId").getAsString();

                if(blockId.isEmpty()) {
                    WTFExpedition.wtfLog.error("Undefined block ID in " + parent.substring(0, parent.length() - 1) + " entry " + index + " at path \"" + jsonPath + "\", skipping!");
                    index++;
                    continue;
                }

                if(object.has("name"))
                    name = object.get("name").getAsString();

                if(name.isEmpty()) {
                    WTFExpedition.wtfLog.error("Undefined name in " + parent.substring(0, parent.length() - 1) + " entry " + index + " at path \"" + jsonPath + "\", skipping!");
                    index++;
                    continue;
                }

                blockId = addMeta(blockId);

                if(parent.contains("ores")) {
                    // TODO Better JSON parsing, use array of "generator" objects
                    OreEntry entryOre = gson.fromJson(element.toString(), OreEntry.class);
                    oreEntries.add(entryOre);
                } else {
                    String fracturedBlockId = "";
                    String texture = "";
                    boolean fracturesFirstWhenMined = false;
                    boolean speleothems = false;
                    int percentageMineSpeedModifier = 100;
                    int percentageStability = 100;

                    if(object.has("fracturedBlockId"))
                        fracturedBlockId = object.get("fracturedBlockId").getAsString();

                    if(object.has("texture"))
                        texture = object.get("texture").getAsString();

                    if(object.has("fracturesFirstWhenMined"))
                        fracturesFirstWhenMined = object.get("fracturesFirstWhenMined").getAsBoolean();

                    if(object.has("percentageMineSpeedModifier"))
                        percentageMineSpeedModifier = Math.max(Math.min(object.get("percentageMineSpeedModifier").getAsInt(), 100), 0);

                    if(object.has("percentageStability"))
                        percentageStability = Math.max(Math.min(object.get("percentageStability").getAsInt(), 100), 0);

                    HashMap<Modifier, String> modifierMap = new HashMap<>();

                    if(object.has("modifiers")) {
                        JsonObject modifiers = object.getAsJsonObject("modifiers");

                        if(modifiers.has("mossy"))
                            modifierMap.put(Modifier.MOSS, modifiers.get("mossy").getAsString());

                        if(modifiers.has("soul"))
                            modifierMap.put(Modifier.SOUL, modifiers.get("soul").getAsString());

                        if(modifiers.has("cracked"))
                            modifierMap.put(Modifier.CRACKED, modifiers.get("cracked").getAsString());

                        if(modifiers.has("lava_crust"))
                            modifierMap.put(Modifier.LAVA_CRUST, modifiers.get("lava_crust").getAsString());

                        if(modifiers.has("wet"))
                            modifierMap.put(Modifier.WET, modifiers.get("wet").getAsString());

                        if(modifiers.has("lava_dripping"))
                            modifierMap.put(Modifier.LAVA_DRIPPING, modifiers.get("lava_dripping").getAsString());

                        if(modifiers.has("frozen"))
                            modifierMap.put(Modifier.FROZEN, modifiers.get("frozen").getAsString());

                        if(modifiers.has("brick"))
                            modifierMap.put(Modifier.BRICK, modifiers.get("brick").getAsString());
                    }

                    if(object.has("speleothems"))
                        speleothems = object.get("speleothems").getAsBoolean();

                    BlockEntry entryBlock = new BlockEntry(blockId, fracturedBlockId, name, texture, fracturesFirstWhenMined, percentageMineSpeedModifier, percentageStability, modifierMap, speleothems);

                    blockEntries.add(entryBlock);
                    identifierToBlockEntry.put(name, entryBlock);
                    identifierToBlockEntry.put(blockId, entryBlock);

                    index++;
                }
            }
        }
    }

    public static String addMeta(String id) {
        return id.contains("@") ? id : id + "@0";
    }

    public static String processId(String id) {
        if(id.isEmpty())
            return "";

        BlockEntry entry = identifierToBlockEntry.get(id);
        return entry != null ? entry.getBlockId() : id;
    }

    public static BlockEntry getEntryFromState(IBlockState state) {
        return identifierToBlockEntry.get(state.getBlock().getRegistryName() + "@" + state.getBlock().getMetaFromState(state));
    }

    public static IBlockState getStateFromId(String id) {
        String[] idSplit = addMeta(id).split("@");

        if (Block.getBlockFromName(idSplit[0]) == null)
            return null;

        return Block.getBlockFromName(idSplit[0]).getStateFromMeta(Integer.parseInt(idSplit[1]));
    }
}
