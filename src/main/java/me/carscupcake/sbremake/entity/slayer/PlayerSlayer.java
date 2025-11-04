package me.carscupcake.sbremake.entity.slayer;

import lombok.Getter;
import me.carscupcake.config.Constants;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.config.ConfigField;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.config.DefaultConfigItem;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.impl.other.slayer.MaddoxBatphone;
import me.carscupcake.sbremake.item.modifiers.enchantment.SkyblockEnchantment;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.gui.Gui;
import me.carscupcake.sbremake.util.gui.InventoryBuilder;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterEntry;
import me.carscupcake.sbremake.util.lootTable.rngMeter.SlayerRngMeter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

import java.util.Map;

@Getter
public class PlayerSlayer implements DefaultConfigItem {
    private final SkyblockPlayer player;
    private final ISlayer slayer;
    private int level = 0;
    @ConfigField
    private int xp;
    @ConfigField
    private final SlayerRngMeter meter;

    public PlayerSlayer(SkyblockPlayer player, ISlayer slayer, ConfigSection section) {
        this.player = player;
        this.slayer = slayer;
        this.xp = section.get("xp", ConfigSection.INTEGER, 0);
        for (int i = 0; i < slayer.getMaxLevel(); i++) {
            if (slayer.requiredXp(i) <= xp) level++;
        }
        meter = slayer.createRngMeter(player, section.get("meter", ConfigSection.SECTION, ConfigSection.empty()));
    }


    public void addXp(int xp) {
        this.xp += xp;
        while (level < slayer.getMaxLevel() && this.xp >= slayer.requiredXp(level)) {
            level++;
            //TODO level up message
        }
    }
    public void resetXp() {
        this.xp = 0;
        this.level = 0;
    }
    public void subtractXp(int exp) {
        this.xp -= exp;
        int xp = this.xp;
        level = 0;
        while (level < slayer.getMaxLevel() && xp >= slayer.requiredXp(level)) {
            level++;
        }
    }
    public void setXp(int exp) {
        this.xp = exp;
        int xp = this.xp;
        level = 0;
        while (level < slayer.getMaxLevel() && xp >= slayer.requiredXp(level)) {
            level++;
        }
    }

    public void openSlayerMenu() {
        var lore =  slayer.getAbilityLore();
        var slayernames = slayer.getSlayerGuiNames();
        var builder = new InventoryBuilder(6, slayer.getMobName())
                .fill(TemplateItems.EmptySlot.getItem());
        if (lore.size() != 5)
            builder.setItem(new ItemBuilder(Material.COAL_BLOCK)
                            .setName("§5" + slayer.getMobName())
                            .addLore("This excrutiating difficult boss will be added later!")
                    .build(), 15);
        for (int i = 0; i < lore.size(); i++) {
            var item = new ItemBuilder(slayer.getMaterial())
                    .setName("§5" + slayer.getMobName() + " " + StringUtils.toRoman(i + 1))
                    .addLoreRow("§7" + slayernames[i])
                    .addAllLore(" ",
                            "§7Health: §c" + StringUtils.toFormatedNumber(slayer.getHealth(i+1)) + Stat.Health.getSymbol(),
                            "§7Damage: §c" + StringUtils.toFormatedNumber(slayer.getDamage(i+1)) + " §7per second",
                            " ")
                    .addAllLore(lore.get(i).build(null, player))
                    .addAllLore(" ",
                            "§7Rewards: §d" + slayer.getSlayerXp(i+1) + " " + slayer.getName() + " Slayer XP",
                            " §8+ Boss Drops",
                            " ");
                    item.setAmount(i+1);
                    if (player.getSlayerQuest() == null) {
                        item.addAllLore("§7Cost to start: §6" + StringUtils.toFormatedNumber(slayer.getSlayerCost(i+1)) + " Coins",
                                " ",
                                "§eClick to slay!");
                    } else {
                        item.addAllLore("§cYou already have a Slayer Quest active!", "§cCancel this first!");
                    }
            builder.setItem(item.build(), i + 11);
        }
        builder.setItem(34, getRngMeterDisplayItem());
        builder.setItem(49, TemplateItems.Close.getItem());
        builder.setItem(48, TemplateItems.BackArrow.getItem());
        var gui = new Gui(builder.build());
        gui.getClickEvents().add(48, _ -> {
            MaddoxBatphone.open(player);
            return true;
        });
        gui.getClickEvents().add(49, _ -> {
            player.closeGui();
            return true;
        });
        gui.getClickEvents().add(34, _ -> {
            openRngMeterMenu();
            return true;
        });
        if (player.getSlayerQuest() == null) {
            gui.getClickEvents().add(11, _ -> {
                showConfirmationPrompt(1);
                return true;
            });
            gui.getClickEvents().add(12, _ -> {
                showConfirmationPrompt(2);
                return true;
            });
            gui.getClickEvents().add(13, _ -> {
                showConfirmationPrompt(3);
                return true;
            });
            gui.getClickEvents().add(14, _ -> {
                showConfirmationPrompt(4);
                return true;
            });
            if (lore.size() == 5) {
                gui.getClickEvents().add(15, _ -> {
                    showConfirmationPrompt(5);
                    return true;
                });
            }
        }
        gui.setCancelled(true);
        gui.showGui(player);
    }

