package me.carscupcake.sbremake.player.skill;

import com.google.gson.JsonObject;
import lombok.Getter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.event.PlayerSkillXpEvent;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.xp.SkyblockXpTask;
import me.carscupcake.sbremake.rewards.Reward;
import me.carscupcake.sbremake.rewards.impl.CoinReward;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.item.Gui;
import me.carscupcake.sbremake.util.item.InventoryBuilder;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class ISkill implements SkyblockXpTask {
    public static final int[] nextLevelXp = {
            50, 125, 200, 300, 500, 750, 1000, 1500, 2000, 3500, 5000, 7500,
            10000, 15000, 20000, 30000, 50000, 75000, 100000, 200000,
            300000, 400000, 500000, 600000, 700000, 800000, 900000,
            1000000, 1100000, 1200000, 1300000, 1400000, 1500000,
            1600000, 1700000, 1800000, 1900000, 2000000, 2100000,
            2200000, 2300000, 2400000, 2500000, 2600000, 2750000,
            2900000, 3100000, 3400000, 3700000, 4000000, 4300000,
            4600000, 4900000, 5200000, 5500000, 5800000, 6100000,
            6400000, 6700000, 7000000
    };
    private int level;
    private double xp;
    private final String id;
    private final SkyblockPlayer player;
    private final Material showItem;
    private final Material fiveLevelItem;
    private final Lore basicLore;

    public ISkill(SkyblockPlayer player, String id, Material showItem, Material fiveLevelItem, Lore basicLore) {
        this.id = id;
        this.player = player;
        this.showItem = showItem;
        this.fiveLevelItem = fiveLevelItem;
        this.basicLore = basicLore;
        ConfigFile file = new ConfigFile("skills", player);
        ConfigSection section = file.get(id, ConfigSection.SECTION, new ConfigSection(new JsonObject()));
        xp = section.get("xp", ConfigSection.DOUBLE, 0d);
        level = section.get("level", ConfigSection.INTEGER, 0);
    }

    public abstract int getMaxLevel();

    public abstract String getName();

    public abstract Stat getWisdomStat();

    public void addXp(double amount) {
        amount = calculateXp(amount);
        if (player.getPet() != null) {
            player.getPet().addXp(player.getPet().getPet().getPetType().apply(amount, this.getType()));
        }
        xp += amount;
        while (level < getMaxLevel() && nextLevelXp[level] <= xp) {
            xp -= nextLevelXp[level];
            level++;
            levelUp(level);
        }
        if (getLevel() >= getMaxLevel())
            player.setDefenseString("§3+" + (StringUtils.cleanDouble(amount)) + " " + (getName()));
        else
            player.setDefenseString("§3+" + (StringUtils.cleanDouble(amount)) + " " + (getName()) + " (" + (StringUtils.toFormatedNumber((int) xp)) + "/" + (StringUtils.toShortNumber(nextLevelXp[level])) + ")");
        player.playSound(SoundType.ENTITY_EXPERIENCE_ORB_PICKUP.create(0.5f, 2f));
    }

    public double calculateXp(double amount) {
        double wisdom = getWisdomStat() != null ? (player.getStat(getWisdomStat()) / 100d) : 0;
        PlayerSkillXpEvent event = new PlayerSkillXpEvent(player, this.getType(), amount, wisdom, 1);
        MinecraftServer.getGlobalEventHandler().call(event);
        amount = event.getXp() * event.getMultiplier();
        amount *= 1 + (event.getWisdom());
        return amount;
    }

    public void levelUp(int level) {
        List<Reward> rewards = getRewards(level);
        for (Reward reward : rewards)
            reward.reward(player);
        player.sendMessage("§3▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        player.sendMessage("  §b§lSKILL LEVELED UP!§r §3" + (getName()) + " §8" + (StringUtils.toRoman(level - 1)) + "➜§3" + (StringUtils.toRoman(level)));
        player.sendMessage("  ");
        player.sendMessage("  §a§lREWARDS");
        for (String s : rewardsLore(level, player)) {
            player.sendMessage("   " + (s));
        }
        player.sendMessage("§3▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }

    public boolean isCosmetic() {
        return false;
    }

    public abstract List<Reward> getRewards(int level);

    public List<String> rewardsLore(int level, SkyblockPlayer player) {
        List<String> lore = new ArrayList<>();
        getRewards(level).forEach((reward) -> lore.addAll(reward.lore().build(null, player)));
        return lore;
    }

    public void save() {
        ConfigFile file = new ConfigFile("skills", player);
        ConfigSection section = file.get(id, ConfigSection.SECTION, new ConfigSection(new JsonObject()));
        section.set("xp", xp, ConfigSection.DOUBLE);
        section.set("level", level, ConfigSection.INTEGER);
        file.set(id, section, ConfigSection.SECTION);
        file.save();
    }

    public Skill getType() {
        for (Skill s : Skill.values())
            if (s.getClazz() == this.getClass()) return s;
        throw new IllegalStateException("Should not be possible!");
    }

    public static CoinReward makeCoinReward(int level) {
        if (level == 1) return new CoinReward(100);
        if (level <= 5) return new CoinReward(250 * (level - 1));
        if (level <= 9) return new CoinReward(1_000 * (level - 5));
        if (level == 10) return new CoinReward(7500);
        if (level <= 15) return new CoinReward(5_000 * (level - 10));
        if (level <= 20) return new CoinReward(10_000 * (level - 12));
        if (level <= 35) return new CoinReward(25_000 * (level - 16));
        if (level < 45) return new CoinReward(50_000 * (level - 26));
        return new CoinReward(1_000_000);

    }

    private static final int[] levelSlots = {9, 18, 27, 28, 29, 20, 11, 2, 3, 4, 13, 22, 31, 32, 33, 24, 15, 6, 7, 8, 17, 26, 35, 44, 53};

    public void openInventory(int page, SkyblockPlayer player) {
        InventoryBuilder builder = new InventoryBuilder(6, (getName()) + " Skill")
                .fill(TemplateItems.EmptySlot.getItem());
        for (int i = 0; i < levelSlots.length; i++) {
            int level = ((page - 1) * 25) + i + 1;
            if (level > getMaxLevel() || level > nextLevelXp.length) break;
            var material = ((i + 1) % 5 == 0) ? fiveLevelItem : (this.level >= level) ? Material.LIME_STAINED_GLASS_PANE : ((this.level + 1 == level) ? Material.YELLOW_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
            builder.setItem(new ItemBuilder(material)
                            .setAmount(level > material.maxStackSize() ? 1 : level)
                    .setName(((this.level >= level) ? "§a" : (this.level + 1 == level) ? "§e" : "§c") + (getName()) + " Level " + (StringUtils.toRoman(level)))
                    .addLoreRow("§7Rewards:")
                    .addAllLore(rewardsLore(level, player))
                    .addLoreIf(() -> this.level >= level, "§7  ", "§a§lUNLOCKED")
                    .addLoreIf(() -> this.level + 1 == level && level != 60, Component.text("§7  "), Component.text().append(StringUtils.makeProgressBar(10, xp, this.level == 60 ? -1 : nextLevelXp[this.level], NamedTextColor.WHITE, NamedTextColor.GREEN,
                            "§m "), Component.text("§r §e" + (StringUtils.cleanDouble(xp)) + "§6/§e" + (StringUtils.cleanDouble(nextLevelXp[level == nextLevelXp.length ? 0 : this.level == 60 ? 0 : this.level])))).build())
                    .build(), levelSlots[i]);
        }
        builder.setItem(0, new ItemBuilder(showItem)
                .setName("§a" + (getName()) + " Skill")
                .addAllLore(basicLore.build(null, player))
                .build());
        int status;
        if ((page * 25) <= this.level) {
            status = page + 1;
            builder.setItem(50, new ItemBuilder(Material.ARROW).setName("§aLevels " + ((page * 25) + 1) + " - " + (Math.min((page + 1) * 25, getMaxLevel()))).addLoreRow("§eClick to view!").build());
        } else if (page != 1) {
            status = 1;
            builder.setItem(50, new ItemBuilder(Material.ARROW).setName("§aLevels 1 - 25").addLoreRow("§eClick to view!").build());
        } else {
            status = -1;
        }
        Gui gui = new Gui(builder.build());
        gui.setCancelled(true);
        if (status != -1)
            gui.getClickEvents().add(50, _ -> {
                openInventory(status, player);
                return true;
            });
        gui.showGui(player);
    }

    public static int xpFromLevel(int level) {
        return (level <= 10) ? 5 : ((level <= 25) ? 10 : ((level <= 50) ? 20 : 30));
    }

    @Override
    public long getMaxXp() {
        return getSkyblockXpForLevel(getMaxLevel());
    }

    private long getSkyblockXpForLevel(int level) {
        if (level <= 10) return level * 5L;
        var xp = 50;
        level -= 10;
        if (level <= 25) return xp + level * 10L;
        xp += 150;
        level -= 15;
        if (level <= 50) return xp + level * 20L;
        xp += 500;
        level -= 25;
        return xp + level * 30L;
    }

    @Override
    public long getTotalXp() {
        return getSkyblockXpForLevel(level);
    }
}
