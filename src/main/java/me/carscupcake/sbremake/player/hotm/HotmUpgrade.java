package me.carscupcake.sbremake.player.hotm;

import lombok.Getter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.impl.MiningFortune;
import me.carscupcake.sbremake.player.hotm.impl.MiningSpeed;
import me.carscupcake.sbremake.player.hotm.impl.MiningSpeedBoost;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

@Getter
@SuppressWarnings({"preview", "unchecked", "unused"})
public abstract class HotmUpgrade {
    public static final EventNode<Event> LISTENER = EventNode.all("hotmUpgrades").addListener(PlayerStatEvent.class, event -> {
        if (event.stat() == Stat.MiningSpeed) {
            MiningSpeed upgrade = event.player().getHotm().getUpgrade(MiningSpeed.class);
            if (upgrade.level > 0)
                event.modifiers().add(new PlayerStatEvent.BasicModifier(upgrade.getName(), upgrade.getBonus(upgrade.level), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Hotm ));
            HotmUpgrade u = event.player().getHotm().getUpgrades().get(9);
            if (u.level >= 1)
                event.modifiers().add(new PlayerStatEvent.BasicModifier(upgrade.getName(), 50, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Hotm ));


        }
        if (event.stat() == Stat.MiningFortune) {
            MiningFortune upgrade = event.player().getHotm().getUpgrade(MiningFortune.class);
            if (upgrade.level > 0)
                event.modifiers().add(new PlayerStatEvent.BasicModifier(upgrade.getName(), upgrade.getBonus(upgrade.level), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Hotm ));
            HotmUpgrade u = event.player().getHotm().getUpgrades().get(9);
            if (u.level >= 1)
                event.modifiers().add(new PlayerStatEvent.BasicModifier(upgrade.getName(), 50, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Hotm ));

        }
    });
    private final Class<? extends HotmUpgrade>[] priorUpgrades;
    //Level 0 -> not unlocked!
    protected int level;
    private final SkyblockPlayer player;

    @SafeVarargs
    public HotmUpgrade(SkyblockPlayer player, Class<? extends HotmUpgrade>... priorUpgrades) {
        this.priorUpgrades = (priorUpgrades == null) ? new Class[0] : priorUpgrades;
        this.player = player;
        ConfigFile file = new ConfigFile("hotm", player);
        level = file.get(getId(), ConfigSection.INTEGER, 0);
    }

    public void save() {
        ConfigFile file = new ConfigFile("hotm", player);
        file.set(getId(), level, ConfigSection.INTEGER);
        file.save();
    }

    public ItemStack getItem() {
        return new ItemBuilder((level == 0) ? Material.COAL : (level == getMaxLevel() ? Material.DIAMOND : Material.EMERALD)).setName(STR."\{level == 0 ? "§c" : (level == getMaxLevel() ? "§a" : "§e")}\{getName()}").addAllLore(STR."§7Level \{level == 0 ? STR."1§8/\{getMaxLevel()}" : (level == getMaxLevel()) ? String.valueOf(getMaxLevel()) : STR."\{level}§8/\{getMaxLevel()}"}", "§7 ").addAllLore(lore((level == 0) ? 1 : level).build(null, player)).addLoreRow("§8 ").addLoreIf(() -> level == 0, "§7Cost", "§51 Token of the Mountain").addLoreIf(() -> level != getMaxLevel() && level != 0, "§a=====[UPGRADE]=====").addLoreIf(() -> level != getMaxLevel() && level != 0, lore(level + 1).build(null, player)).addLoreIf(() -> level != getMaxLevel() && level != 0, "§7Cost", STR."\{upgradeType(level).getColor()}\{nextLevelCost(level)} \{upgradeType(level).getName()}").build();
    }

    public abstract String getName();

    public abstract int getMaxLevel();

    public abstract int nextLevelCost(int current);

    public abstract Powder upgradeType(int current);

    public abstract String getId();

    public abstract Lore lore(int level);

    public abstract int levelRequirement();
}