    public ItemStack getRngMeterDisplayItem() {
        var rngMeterItem = new ItemBuilder(slayer.getMaterial())
            .setName("§dRNG Meter")
            .setLore(new Lore(Constants.SLAYER.RNGMETER_LORE, Map.of("%mobname%", (_, _) -> slayer.getMobName(),
                    "%slayer%", (_, _) -> slayer.getName())))
            .addLoreRow(" ");
        var rngmeteritem = meter.getSelected();
        if (rngmeteritem != null) {
            var name = rngmeteritem.loots()[0].previewItem().getRarity().getPrefix() +  rngmeteritem.loots()[0].previewItem().displayName();
            var percentage = (meter.getRngMeterXp() / meter.getRequiredXp(rngmeteritem.loots()[0])) * 100;
            rngMeterItem.addAllLore("§7Selected Drop","§f" + name, " ",
                    "§7Progress: §d"
                            + StringUtils.cleanDouble(percentage, 2) + "§5%", StringUtils.makeProgressBarAsString(25, percentage, 100d, "§f", "§d", "§m ") + "§r §d"
                            + StringUtils.toFormatedNumber(meter.getRngMeterXp()) + "§5/" + StringUtils.toShortNumber(meter.getRequiredXp(rngmeteritem.loots()[0])));
        } else {
            rngMeterItem.addLore("""
                    §cYou don't have an RNG drop selected. Choose one to start progressing towards it!
                    
                    §7Stored Slayer XP: §d
                    """ + StringUtils.toFormatedNumber(meter.getRngMeterXp()));
        }
        rngMeterItem.addAllLore(" ", "§eClick to view!");
        return rngMeterItem.build();
    }

    private void showConfirmationPrompt(int tier) {
        var gui = new Gui(new InventoryBuilder(3, "Confirm")
                .fill(TemplateItems.EmptySlot.getItem())
                .setItem(11, new ItemBuilder(Material.LIME_TERRACOTTA)
                        .setName("§aConfirm")
                        .build())
                .setItem(15, new ItemBuilder(Material.RED_TERRACOTTA)
                        .setName("§cCancel")
                        .build())
                .build());
        gui.setCancelled(true);
        gui.getClickEvents().add(11, _ -> {
            player.closeGui();
            slayer.startSlayerQuest(tier, player);
            return true;
        });
        gui.getClickEvents().add(15, _ -> {
            openSlayerMenu();
            return true;
        });
        gui.showGui(player);
    }

    public void openRngMeterMenu() {
        InventoryBuilder builder = new InventoryBuilder(6, "Rng Meter").fill(TemplateItems.EmptySlot.getItem(), 0, 8).fill(TemplateItems.EmptySlot.getItem(), 45, 53).setItem(TemplateItems.EmptySlot.getItem(), 9).setItem(TemplateItems.EmptySlot.getItem(), 18).setItem(TemplateItems.EmptySlot.getItem(), 27).setItem(TemplateItems.EmptySlot.getItem(), 36).setItem(TemplateItems.EmptySlot.getItem(), 8).setItem(TemplateItems.EmptySlot.getItem(), 17).setItem(TemplateItems.EmptySlot.getItem(), 26).setItem(TemplateItems.EmptySlot.getItem(), 35).setItem(TemplateItems.EmptySlot.getItem(), 44);
        int i = 10;
        for (RngMeterEntry meterEntry : slayer.getRngMeterEntries()) {
            ItemBuilder itemBuilder = new ItemBuilder(meterEntry.loots()[meterEntry.loots().length - 1].previewItem().item());
            itemBuilder.clearLore();
            Map<SkyblockEnchantment, Integer> enchantmentIntegerMap = meterEntry.loots()[0].previewItem().getEnchantments();
            if (!enchantmentIntegerMap.isEmpty()) {
                for (Map.Entry<SkyblockEnchantment, Integer> enchantment : enchantmentIntegerMap.entrySet())
                    itemBuilder.addLoreRow("§9" + (enchantment.getKey().getName()) + " " + (StringUtils.toRoman(enchantment.getValue())));
            }
            double chance = meter.getLootTableChances().get(meterEntry);
            String oddMessage;
            if (chance < 0.0003) oddMessage = "§cRNGesus Incarnate";
            else if (chance < 0.006) oddMessage = "§dPray RNGesus";
            else if (chance < 0.03) oddMessage = "§5Extraordinary";
            else if (chance < 0.11) oddMessage = "§bRare";
            else if (chance < 0.31) oddMessage = "§9Occasional";
            else if (chance < 1) oddMessage = "§fCommon";
            else oddMessage = "§aGuaranteed";
            itemBuilder.addAllLore("§c ",
                                   "§7Odds: " + (oddMessage) + " §7(" + (meter.getSelected() == meterEntry ? "§8§m" : "") + (StringUtils.cleanDouble(chance * 100, 4)) + (meter.getSelected() == meterEntry ? "§7 " + (StringUtils.cleanDouble((chance * (1 + Math.min(2, 2 * (meter.getRngMeterXp() / meter.getLootTableGoals().get(meterEntry))))) * 100, 4)) : "§7") + ")", " ", "§d" + (StringUtils.cleanDouble(meter.getRngMeterXp())) + "§5/§d" + (StringUtils.toFormatedNumber(meter.getLootTableGoals().get(meterEntry))));
            builder.setItem(i, itemBuilder.build().withTag(Tag.String("meter_id"), meterEntry.id()));
            i++;
            if ((i + 1) % 9 == 0) i += 2;
        }
        Gui gui = new Gui(builder.build());
        gui.setCancelled(true);
        gui.setGeneralClickEvent(event -> {
            String meter_id = event.getClickedItem().getTag(Tag.String("meter_id"));
            RngMeterEntry entry = meter.findById(meter_id);
            if (entry != null && entry != meter.getSelected()) {
                meter.setSelected(entry);
                openRngMeterMenu();
            }
            return true;
        });
        gui.showGui(player);
    }
}
