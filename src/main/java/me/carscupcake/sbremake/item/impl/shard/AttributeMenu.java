package me.carscupcake.sbremake.item.impl.shard;

import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.xp.SkyblockXpTask;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.gui.InventoryBuilder;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.gui.PageGui;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public record AttributeMenu(SkyblockPlayer player) implements SkyblockXpTask {
    @Override
    public long getTotalXp() {
        return player.getAttributesShards().values().stream().map(AttributeEntry::level).reduce(0, Integer::sum);
    }

    @Override
    public long getMaxXp() {
        return Shard.values().length * 10L;
    }

    public void open() {
        List<Inventory> inventories = new LinkedList<>();
        InventoryBuilder builder = null;
        int i = 10;
        for (var shard : Arrays.stream(Shard.values()).sorted(IAttributeShard::compareTo).toList()) {
            if (builder == null) {
                builder = new InventoryBuilder(6, "Attribute Shards")
                        .fill(TemplateItems.EmptySlot.getItem(), 0, 8)
                        .fill( TemplateItems.EmptySlot.getItem(), 45, 53)
                        .verticalFill(0, 6, TemplateItems.EmptySlot.getItem())
                        .verticalFill(8, 6, TemplateItems.EmptySlot.getItem());
            }
            var playerShard = player.getAttributesShards().get(shard);
            var level = playerShard == null ? 0 : playerShard.level();
            var previewItem = new ItemBuilder(level == 0 ? Material.GRAY_DYE : shard.getMaterial())
                    .setName("§6" + shard.getAbilityName() + (level == 0 ? "" : (" " + StringUtils.toRoman(level))))
                    .addAllLore("§8" + shard.getCategory(), " ")
                    .addAllLore(shard.getLore().build(null, player))
                    .addAllLore(" ", "§7Source: " + shard.getRarity().getPrefix() + shard.getDisplayName() + " §8(ID " + shard.getShardId() + ")",
                            "§7Rarity: " + shard.getRarity().getPrefix() + "§l" + shard.getRarity().getDisplay().toUpperCase(), " ");
            if (playerShard != null) {
                previewItem.addAllLore("§7Level: " + playerShard.level());
                if (playerShard.level() != 10)
                    previewItem.addLoreRow("§7Syphon §b" + playerShard.shardsToNextLevel() + " §7to level up!");
            } else {
                previewItem.addAllLore("§7Syphon §b1 §7Shard to unlock!");
            }
            if (shard.getMaterial() == Material.PLAYER_HEAD)
                previewItem.setHeadTexture(shard.getHeadValue());
            builder.setItem(previewItem.build(), i);

            if ((i + 2) % 9 == 0) {
                if (i == 43) {
                    inventories.add(builder.build());
                    builder = null;
                    i = 10;
                    continue;
                }
                else i += 2;
            }
            i++;
        }
        if (builder != null) inventories.add(builder.build());
        var pagedGui = new PageGui(inventories, PageGui.ItemSlotPosition.BottomRight, PageGui.ItemSlotPosition.BottomLeft);
        pagedGui.setCancelled(true);
        pagedGui.showGui(player);
    }

    public void openHuntingBox() {
        List<Inventory> inventories = new LinkedList<>();
        InventoryBuilder builder = null;
        int i = 10;
        var shards = player.getHuntingBox().entrySet().stream().sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey())).toList();
        for (var entry : shards) {
            if (builder == null) {
                builder = new InventoryBuilder(6, "Hunting Box")
                        .fill(TemplateItems.EmptySlot.getItem(), 0, 8)
                        .fill( TemplateItems.EmptySlot.getItem(), 45, 53)
                        .verticalFill(0, 6, TemplateItems.EmptySlot.getItem())
                        .verticalFill(8, 6, TemplateItems.EmptySlot.getItem());
            }
            var shard = entry.getKey();
            var playerShard = player.getAttributesShards().get(shard);
            var level = playerShard == null ? 0 : playerShard.level();
            var previewItem = new ItemBuilder(shard.getMaterial())
                    .setName(shard.getRarity().getPrefix() + shard.getDisplayName());
            for (var family : shard.getFamilies())
                    previewItem.addLoreRow("§8" + family.getName() + " Family");
            previewItem.addLoreRow(" ");
            previewItem.addLoreRow("§6" + shard.getAbilityName() + (level == 0 ? "" : (" " + StringUtils.toRoman(level))) + " §8(" + shard.getCategory() + ")");
            previewItem.addAllLore(shard.getLore().build(null, player));
            previewItem.addAllLore(" ", "§7Owned: §b" + entry.getValue() + " Shards");
            if (level == 10) {
                previewItem.addLoreRow("§a§lATTRIBUTE MAXED");
                previewItem.addLoreRow(" ");
            } else {
                var tillNextLevel = playerShard == null ? 1 : playerShard.shardsToNextLevel();
                previewItem.addAllLore("§7Syphon §b" + tillNextLevel + " §7more to level up!", " ",
                        "§eLeft-Click to syphon!",
                        "§eShift Left-Click to syphon all!");
            }
            previewItem.addAllLore("§eRight-click to convert to an item",
                    "§eShift Right-click to convert to a stack of items!");
            if (shard.getMaterial() == Material.PLAYER_HEAD)
                previewItem.setHeadTexture(shard.getHeadValue());
            builder.setItem(previewItem.build(), i);

            if ((i + 2) % 9 == 0) {
                if (i == 43) {
                    inventories.add(builder.build());
                    builder = null;
                    i = 10;
                    continue;
                }
                else i += 2;
            }
            i++;
        }
        if (builder != null) inventories.add(builder.build());
        var pagedGui = new PageGui(inventories, PageGui.ItemSlotPosition.BottomRight, PageGui.ItemSlotPosition.BottomLeft);
        pagedGui.setCancelled(true);
        pagedGui.setGeneralClickEvent(event -> {
            var slot = event.getSlot();
            if (slot < 10 || slot > 53) {return true;}
            if (slot % 9 == 0 || (slot + 1) % 9 == 0) {return true;}
            var index = slot - 10 + ((slot / 9) - 1) * -2 + pagedGui.getPage() * 4 * 7;
            var indexed = shards.get(index);
            var item = indexed.getKey();
            var playerShard = player.getAttributesShards().get(item);
            if (event.getClick() instanceof Click.Left || event.getClick() instanceof Click.LeftShift) {
                if (playerShard != null && playerShard.level() == 10) {
                    player.sendMessage("§cThis shard is already maxed!");
                    return true;
                }
                var total = event.getClick() instanceof Click.Left ? 1 : Math.min(indexed.getValue(),
                        (playerShard == null ? new AttributeEntry(item, 0) : playerShard).shardsToMax());
                playerShard = new AttributeEntry(item, total + (playerShard == null ? 0 : playerShard.amount()));
                player.getAttributesShards().put(item, playerShard);
                player.getHuntingBox().subtract(item, total);
                openHuntingBox();
            } else if (event.getClick() instanceof Click.Right || event.getClick() instanceof Click.RightShift) {
                var amount = event.getClick() instanceof Click.Right ? 1 : Math.min(64, indexed.getValue());
                var newItem = SbItemStack.from(ShardItem.class).withModifier(Modifier.ATTRIBUTE, item).withAmount(amount).update(player);
                if (!player.addItem(newItem, false)) {
                    player.sendMessage("§cYour inventory is full!");
                    return true;
                }
                player.getHuntingBox().subtract(item, amount);
                openHuntingBox();
            }
            return true;
        });
        pagedGui.showGui(player);
    }

    public static @NotNull Map<String, Lore.IPlaceHolder> toPlaceholderMap(Shard thisShard, @NotNull Map<String, Function<Integer, Double>> placeholder) {
        var map = new HashMap<String, Lore.IPlaceHolder>(placeholder.size());
        for (var entry : placeholder.entrySet()) {
            map.put(entry.getKey(), (item, player) -> {
                if (item != null && item.sbItem() instanceof ShardItem) {
                    var modifier = item.getModifier(Modifier.ATTRIBUTE);
                    if (modifier != null) {
                        return StringUtils.cleanDouble(entry.getValue().apply(1));
                    }
                }
                if (player == null) return StringUtils.cleanDouble(entry.getValue().apply(1));
                var shardEntry = player.getAttributesShards().get(thisShard);
                var level = shardEntry != null ? shardEntry.level() : 1;
                return StringUtils.cleanDouble(entry.getValue().apply(level));
            });
        }
        return map;
    }
}
