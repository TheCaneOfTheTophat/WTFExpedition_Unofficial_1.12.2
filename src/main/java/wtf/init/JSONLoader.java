package wtf.init;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import wtf.WTFExpedition;
import wtf.config.OreEntry;
import wtf.config.OreGeneratorSettings;
import wtf.enums.Modifier;
import wtf.config.BlockEntry;
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
                    // TODO Use array of "generator" objects
                    ArrayList<String> stoneList = new ArrayList<>();
                    int minAmountPerChunk = 0;
                    int maxAmountPerChunk = 0;
                    int surfaceHeightMinPercentage = 0;
                    int surfaceHeightMaxPercentage = 100;
                    ArrayList<Integer> dimensionList = new ArrayList<>();
                    boolean dimensionListWhitelist = true;
                    boolean useDenseBlock = false;
                    String overlayPath = "";
                    int veinPercentDensity = 100;
                    ArrayList<String> biomeTypeList = new ArrayList<>();
                    boolean biomeTypeListWhitelist = false;
                    HashMap<String, Integer> percentGenerationPerBiomeType = new HashMap<>();

                    OreGeneratorSettings settings = null;
                    
                    String primaryGenerationType = "";
                    String secondaryGenerationType = "";
                    
                    double veinPitchAverage = 0;
                    int veinLength = 0;
                    int veinWidth = 0;
                    int veinVerticalThickness = 0;
                    
                    int cloudDiameter = 0;
                    
                    int blocksPerCluster = 0;

                    boolean ceiling = false;
                    boolean wall = false;
                    boolean floor = false;

                    if(object.has("stoneList"))
                        for (JsonElement arrayElement : object.get("stoneList").getAsJsonArray())
                            stoneList.add(arrayElement.getAsString());

                    if(object.has("minAmountPerChunk"))
                        minAmountPerChunk = object.get("minAmountPerChunk").getAsInt();

                    if(object.has("maxAmountPerChunk"))
                        maxAmountPerChunk = object.get("maxAmountPerChunk").getAsInt();

                    if(object.has("surfaceHeightMinPercentage"))
                        surfaceHeightMinPercentage = object.get("surfaceHeightMinPercentage").getAsInt();

                    if(object.has("surfaceHeightMaxPercentage"))
                        surfaceHeightMaxPercentage = object.get("surfaceHeightMaxPercentage").getAsInt();

                    if(surfaceHeightMaxPercentage - surfaceHeightMinPercentage < 1) {
                        WTFExpedition.wtfLog.error("Minimum surface height percentage greater than maximum surface height percentage in ore entry \"" + name + "\" at path \"" + jsonPath + "\", skipping!");
                        index++;
                        continue;
                    }

                    if(object.has("dimensionList"))
                        for (JsonElement arrayElement : object.get("dimensionList").getAsJsonArray())
                            dimensionList.add(arrayElement.getAsInt());

                    if(object.has("dimensionListWhitelist"))
                        dimensionListWhitelist = object.get("dimensionListWhitelist").getAsBoolean();

                    if(object.has("useDenseBlock"))
                        useDenseBlock = object.get("useDenseBlock").getAsBoolean();

                    if(object.has("overlayPath"))
                        overlayPath = object.get("overlayPath").getAsString();

                    if(overlayPath.isEmpty() && useDenseBlock) {
                        WTFExpedition.wtfLog.error("Undefined overlay path in ore entry \"" + name + "\" at path \"" + jsonPath + "\", skipping!");
                        index++;
                        continue;
                    }

                    if(object.has("veinPercentDensity"))
                        veinPercentDensity = object.get("veinPercentDensity").getAsInt();

                    if(object.has("biomeTypeList"))
                        for (JsonElement arrayElement : object.get("biomeTypeList").getAsJsonArray())
                            biomeTypeList.add(arrayElement.getAsString());

                    if(object.has("biomeTypeListWhitelist"))
                        biomeTypeListWhitelist = object.get("biomeTypeListWhitelist").getAsBoolean();

                    if(object.has("percentGenerationPerBiomeType"))
                        for(Map.Entry<String, JsonElement> entry : object.getAsJsonObject("percentGenerationPerBiomeType").entrySet())
                            percentGenerationPerBiomeType.put(entry.getKey(), entry.getValue().getAsInt());
                    
                    // GENERATOR
                    if(object.has("generation_settings")) {
                        JsonObject generationSettings = object.getAsJsonObject("generation_settings");

                        if(generationSettings.has("primaryGenerationType"))
                            primaryGenerationType = generationSettings.get("primaryGenerationType").getAsString();

                        if(generationSettings.has("secondaryGenerationType"))
                            secondaryGenerationType = generationSettings.get("secondaryGenerationType").getAsString();

                        if(primaryGenerationType.equals("vein")) {
                            if(generationSettings.has("vein_generation")) {
                                JsonObject veinGeneration = generationSettings.getAsJsonObject("vein_generation");

                                if(veinGeneration.has("veinPitchAverage"))
                                    veinPitchAverage = veinGeneration.get("veinPitchAverage").getAsDouble();

                                if(veinGeneration.has("veinLength"))
                                    veinLength = veinGeneration.get("veinLength").getAsInt();

                                if(veinGeneration.has("veinWidth"))
                                    veinWidth = veinGeneration.get("veinWidth").getAsInt();

                                if(veinGeneration.has("veinVerticalThickness"))
                                    veinVerticalThickness = veinGeneration.get("veinVerticalThickness").getAsInt();
                            }
                        } else if(primaryGenerationType.equals("cloud")) {
                            if(generationSettings.has("cloud_generation")) {
                                JsonObject cloudGeneration = generationSettings.getAsJsonObject("cloud_generation");

                                if (cloudGeneration.has("cloudDiameter"))
                                    cloudDiameter = cloudGeneration.get("cloudDiameter").getAsInt();
                            }
                        } else if(primaryGenerationType.equals("vanilla")) {
                            if(generationSettings.has("vanilla_generation")) {
                                JsonObject vanillaGeneration = generationSettings.getAsJsonObject("vanilla_generation");

                                if (vanillaGeneration.has("blocksPerCluster"))
                                    blocksPerCluster = vanillaGeneration.get("blocksPerCluster").getAsInt();
                            }
                        } else if(!primaryGenerationType.equals("cluster") && !primaryGenerationType.equals("single")) {
                            WTFExpedition.wtfLog.error("Invalid primary generation type in ore entry \"" + name + "\" at path \"" + jsonPath + "\", skipping!");
                            index++;
                            continue;
                        }

                        if(secondaryGenerationType.equals("cave")) {
                            if(generationSettings.has("cave_generation")) {
                                JsonObject caveGeneration = generationSettings.getAsJsonObject("cave_generation");

                                if(caveGeneration.has("ceiling"))
                                    ceiling = caveGeneration.get("ceiling").getAsBoolean();

                                if(caveGeneration.has("wall"))
                                    wall = caveGeneration.get("wall").getAsBoolean();

                                if(caveGeneration.has("floor"))
                                    floor = caveGeneration.get("floor").getAsBoolean();

                            } else if(!secondaryGenerationType.equals("underwater") && !secondaryGenerationType.isEmpty()) {
                                WTFExpedition.wtfLog.error("Invalid secondary generation type in ore entry \"" + name + "\" at path \"" + jsonPath + "\", skipping!");
                                index++;
                                continue;
                            }
                        }

                        settings = new OreGeneratorSettings(primaryGenerationType, secondaryGenerationType, veinPitchAverage, veinLength, veinWidth, veinVerticalThickness, cloudDiameter, ceiling, wall, floor, blocksPerCluster);
                    }

                    OreEntry entryOre = new OreEntry(blockId, name, stoneList, minAmountPerChunk, maxAmountPerChunk, surfaceHeightMinPercentage, surfaceHeightMaxPercentage, dimensionList, dimensionListWhitelist, useDenseBlock, overlayPath, veinPercentDensity, biomeTypeList, biomeTypeListWhitelist, percentGenerationPerBiomeType, settings);

                     oreEntries.add(entryOre);
                } else {
                    String fracturedBlockId = "";
                    String texture = "";
                    boolean fracturesFirstWhenMined = false;
                    boolean speleothems = false;
                    int percentageMineSpeedModifier = 100;
                    int percentageStability = 100;
                    HashMap<Modifier, String> modifierMap = new HashMap<>();

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
