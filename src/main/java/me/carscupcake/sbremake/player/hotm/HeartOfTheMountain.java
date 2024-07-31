package me.carscupcake.sbremake.player.hotm;

import lombok.Getter;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.impl.*;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import me.carscupcake.sbremake.util.item.OversizedGui;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HeartOfTheMountain {
    private static final int[] XP = {0, 3_000, 9_000, 25_000, 60_000, 100_000, 150_000, 210_000, 290_000, 400_000};
    private static final List<Class<? extends HotmUpgrade>> UPGRADES = List.of(MiningSpeed.class, MiningFortune.class,
            TitaniumInsanium.class, QuickForge.class, MiningSpeedBoost.class, Pickobulus.class
            , LuckOfTheCave.class, DailyPowder.class, Crystallized.class, MiningMadness.class, EfficientMiner.class, FrontLoaded.class, SkyMall.class, SeasonedMineman.class, Orbiter.class, PrecisionMining.class);
    private final SkyblockPlayer player;
    private final List<HotmUpgrade> upgrades = new ArrayList<>();
    private PickaxeAbility activeAbility = null;
    private int level;
    private int xp;
    private int tokenOfTheMountain = 0;

    public HeartOfTheMountain(SkyblockPlayer player) {
        this.player = player;
        ConfigFile file = new ConfigFile("hotm", player);
        String activeId = file.get("active_ability", ConfigSection.STRING);
        level = file.get("level", ConfigSection.INTEGER, 0);
        xp = file.get("xp", ConfigSection.INTEGER, 0);
        if (level > 0) {
            tokenOfTheMountain++;
            tokenOfTheMountain += (level - 1) * 2;
            if (level >= 7) tokenOfTheMountain++;
        }
        for (Class<? extends HotmUpgrade> upgradeClass : UPGRADES) {
            try {
                HotmUpgrade upgrade = upgradeClass.getConstructor(SkyblockPlayer.class).newInstance(player);
                upgrades.add(upgrade);
                if (upgrade.level > 0) tokenOfTheMountain--;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (activeId != null) {
            for (HotmUpgrade upgrade : upgrades)
                if (upgrade instanceof PickaxeAbility pickaxeAbility)
                    if (upgrade.getId().equals(activeId))
                        activeAbility = pickaxeAbility;
        }
    }

    public void save() {
        for (HotmUpgrade upgrade : upgrades)
            upgrade.save();
        ConfigFile file = new ConfigFile("hotm", player);
        file.set("active_ability", (activeAbility == null) ? null : activeAbility.getId(), ConfigSection.STRING);
        file.set("level", level, ConfigSection.INTEGER);
        file.set("xp", xp, ConfigSection.INTEGER);
        file.save();
    }

    public <T extends HotmUpgrade> T getUpgrade(Class<T> upgrade) {
        for (HotmUpgrade u : upgrades)
            if (u.getClass().equals(upgrade))
                return (T) u;
        return null;
    }

    public void addXp(int amount) {
        xp += amount;
        while (level < XP.length && XP[level] <= xp) {
            xp -= XP[level];
            level++;
            if (level == 1) tokenOfTheMountain++;
            else {
                tokenOfTheMountain += 2;
                if (level == 7) tokenOfTheMountain++;
            }
            //TODO level up message
            player.sendMessage(STR."§5Hotm§f level \{level}");
        }
    }

    private static final List<String> unlockedLore = List.of("§7You have unlocked this tier. All", "§7perks and abilities on this tier", "§7are available for unlocking with", "§5Token of the Mountain§7.");
    private static final List<String> lockedLore = List.of("§7Progress through your Heart of the", "§7Mountain by gaining §5HOTM Exp§7, which", "§7is earned through completing", "§aCommissions", " ", "§7Commissions are tasks given by the", "§6§lKing §7in the §bRoyal Place§7. Complete", "§7them to earn bountiful rewards!");
    //0  1
    //9  2
    //18 3
    //27 4
    //36 5
    //45 6
    //54 7
    //63 8
    //72 9
    //81 10
    //90 11

    private static final int[] HotmUpgradeSlots = {85, 76, 75, 77, 74, 78, 65, 67, 69, 56, 58, 60, 55, 57, 59, 61};

    public void openMenu() {
        List<ItemStack> item = new ArrayList<>();
        for (int i = 0; i++ < 99; ) {
            item.add(TemplateItems.EmptySlot.getItem());
        }
        int j = 0;
        for (int i : HotmUpgradeSlots) {
            item.set(i, upgrades.get(j).getItem());
            j++;
            if (j == upgrades.size()) break;
        }

        for (int i = 0; i < 10; i++) {
            int level = 10 - i;
            item.set(i * 9, new ItemBuilder(level == this.level + 1 ? Material.YELLOW_STAINED_GLASS_PANE : (level > this.level) ? Material.RED_STAINED_GLASS_PANE : Material.GREEN_STAINED_GLASS_PANE)
                    .addLoreIf(() -> level > this.level, lockedLore)
                    .addLoreIf(() -> level <= this.level, unlockedLore)
                    .setName(STR."\{level <= this.level ? "§a" : (level == this.level + 1 ? "§e" : "§c")}Tier \{level}")
                    .build());
        }

        OversizedGui gui = new OversizedGui(item, OversizedGui.StartingPosition.Bottom, "Heart of the Mountain", 8, 53);
        gui.setCancelled(true);
        gui.setClose(45);
        gui.addStatic(46, TemplateItems.EmptySlot.getItem());
        gui.addStatic(47, TemplateItems.EmptySlot.getItem());
        gui.addStatic(48, TemplateItems.EmptySlot.getItem());
        gui.addStatic(49, buildHotmDiplayItem());
        gui.addStatic(50, TemplateItems.EmptySlot.getItem());
        gui.addStatic(51, TemplateItems.EmptySlot.getItem());
        gui.addStatic(52, TemplateItems.EmptySlot.getItem());
        gui.setGeneralClickEvent(event -> {
            if (event.getSlot() >= 46) return true;
            int slot = event.getSlot() + (9 * gui.getIndex());
            for (int i = 0; i < HotmUpgradeSlots.length; i++) {
                if (slot == HotmUpgradeSlots[i]) {
                    HotmUpgrade upgrade = upgrades.get(i);
                    if (upgrade.getLevel() == 0) {
                        if (upgrade.levelRequirement() > level) {
                            player.sendMessage("§cYour Heart of the Mountain level is too low!");
                        } else if (tokenOfTheMountain == 0) {
                            player.sendMessage("§cYou do not have enough Token of the Mountains!");
                        } else {
                            boolean b = upgrade.getPriorUpgrades().length == 0;
                            for (Class<? extends HotmUpgrade> uClass : upgrade.getPriorUpgrades())
                                if (getUpgrade(uClass).level > 0) {
                                    b = true;
                                    break;
                                }
                            if (b) {
                                upgrade.level++;
                                tokenOfTheMountain--;
                                updateAfterUpgrade(upgrade, slot, gui);
                            } else {
                                player.sendMessage("§cYou do not have the prior upgrades");
                            }
                        }
                    } else if (upgrade.level >= upgrade.getMaxLevel()) {
                        player.sendMessage("§cYou have already purchased this upgrade!");
                    } else {
                        boolean shift = event.getClickType() == ClickType.START_SHIFT_CLICK;
                        int cost = 0;
                        if (shift) {
                            int levels = Math.min(upgrade.getMaxLevel() - upgrade.getLevel(), 10);
                            for (int l = upgrade.level ; l < upgrade.level + levels; l++)
                                cost += upgrade.nextLevelCost(l);
                        } else cost =  upgrade.nextLevelCost(upgrade.getLevel());
                        if (player.getPowder(upgrade.upgradeType(upgrade.level)) >= cost) {
                            player.removePowder(upgrade.upgradeType(upgrade.level), cost);
                            upgrade.level += shift ? Math.min(upgrade.getMaxLevel() - upgrade.getLevel(), 10) : 1;
                            updateAfterUpgrade(upgrade, slot, gui);
                        } else {
                            player.sendMessage(STR."§cYou dont have enough \{upgrade.upgradeType(upgrade.level).getName()} Powder!");
                        }
                    }
                    return true;
                }
            }
            return true;
        });
        gui.showGui(player);
    }

    private ItemStack buildHotmDiplayItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setName("§5Heart of the Mountain")
                .addAllLore(STR."§7Token of the Mountain: §5\{tokenOfTheMountain}",
                        " ",
                        "§8Use §5Token of the Mountain",
                        "§8to unlock perks and abilities",
                        "§8above!",
                        "  ",
                        "§9᠅ Powder",
                        "§9Powders §8are dropped from",
                        "§8mining ores in the §2Dwarven",
                        "§2Mines §8and are used to upgrade",
                        "§8the perks you've unlocked!",
                        "   ",
                        STR."§7Mithril Powder: §2\{player.getPowder(Powder.MithrilPowder)}",
                        STR."§7Gemstone Powder: §d\{player.getPowder(Powder.GemstonePowder)}",
                        STR."§7Glacial Powder: §b\{player.getPowder(Powder.GlacialPowder)}")
                .setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODZmMDZlYWEzMDA0YWVlZDA5YjNkNWI0NWQ5NzZkZTU4NGU2OTFjMGU5Y2FkZTEzMzYzNWRlOTNkMjNiOWVkYiJ9fX0=")
                .build();
    }

    private void updateAfterUpgrade(HotmUpgrade upgrade, int slot, OversizedGui gui) {
        ItemStack item = upgrade.getItem();
        gui.getInventory().setItemStack(slot - gui.getIndex() * 9, item);
        int row = (int) (slot / 9d);
        gui.getItems().get(row).set(slot - row * 9, upgrade.getItem());
        gui.addStatic(49, buildHotmDiplayItem());
    }
}
