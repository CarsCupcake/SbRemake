package me.carscupcake.sbremake.util.schematic;

import lombok.Getter;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.util.MutableCharSequence;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import org.enginehub.linbus.common.LinTagId;
import org.enginehub.linbus.stream.LinReadOptions;
import org.enginehub.linbus.stream.LinStream;
import org.enginehub.linbus.stream.exception.NbtParseException;
import org.enginehub.linbus.stream.impl.LinNbtReader;
import org.enginehub.linbus.tree.LinCompoundTag;
import org.enginehub.linbus.tree.LinIntArrayTag;
import org.enginehub.linbus.tree.LinTag;
import org.enginehub.linbus.tree.LinTagType;
import org.enginehub.linbus.tree.impl.LinTagReader;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;

@Getter
public class CompiledSchematic {
    private static final Method READ_VALUE_METHOD;

    static {
        try {
            READ_VALUE_METHOD = LinTagReader.class.getDeclaredMethod("readValue", LinStream.class, LinTagType.class);
            READ_VALUE_METHOD.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<Pos, Block> blocks;
    private int dataVersion = -1;
    private Pos origin;
    private Pos offset;
    private Pos dimensions = new Pos(0, 0, 0);
    private BlockState[] blockPalette;
    private final Set<Byte> remainingTags = new HashSet<>();

    public CompiledSchematic(File file) {
        try (var in = Files.newInputStream(file.toPath());
             var dataInputStream = new DataInputStream(in)) {
            dataInputStream.skipNBytes(15);
            var nbtInputStream = new LinNbtReader(dataInputStream, LinReadOptions.builder().build());

            byte type;
            String tag;
            while ((type = dataInputStream.readByte()) != NBTConstants.TYPE_END) {
                tag = dataInputStream.readUTF();
                switch (tag) {
                    case "DataVersion" -> {
                        this.dataVersion = dataInputStream.readInt();
                    }
                    case "Metadata" -> {
                        LinCompoundTag metadataCompoundTag = LinTagReader.readCompound(nbtInputStream);

                        LinCompoundTag worldEditTag = metadataCompoundTag.findTag("WorldEdit", LinTagType.compoundTag());
                        if (worldEditTag != null) {
                            LinIntArrayTag originTag = worldEditTag.findTag("Origin", LinTagType.intArrayTag());
                            if (originTag != null) {
                                int[] parts = originTag.value();
                                if (parts.length != 3) {
                                    throw new IOException("`Metadata > WorldEdit > Origin` int array length is invalid.");
                                }
                                this.origin = new Pos(parts[0], parts[1], parts[2]);
                            }
                        }
                    }
                    case "Offset" -> {
                        dataInputStream.skipNBytes(4); // Array Length field (4 byte int)
                        this.offset = new Pos(
                                dataInputStream.readInt(),
                                dataInputStream.readInt(),
                                dataInputStream.readInt()
                        );
                    }
                    case "Width" -> this.dimensions = this.dimensions.withX(dataInputStream.readShort() & 0xFFFF);
                    case "Height" -> this.dimensions = this.dimensions.withY(dataInputStream.readShort() & 0xFFFF);
                    case "Length" -> this.dimensions = this.dimensions.withZ(dataInputStream.readShort() & 0xFFFF);
                    case "Blocks" -> readBlocks();
                    case "Biomes", "Entities" -> {/*Not implemented*/}
                    default -> {
                        Object out = READ_VALUE_METHOD.invoke(null, nbtInputStream, LinTagType.fromId(LinTagId.fromId(type)));
                        if (out == null) throw new NbtParseException("`%s` is not a valid tag type.".formatted(type));
                        if (!(out instanceof LinTag<?>)) throw new Error("Type should never be like this!");
                    }
                }
            }

            if (!remainingTags.isEmpty()) {
                readRemainingDataReset(clipboard);
            } else if (this.dataCacheWriter != null || this.paletteCacheWriter != null) {
                readRemainingDataCache(clipboard);
            }
        } catch (Exception e) {
            Main.LOGGER.error("Failed to read compiled schematic %s".formatted(file.getName()));
            throw new RuntimeException(e);
        }
    }

    private void readBlocks() {
        blockPalette = new BlockState[Block.values().size()];
        readPalette(0x01, () -> blockPalette[0] != null,
                );

    }

    private void readPalette(
            byte paletteType,
            BooleanSupplier paletteAlreadyInitialized,
            PaletteInitializer paletteInitializer,
            PaletteDataApplier paletteDataApplier,
            AdditionalTagConsumer additionalTag
    ) throws IOException {

    }

    public static class BlockState {
        private final int internalId;
        private Material material;
        private final Block emptyBaseBlock;
        private CompoundBinaryTag compoundInput;

        public BlockState(Block block) {
            internalId = block.registry().id();
            emptyBaseBlock = block.defaultState();
            compoundInput = block.nbt();
        }

        public static BlockState get(String state) throws ParseException {
            return get(null, state);
        }

        /**
         * Returns a temporary BlockState for a given type and string.
         *
         * <p>It's faster if a BlockType is provided compared to parsing the string.</p>
         *
         * @param type  BlockType e.g., BlockTypes.STONE (or null)
         * @param state String e.g., minecraft:water[level=4]
         * @return BlockState
         */
        public static BlockState get(@Nullable Block type, String state) throws ParseException {
            return get(type, state, null);
        }

        /**
         * Returns a temporary BlockState for a given type and string.
         *
         * <p>It's faster if a BlockType is provided compared to parsing the string.</p>
         *
         * @param type  BlockType e.g., BlockTypes.STONE (or null)
         * @param state String e.g., minecraft:water[level=4]
         * @return BlockState
         */
        public static BlockState get(@Nullable Block type, String state, BlockState defaultState) throws ParseException {
            int propStrStart = state.indexOf('[');
            if (type == null) {
                CharSequence key;
                if (propStrStart == -1) {
                    key = state;
                } else {
                    MutableCharSequence charSequence = MutableCharSequence.getTemporal();
                    charSequence.setString(state);
                    charSequence.setSubstring(0, propStrStart);
                    key = charSequence;
                }
                type = Block.fromNamespaceId(key.toString());
                if (type == null) {
                    throw new ParseException("Invalid block type", 0);
                }
            }
            if (propStrStart == -1) {
                return new BlockState(type.defaultState());
            }

            List<? extends Map.Entry<String, String>> propList = type.properties().entrySet().stream().toList();

            if (state.charAt(state.length() - 1) != ']') {
                state = "%s]".formatted(state);
            }
            MutableCharSequence charSequence = MutableCharSequence.getTemporal();
            charSequence.setString(state);

            if (propList.size() == 1) {
                Map.Entry<String, String> property = propList.get(0);
                String name = property.getKey();

                charSequence.setSubstring(propStrStart + name.length() + 2, state.length() - 1);
                try {
                    int index = charSequence.length() <= 0 ? -1 : 0;
                    if (index != -1) {
                        return defaultState;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Invalid block state", e);
                }
            }
            int stateId;
            if (defaultState != null) {
                stateId = defaultState.internalId;
            } else {
                stateId = type.stateId();
            }
            int length = state.length();
            Map.Entry<String, String> property = null;

            int last = propStrStart + 1;
            for (int i = last; i < length; i++) {
                char c = state.charAt(i);
                switch (c) {
                    case ']':
                    case ',': {
                        charSequence.setSubstring(last, i);
                        if (property != null) {
                            int index;
                            try {
                                index = property.getIndexFor(charSequence);
                            } catch (Exception e) {
                                throw new InputParseException(Caption.of(
                                        "fawe.error.invalid-block-state-property",
                                        TextComponent.of(charSequence.toString()),
                                        TextComponent.of(property.getName()),
                                        TextComponent.of(state)
                                ), e);
                            }
                            if (index == -1) {
                                throw new ParseException(charSequence.toString(), 0);
                            }
                            stateId = property.modifyIndex(stateId, index);
                        } else {
                            // suggest
                            PropertyKey key = PropertyKey.getByName(charSequence);
                            if (key == null || !type.properties().containsKey(key.getName())) {
                                // Suggest property
                                String input = charSequence.toString();
                                Block finalType = type;
                                throw new ParseException("Unknown Property", 0);
                            } else {
                                throw new ParseException("No Operation for Input", 0);
                            }
                        }
                        property = null;
                        last = i + 1;
                        break;
                    }
                    case '=': {
                        charSequence.setSubstring(last, i);
                        property = type.properties().entrySet().stream().filter(e -> e.getKey().contentEquals(charSequence)).findFirst().get();
                        last = i + 1;
                        break;
                    }
                    default:
                        continue;
                }
            }
            return type.with(stateId >> BlockTypesCache.BIT_OFFSET);
        }
    }

    @ApiStatus.Internal
    @FunctionalInterface
    private interface PaletteInitializer {

        /**
         * Called for each palette entry (the mapping part, not data).
         *
         * @param index the index of the entry, as used in the Data byte array.
         * @param value the value for this entry (either biome type as resource location or the block state as a string).
         */
        void initialize(char index, String value);

    }

    @ApiStatus.Internal
    @FunctionalInterface
    private interface PaletteDataApplier {

        /**
         * Called for each palette data entry (not the mapping part, but the var-int byte array).
         *
         * @param index   The index of this data entry (due to var-int behaviour not necessarily the index in the data byte array).
         * @param ordinal The ordinal of this entry as defined in the palette mapping.
         */
        void apply(int index, char ordinal);

    }

    @ApiStatus.Internal
    @FunctionalInterface
    private interface AdditionalTagConsumer {

        /**
         * Called for each unknown nbt tag.
         *
         * @param type The type of the tag (as defined by the constants in {@link NBTConstants}).
         * @param name The name of the tag.
         */
        void accept(byte type, String name);

    }

    private PaletteInitializer provideBlockPaletteInitializer() {
        return (index, value) -> {
            try {
                this.blockPalette[index] = BlockState.get(value);
            } catch (InputParseException e) {
                LOGGER.warn("Invalid BlockState in palette: {}. Block will be replaced with air.", value);
                this.blockPalette[index] = BlockTypes.AIR.getDefaultState();
            }
        };
    }

    public static final int[] stateOrdinals;
}
