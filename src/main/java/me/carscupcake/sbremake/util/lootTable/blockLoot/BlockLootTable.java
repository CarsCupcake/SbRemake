package me.carscupcake.sbremake.util.lootTable.blockLoot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.enchantment.SkyblockEnchantment;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.lootTable.ILootTable;
import net.kyori.adventure.key.Key;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@Getter
public class BlockLootTable implements ILootTable<SbItemStack> {
    public static Map<Integer, BlockLootTable> blockLootTables = new HashMap<>();
    private final Pool[] pools;
    private final Block block;

    public BlockLootTable(InputStream blockLootFile, Block block) {
        this.block = block;
        try (var reader = new InputStreamReader(blockLootFile)) {
            JsonObject element = JsonParser.parseReader(reader).getAsJsonObject();
            if (!element.get("type").getAsString().equals("minecraft:block"))
                throw new UnsupportedOperationException("Block loot table should be a block loot table");
            LinkedList<Pool> pools = new LinkedList<>();
            if (element.get("pools") == null) {
                this.pools = null;
                return;
            }
            for (JsonElement pool : element.get("pools").getAsJsonArray()) {
                JsonObject poolElement = pool.getAsJsonObject();
                JsonArray poolEntries = poolElement.get("entries").getAsJsonArray();
                PoolEntry[] entries = new PoolEntry[poolEntries.size()];
                for (int i = 0; i < poolEntries.size(); i++) {
                    JsonObject poolEntry = poolEntries.get(i).getAsJsonObject();
                    entries[i] = readEntry(poolEntry);
                }
                pools.add(new Pool(poolElement.get("bonus_rolls").getAsDouble(), entries, poolElement.get("rolls").getAsInt(), parsePredicates(poolElement.get("conditions"))));
            }
            this.pools = new Pool[pools.size()];
            int i = 0;
            for (var pool : pools) {
                this.pools[i++] = pool;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Contract("_ -> new")
    private @NotNull PoolEntry readEntry(@NotNull JsonObject poolEntry) {
        var poolEntries = poolEntry.get("children");
        PoolEntry[] entries = new PoolEntry[0];
        if (poolEntries != null && poolEntries.isJsonArray()) {
            var arr = poolEntries.getAsJsonArray();
            entries = new PoolEntry[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                entries[i] = readEntry(arr.get(i).getAsJsonObject());
            }
        }
        var name = poolEntry.get("name");
        return new PoolEntry(Objects.requireNonNull(PoolType.parse(poolEntry.get("type").getAsString())), entries, parsePredicates(poolEntry.get("conditions")), new Function[0], name == null ? null : name.getAsString());
    }

    private static Predicate<BlockContext>[] parsePredicates(JsonElement cond) {
        Predicate[] conditions = new Predicate[0];
        if (cond != null) {
            conditions = cond.getAsJsonArray().asList().stream().map(entry -> {
                var condition = entry.getAsJsonObject().get("condition").getAsString();
                Predicate<BlockContext> out = switch (condition) {
                    case "minecraft:match_tool" -> {
                        var predicate = entry.getAsJsonObject().get("predicate").getAsJsonObject();
                        String[] items = new String[0];
                        var itemsObj = predicate.get("items");
                        if (itemsObj != null) {
                            if (itemsObj.isJsonPrimitive())
                                items = new String[]{itemsObj.getAsString().split(":", 2)[1]};
                            else if (itemsObj.isJsonArray())
                                items = itemsObj.getAsJsonArray().asList().stream().map(JsonElement::getAsString).toArray(String[]::new);
                        }
                        var oPredicates = predicate.get("predicates");
                        Enchantment[] enchantments = new Enchantment[0];
                        if (oPredicates != null) {
                            var predicates = oPredicates.getAsJsonObject();
                            enchantments = predicates.get("minecraft:enchantments").getAsJsonArray().asList().stream().map(jsonElement -> {
                                var enchantents = jsonElement.getAsJsonObject().get("enchantments").getAsString();
                                var levels = jsonElement.getAsJsonObject().get("levels").getAsJsonObject();
                                return new Enchantment(SkyblockEnchantment.enchantments.get(enchantents), levels.get("min").getAsInt(), levels.get("max") == null ? -1 : levels.get("max").getAsInt());
                            }).toArray(Enchantment[]::new);
                        }
                        yield new MatchToolPredicate(items, enchantments);
                    }
                    case "minecraft:table_bonus" -> {
                        try {
                            var items = entry.getAsJsonObject().get("chances").getAsJsonArray().asList();
                            yield new TableBonus(SkyblockEnchantment.enchantments.get(entry.getAsJsonObject().get("enchantment").getAsString()),
                                    items.stream().map(JsonElement::getAsDouble).toArray(Double[]::new));
                        } catch (Exception e) {
                            Main.LOGGER.error(entry.toString(), e);
                            yield null;
                        }
                    }
                    case "minecraft:survives_explosion" -> null;
                    case "minecraft:block_state_property" -> {
                        Map<String, String> properties = new HashMap<>();
                        for (Map.Entry<String, JsonElement> entry1 : entry.getAsJsonObject().get("properties").getAsJsonObject().entrySet())
                            properties.put(entry1.getKey(), entry1.getValue().getAsString());
                        yield new BlockStateProperties(properties);
                    }
                    case "minecraft:inverted" -> {
                        var arr = new JsonArray();
                        arr.add(entry.getAsJsonObject().get("term"));
                        yield new Invert(parsePredicates(arr)[0]);
                    }
                    case "minecraft:any_of" -> new Or(parsePredicates(entry.getAsJsonObject().get("terms")));
                    case "minecraft:random_chance" -> new Chance(entry.getAsJsonObject().get("chance").getAsDouble());
                    case "minecraft:location_check" -> null; //TODO Implement later
                    case "minecraft:entity_properties" -> null; //I don't get this one lol. Here is the json {"condition":"minecraft:entity_properties","entity":"this","predicate":{}}
                    default -> {
                        Main.LOGGER.warn("Invalid condition '{}'", condition);
                        yield null;
                    }
                };
                return out;
            }).filter(Objects::nonNull).toArray(Predicate[]::new);
        }
        return conditions;
    }

    @Override
    public Set<SbItemStack> loot(SkyblockPlayer player) {
        Set<SbItemStack> stacks = new HashSet<>();
        for (Pool pool : pools) {
            boolean allowed = true;
            for (Predicate<BlockContext> b : pool.conditions)
                if (b.test(new BlockContext(1, player, block))){
                    allowed = false;
                    break;
                }
            if (!allowed) continue;
            for (int i = 0; i < pool.rolls; i++) {
                for (PoolEntry entry : pool.entries) {
                    BlockContext blockContext = new BlockContext(1, player, block);
                    var item = calculatePoolEntry(blockContext, entry);
                    if (item != null) {
                        stacks.addAll(item);
                    }
                }
            }
        }
        return stacks;
    }

    private @Nullable Set<SbItemStack> calculatePoolEntry(BlockContext context, @NotNull PoolEntry entry) {
        if (entry.type == PoolType.Alternatives) {
            for (PoolEntry child : entry.children) {
                var out = calculatePoolEntry(context, child);
                if (out != null)
                    return out;
            }
            return null;
        }
        boolean allowed = true;
        for (Predicate<BlockContext> condition : entry.conditions) {
            if (!condition.test(context)) {
                allowed = false;
                break;
            }
        }
        if (!allowed) return null;
        assert entry.name != null;
        SbItemStack item = SbItemStack.base(Objects.requireNonNull(Material.fromNamespaceId(NamespaceID.from(Key.key(entry.name, ':')))));
        for (Function<BlockContext, Integer> function : entry.functions) {
            assert item != null;
            int amount = function.apply(context);
            if (amount == context.currentDrop) continue;
            item = item.withAmount(amount);
            context = new BlockContext(amount, context.player, context.block);
        }
        return item == null ? null : Set.of(item);
    }

    public record Pool(double bonusRolls, PoolEntry[] entries, int rolls, Predicate<BlockContext>[] conditions) {

    }

    public record PoolEntry(PoolType type, PoolEntry[] children, Predicate<BlockContext>[] conditions,
                            Function<BlockContext, Integer>[] functions, @Nullable String name) {

    }

    public record BlockContext(int currentDrop, SkyblockPlayer player, Block block) {

    }

    public record BlockStateProperties(Map<String, String> s) implements Predicate<BlockContext> {

        @Override
        public boolean test(BlockContext context) {
            for (Map.Entry<String, String> entry : s.entrySet()) {
                if (!Objects.equals(context.block.getProperty(entry.getKey()), entry.getValue())) return false;
            }
            return true;
        }
    }

    public enum PoolType {
        Block,
        Item,
        Dynamic,
        Alternatives;

        public static @Nullable PoolType parse(String s) {
            for (PoolType t : PoolType.values()) {
                if (s.equalsIgnoreCase(MessageFormat.format("minecraft:{0}", t.name().toLowerCase()))) {
                    return t;
                }
            }
            return null;
        }
    }

    public record MatchToolPredicate(String[] items, Enchantment[] enchantments) implements Predicate<BlockContext> {

        @Override
        public boolean test(@NotNull BlockContext blockContext) {
            if (blockContext.player == null) return false;
            var item = blockContext.player.getSbItemInHand(Player.Hand.MAIN);
            if (item == null) return false;
            boolean result = false;
            for (String itemId : items) {
                if (itemId.equalsIgnoreCase(item.sbItem().getId())) {
                    result = true;
                    break;
                }
            }
            if (!result && items.length != 0) return false;
            return Arrays.stream(enchantments).filter(enchantment -> {
                var level = item.getEnchantmentLevel(enchantment.enchantment);
                var b = level < enchantment.min || !(enchantment.max < 0 || level <= enchantment.max);
                return b;
            }).findFirst().isEmpty();
        }
    }

    record Enchantment(SkyblockEnchantment enchantment, int min, int max) {

    }

    public record Invert(Predicate<BlockContext> invert) implements Predicate<BlockContext> {

        @Override
        public boolean test(BlockContext context) {
            return !invert.test(context);
        }
    }

    public record Or(Predicate<BlockContext>[] conditions) implements Predicate<BlockContext> {

        @Override
        public boolean test(BlockContext context) {
            for (Predicate<BlockContext> condition : conditions) {
                if (condition.test(context)) return true;
            }
            return false;
        }
    }

    public record Chance(double chance) implements Predicate<BlockContext> {

        @Override
        public boolean test(BlockContext context) {
            return RANDOM.nextDouble() < chance;
        }
    }

    private static final Random RANDOM = new Random();

    public record TableBonus(SkyblockEnchantment enchantement, Double[] chances) implements Predicate<BlockContext> {

        @Override
        public boolean test(@NotNull BlockContext blockContext) {
            int level = 0;
            if (blockContext.player != null) {
                var item = blockContext.player.getSbItemInHand(Player.Hand.MAIN);
                if (item != null)
                    level = item.getEnchantmentLevel(enchantement);
            }
            return RANDOM.nextDouble() < chances[level > chances.length ? chances.length - 1 : level];
        }
    }
}
