package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PeakOfTheMountain extends HotmUpgrade {
    public PeakOfTheMountain(SkyblockPlayer player) {
        super(player);
        if (level == 0) level = 1;
    }


    @Override
    public String getName() {
        return "Peak of the Mountain";
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public int nextLevelCost(int current) {
        return switch (current) {
            case 1 -> 50_000;
            case 2 -> 75_000;
            case 3 -> 100_000;
            case 4 -> 125_000;
            case 5 -> 500_000;
            case 6 -> 750_000;
            case 7 -> 1_000_000;
            case 8 -> 1_250_000;
            case 9 -> 1_500_000;
            default -> 0;
        };
    }

    @Override
    public void refund() {
    }

    @Override
    public Powder upgradeType(int current) {
        if (current >= 7) return Powder.GlacialPowder;
        if (current >= 5) return Powder.GemstonePowder;
        return Powder.MithrilPowder;
    }

    @Override
    public String getId() {
        return "PEAK_OF_THE_MOUNTAIN";
    }

    @Override
    public Lore lore(int level) {
        if (level == this.level) {
            List<String> strings = makeLore(level);
            return new Lore(strings);
        }
        return switch (level) {
            case 1, 5, 7 -> new Lore("§8+ §51 Token of the Mountain");
            case 2 -> new Lore("§8+ §a1 Forge Slot");
            case 3 -> new Lore("§8+ §a1 Commission Slot");
            case 4 -> new Lore("§8+ §21 Base Mithril Powder §7when mining §2Mithril");
            case 6 -> new Lore("§8+ §d2 Base Gemstone Powder §7when §7mining §dGemstones");
            case 8 -> new Lore("§8+ §b3 Base Glacite Powder §7when mining §bGlacite");
            case 9 -> new Lore("§8+ §a+10% chance §7for §bGlacial Mineshafts §7to spawn");
            case 10 -> new Lore("§8+ §52 Token of the Mountain");
            default -> Lore.EMPTY;
        };
    }

    private static @NotNull List<String> makeLore(int level) {
        List<String> strings = new ArrayList<>();
        strings.add("§8+§c 1 Pickaxe Ability Level");
        int tokens = 1;
        if (level > 1)
            strings.add("§8+ §a1 Forge Slot");
        if (level > 2)
            strings.add("§8+ §a1 Commission Slot");
        if (level > 3) {
            strings.add("§8+ §21 Base Mithril Powder §7when mining");
            strings.add("§2Mithril");
        }
        if (level > 4) tokens++;
        if (level > 5) {
            strings.add("§8+ §d2 Base Gemstone Powder §7when");
            strings.add("§7mining §dGemstones");
        }
        if (level > 6) tokens++;
        if (level > 7) {
            strings.add("§8+ §b3 Base Glacite Powder §7when mining");
            strings.add("§bGlacite");
        }
        if (level > 8)
            strings.add("§8+ §a+10% chance §7for §bGlacial Mineshafts §7to spawn");
        if (level > 9)
            tokens += 2;
        strings.add("§8+ §5" + (tokens) + " Token of the Mountain");
        return strings;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder((getPlayer().getHotm().getLevel() < 5) ? Material.BEDROCK : (level == getMaxLevel() ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK)).setName((level == 0 ? "§c" : (level == getMaxLevel() ? "§a" : "§e")) + (getName())).addAllLore("§7Level " + (level == 0 ? "1§8/" + (getMaxLevel()) : (level == getMaxLevel()) ? String.valueOf(getMaxLevel()) : (level) + "§8/" + (getMaxLevel())), "§7 ").addAllLore(lore((level == 0) ? 1 : level).build(null, getPlayer())).addLoreRow("§8 ").addLoreIf(() -> level == 0, "§7Cost", "§51 Token of the Mountain").addLoreIf(() -> level != getMaxLevel() && level != 0, "§a=====[UPGRADE]=====").addLoreIf(() -> level != getMaxLevel() && level != 0, lore(level + 1).build(null, getPlayer())).addLoreIf(() -> level != getMaxLevel() && level != 0, " ", "§7Cost", (upgradeType(level).getColor()) + (StringUtils.toFormatedNumber(nextLevelCost(level))) + " " + (upgradeType(level).getName())).build();
    }

    @Override
    public int levelRequirement() {
        return 5;
    }
}
