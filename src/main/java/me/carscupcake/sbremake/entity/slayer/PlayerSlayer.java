package me.carscupcake.sbremake.entity.slayer;

import com.google.gson.JsonObject;
import lombok.Getter;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.item.modifiers.enchantment.SkyblockEnchantment;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.item.Gui;
import me.carscupcake.sbremake.util.item.InventoryBuilder;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterEntry;
import me.carscupcake.sbremake.util.lootTable.rngMeter.SlayerRngMeter;
import net.minestom.server.tag.Tag;

import java.util.Map;

@Getter
public class PlayerSlayer {
    private final SkyblockPlayer player;
    private final ISlayer slayer;
    private int level = 0;
    private int xp;
    private final SlayerRngMeter meter;

    public PlayerSlayer(SkyblockPlayer player, ISlayer slayer) {
        this.player = player;
        this.slayer = slayer;
        ConfigSection file = new ConfigFile("slayer", player).get(slayer.getId(), ConfigSection.SECTION, new ConfigSection(new JsonObject()));
        this.xp = file.get("xp", ConfigSection.INTEGER, 0);
        for (int i = 0; i < slayer.getMaxLevel(); i++) {
            if (slayer.requiredXp(i) <= xp) level++;
        }
        meter = slayer.createRngMeter(player);
    }


    public void addXp(int xp) {
        this.xp += xp;
        while (level < slayer.getMaxLevel() && this.xp >= slayer.requiredXp(level)) {
            level++;
            //TODO level up message
        }
    }

    public void save(ConfigFile f) {
        ConfigSection section = new ConfigSection(new JsonObject());
        section.set("xp", xp, ConfigSection.INTEGER);
        f.set(slayer.getId(), section, ConfigSection.SECTION);
        meter.save();
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
