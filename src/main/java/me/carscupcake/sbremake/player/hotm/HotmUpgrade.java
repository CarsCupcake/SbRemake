package me.carscupcake.sbremake.player.hotm;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.config.ConfigField;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.config.DefaultConfigItem;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.impl.MiningFortune;
import me.carscupcake.sbremake.player.hotm.impl.MiningFortune2;
import me.carscupcake.sbremake.player.hotm.impl.MiningSpeed;
import me.carscupcake.sbremake.player.hotm.impl.MiningSpeed2;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

@Getter
@SuppressWarnings({"unchecked", "unused"})
public abstract class HotmUpgrade implements DefaultConfigItem {
    public static final EventNode<Event> LISTENER = EventNode.all("hotmUpgrades").addListener(PlayerStatEvent.class, event -> {
        if (event.stat() == Stat.MiningSpeed) {
            MiningSpeed upgrade = event.player().getHotm().getUpgrade(MiningSpeed.class);
            if (upgrade.level > 0 && upgrade.isEnabled())
                event.modifiers().add(new PlayerStatEvent.BasicModifier(upgrade.getName(), upgrade.getBonus(upgrade.level), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Hotm));
            HotmUpgrade u = event.player().getHotm().getUpgrades().get(9);
            if (u.level >= 1 && u.isEnabled())
                event.modifiers().add(new PlayerStatEvent.BasicModifier(u.getName(), 50, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Hotm));
            MiningSpeed2 miningSpeed2 = (MiningSpeed2) event.player().getHotm().getUpgrades().get(26);
            if (miningSpeed2.level > 0 && miningSpeed2.isEnabled())
                event.modifiers().add(new PlayerStatEvent.BasicModifier(miningSpeed2.getName(), miningSpeed2.getBonus(upgrade.level), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Hotm));


        }
        if (event.stat() == Stat.MiningFortune) {
            MiningFortune upgrade = event.player().getHotm().getUpgrade(MiningFortune.class);
            if (upgrade.level > 0 && upgrade.isEnabled())
                event.modifiers().add(new PlayerStatEvent.BasicModifier(upgrade.getName(), upgrade.getBonus(upgrade.level), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Hotm));
            HotmUpgrade u = event.player().getHotm().getUpgrades().get(9);
            if (u.level >= 1 && u.isEnabled())
                event.modifiers().add(new PlayerStatEvent.BasicModifier(upgrade.getName(), 50, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Hotm));
            MiningFortune2 miningFortune2 = (MiningFortune2) event.player().getHotm().getUpgrades().get(28);
            if (miningFortune2.level > 0 && miningFortune2.isEnabled())
                event.modifiers().add(new PlayerStatEvent.BasicModifier(miningFortune2.getName(), miningFortune2.getBonus(upgrade.level), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Hotm));


        }
    });
    private final Class<? extends HotmUpgrade>[] priorUpgrades;
    //Level 0 -> not unlocked!
    @ConfigField
    protected int level;
    private final SkyblockPlayer player;
    @Setter
    @ConfigField
    private boolean enabled;

    @SafeVarargs
    public HotmUpgrade(SkyblockPlayer player, Class<? extends HotmUpgrade>... priorUpgrades) {
        this.priorUpgrades = (priorUpgrades == null) ? new Class[0] : priorUpgrades;
        this.player = player;
        ConfigFile file = new ConfigFile("hotm", player);
        ConfigSection section = file.get(getId(), ConfigSection.SECTION, new ConfigSection(new JsonObject()));
        level = section.get("level", ConfigSection.INTEGER, 0);
        enabled = section.get("enabled", ConfigSection.BOOLEAN, true);
    }

    public void save(ConfigFile file) {
        ConfigSection section = file.get(getId(), ConfigSection.SECTION, new ConfigSection(new JsonObject()));
        section.set("level", level, ConfigSection.INTEGER);
        section.set("enabled", enabled, ConfigSection.BOOLEAN);
        file.set(getId(), section, ConfigSection.SECTION);
    }

    public ItemStack getItem() {
        return new ItemBuilder((level == 0) ? Material.COAL : (level == getMaxLevel() ? Material.DIAMOND : Material.EMERALD)).setName((level == 0 ? "§c" : (level == getMaxLevel() ? "§a" : "§e")) + (getName())).addAllLore("§7Level " + (level == 0 ? "1§8/" + (getMaxLevel()) : (level == getMaxLevel()) ? String.valueOf(getMaxLevel()) : (level) + "§8/" + (getMaxLevel())), "§7 ").addAllLore(lore((level == 0) ? 1 : level).build(null, player))
                .addLoreIf(() -> level != getMaxLevel(), " ")
                .addLoreIf(() -> level == 0, "§7Cost", "§51 Token of the Mountain")
                .addLoreIf(() -> level != getMaxLevel() && level != 0, "§a=====[UPGRADE]=====")
                .addLoreIf(() -> level != getMaxLevel() && level != 0, lore(level + 1).build(null, player))
                .addLoreIf(() -> level != getMaxLevel() && level != 0, " ", "§7Cost", (upgradeType(level).getColor()) + (StringUtils.toFormatedNumber(nextLevelCost(level))) + " " + (upgradeType(level).getName()))
                .addAllLore(" ", enabled ? "§a§lENABLED" : "§c§lDISABLED")
                .build();
    }

    public void refund() {
        if (level > 0) {
            player.getHotm().addTokenOfTheMountain();
            for (int i = 1; i < level; i++)
                player.addPowder(upgradeType(i), nextLevelCost(i));
            enabled = true;
        }
    }

    public abstract String getName();

    public abstract int getMaxLevel();

    public abstract int nextLevelCost(int current);

    public abstract Powder upgradeType(int current);

    public abstract String getId();

    public abstract Lore lore(int level);

    public abstract int levelRequirement();
}
