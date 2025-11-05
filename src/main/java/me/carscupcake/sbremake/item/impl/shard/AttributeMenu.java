package me.carscupcake.sbremake.item.impl.shard;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.xp.SkyblockXpTask;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.gui.InventoryBuilder;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.gui.PageGui;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Material;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
                            "§7Rarity: " + shard.getRarity().getPrefix() + "§l" + shard.getRarity().getDisplay().toUpperCase(), "");
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
        for (var entry : player.getHuntingBox().entrySet().stream().sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey())).toList()) {
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
}
