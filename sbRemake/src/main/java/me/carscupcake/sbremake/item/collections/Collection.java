package me.carscupcake.sbremake.item.collections;

import lombok.Getter;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.player.xp.SkyblockXpTask;
import me.carscupcake.sbremake.rewards.Reward;
import me.carscupcake.sbremake.rewards.impl.SkyblockXpReward;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.item.Gui;
import me.carscupcake.sbremake.util.item.InventoryBuilder;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Collection implements SkyblockXpTask {
    private final SkyblockPlayer player;
    private long progress;
    private final int[] levelProgress;
    private final List<List<Reward>> rewards;
    private final int maxLevel;
    private int level;

    public Collection(SkyblockPlayer player, int[] levels, List<List<Reward>> rewards) {
        this.player = player;
        this.levelProgress = levels;
        this.rewards = rewards;
        this.maxLevel = levels.length;
        Assert.assertEquals(levels.length, rewards.size());
        ConfigFile configFile = new ConfigFile("collections", player);
        progress = configFile.get(getId(), ConfigSection.LONG, 0L);
        level = 0;
        while (level < getMaxLevel() && levelProgress[level] <= progress) {
            level++;
        }
    }

    public void addProgress(int i) {
        if (i <= 0) return;
        progress += i;
        while (level < getMaxLevel() && levelProgress[level] <= progress) {
            level++;
            levelUp(level);
        }
    }

    public abstract Material showItem();

    private static final int[][] slots = {{22}, {21, 23}, {21, 22, 23}, {20, 21, 23, 24}, {20, 21, 22, 23, 24}, {19, 20, 21, 23, 24, 25}, {19, 20, 21, 22, 23, 24, 25}, {18, 19, 20, 21, 23, 24, 25, 26}};

    public void showInventory() {
        InventoryBuilder builder = new InventoryBuilder(6, (getName()) + " Collection")
                .fill(TemplateItems.EmptySlot.getItem())
                .setItem(new ItemBuilder(showItem()).setName("§e" + (getName()) + " " + (StringUtils.toRoman(level)))
                        .addAllLore("§7View all your " + (getName()) + " Collection", "§7progress and rewards!", "§7 ", "§7Total Collected: §e" + (StringUtils.toFormatedNumber(progress)))
                        .build(), 4);
        if (maxLevel < slots.length) {
            for (int i = 0; i < maxLevel; i++) {
                builder.setItem(getCollectionShowItem(i + 1), slots[maxLevel][i]);
            }
        } else {
            for (int i = 0; i < maxLevel; i++) {
                builder.setItem(getCollectionShowItem(i + 1), 18 + i);
            }
        }
        Gui gui = new Gui(builder.setItem(new ItemBuilder(Material.BARRIER).setName("§cClose").build(), 49).build());
        gui.setCancelled(true);
        gui.showGui(player);
    }

    public ItemStack getCollectionShowItem(int level) {
        String prefix = (level <= this.level) ? "§a" : "§e";
        return new ItemBuilder((level <= this.level) ? Material.LIME_STAINED_GLASS_PANE : (level == 1 + this.level) ? Material.YELLOW_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE)
                .setName((prefix) + (getName()) + " " + (StringUtils.toRoman(level)))
                .addAllLore("§a ", "§7Progress: " + (prefix) + (StringUtils.cleanDouble(100 * Math.min(1d, (double) progress / levelProgress[level - 1]))) + "%")
                .addLoreRow(StringUtils.makeProgressBar(20, progress, levelProgress[level - 1], NamedTextColor.WHITE, NamedTextColor.GREEN,
                        "§m ").append(Component.text(" §e" + (progress) + " §6/ §e" + (StringUtils.toShortNumber(levelProgress[level - 1])))))
                .addLoreRow("§e ")
                .addLoreRow("§aRewards:")
                .addAllLore(rewardsLore(level))
                .setAmount(level)
                .build();
    }

    public void levelUp(int level) {
        List<Reward> rewards = getRewards().get(level - 1);
        for (Reward reward : rewards)
            reward.reward(player);
        player.sendMessage("§e▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        player.sendMessage(Component.text("  §6§lCOLLECTION LEVELED UP!§r §e" + (getName()) + " §e" + (StringUtils.toRoman(level))).clickEvent(ClickEvent.runCommand("/collectionmenu " + (getId()))).hoverEvent(Component.text("§eClick to open")));
        player.sendMessage("  ");
        player.sendMessage("  §a§lREWARDS");
        for (String s : rewardsLore(level)) {
            player.sendMessage("   " + (s));
        }
        player.sendMessage("§e▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }

    public void save() {
        ConfigFile configFile = new ConfigFile("collections", player);
        configFile.set(getId(), progress, ConfigSection.LONG);
        configFile.save();
    }

    public List<String> rewardsLore(int level) {
        List<String> lore = new ArrayList<>();
        getRewards().get(level - 1).forEach((reward) -> lore.addAll(reward.lore().build(null, player)));
        return lore;
    }

    public abstract String getId();

    public abstract String getName();

    /**
     * Progress for this collection
     *
     * @param item the item
     * @return the amount of progress 0 = not in the collection
     */
    public abstract int progress(ISbItem item);

    @Override
    public long getMaxXp() {
        return getXpForLeve(maxLevel);
    }

    private long getXpForLeve(int level) {
        var sbXp = 0L;
        for (int i = 1; i <= level; i++) {
            for (var reward : getRewards().get(i - 1)) {
                if (reward instanceof SkyblockXpReward(var xp)) {
                    sbXp += xp;
                }
            }
        }
        return sbXp;
    }

    @Override
    public long getTotalXp() {
        return getXpForLeve(level);
    }
}
