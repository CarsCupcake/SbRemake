package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.GetItemStatEvent;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.impl.bow.Shortbow;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A Wrapper for an ItemStack to use some Sb features
 *
 * @param item   the item that gets wrapped
 * @param sbItem the Sb item
 */
public record SbItemStack(@NotNull ItemStack item, @NotNull ISbItem sbItem) {

    private static final Map<String, ISbItem> items = new HashMap<>();

    public static void initSbItem(ISbItem item) {
        items.put(item.getId(), item);
    }

    public static ISbItem raw(String id) {
        return items.get(id);
    }

    public static SbItemStack from(Class<? extends ISbItem> itemClass) {
        return ISbItem.get(itemClass).create();
    }

    public static @Nullable SbItemStack from(String id) {
        ISbItem item = items.get(id);
        if (item == null) return null;
        return item.create();
    }

    public static SbItemStack from(ItemStack stack) {
        if (stack == null || stack.material() == Material.AIR) return null;
        CompoundBinaryTag compound = (CompoundBinaryTag) stack.getTag(Tag.NBT("ExtraAttributes"));
        if (!compound.keySet().contains("id")) return base(stack.material());
        ISbItem iSbItem = items.get(compound.getString("id"));
        if (iSbItem == null) return base(stack.material());
        return new SbItemStack(stack, iSbItem);
    }

    public static SbItemStack base(Material material) {
        ISbItem item = items.get(material.namespace().value().toUpperCase());
        return item.create();
    }

    public SbItemStack update() {
        return update(null);
    }

    public SbItemStack update(@Nullable SkyblockPlayer player) {
        if (!sbItem.allowUpdates()) return this;
        List<Component> lore = new ArrayList<>();
        for (String s : buildLore(player))
            lore.add(Component.text(s));
        return new SbItemStack(item.with(ItemComponent.LORE, lore).with(ItemComponent.CUSTOM_NAME, Component.text(getRarity().getPrefix() + displayName())), sbItem);
    }

    public String displayName() {
        //TODO Implement Modifiers
        return sbItem.getName();
    }

    /*
    Lore Placement:

    Top Row Lore
    Stats
    Gemstones
    --- (row if trl/stats/gemstones are present)
    Above Ability Lore
    --- (if aal is present)
    Ability Lore(s)
    --- (if ability is present)
    Below Ability Lore
    --- (if bal is present)
    Other info (shortbow etc)
    --- (if other info is present)
    Reforgable (only shows if no reforge is applied)
    Soulbound or not
    Rarity - Type String
     */
    private static final Stat[] redStats = {Stat.Damage, Stat.Strength, Stat.CritChance, Stat.CritDamage, Stat.AttackSpeed};
    private static final Stat[] greenStats = {Stat.SwingRange, Stat.Health, Stat.Defense, Stat.Speed, Stat.Intelligence, Stat.MagicFind, Stat.PetLuck, Stat.TrueDefense, Stat.Ferocity, Stat.MiningSpeed, Stat.Pristine, Stat.MiningFortune, Stat.FarmingFortune, Stat.WheatFortune, Stat.CarrotFortune, Stat.PotatoFortune, Stat.PumpkinFortune, Stat.MelonFortune, Stat.MushroomFortune, Stat.CactusFortune, Stat.SugarCaneFortune, Stat.NetherWartFortune, Stat.CocoaBeansFortune, Stat.ForagingFortune, Stat.SeaCreatureChance, Stat.FishingSpeed, Stat.Vitality, Stat.Mending};

    public List<String> buildLore(@Nullable SkyblockPlayer player) {
        boolean space = false;
        ItemRarity rarity = getRarity();
        List<String> lore = new ArrayList<>();
        if (sbItem.getLorePlacement() == ISbItem.LorePlace.Top) {
            space = true;
            lore.addAll(sbItem.getLore().build(this, player));

        }
        if (sbItem.statsReplacement() != null) {
            lore.addAll(Objects.requireNonNull(sbItem.statsReplacement()).build(this, player));
            space = true;
        } else {
            double breakingPower = getStat(Stat.BreakingPower);
            if (breakingPower > 0) {
                lore.add(STR."§8Breaking Power \{StringUtils.cleanDouble(breakingPower, 1)}");
                lore.add("§8  ");
            }

            for (Stat stat : redStats) {
                double value = getStat(stat);
                if (value == 0) continue;
                lore.add(STR."§7\{stat.getName()} §c\{(value < 0) ? "" : "+"}\{StringUtils.cleanDouble(value, 1)}\{(stat.isPercentValue()) ? "%" : ""}");
                space = true;
            }
            for (Stat stat : greenStats) {
                double value = getStat(stat);
                if (value == 0) continue;
                lore.add(STR."§7\{stat.getName()} §a\{(value < 0) ? "" : "+"}\{StringUtils.cleanDouble(value, 1)}\{(stat.isPercentValue()) ? "%" : ""}");
                space = true;
            }
            if (sbItem instanceof Shortbow shortbow) {
                lore.add(STR."§7Shot Cooldown: §a\{StringUtils.cleanDouble(((player == null) ? shortbow.getShortbowCooldown(getStat(Stat.AttackSpeed)) : shortbow.getShortbowCooldown(player.getStat(Stat.AttackSpeed, true))) / 1000d)}s");
            }
        }
        //Todo Gemstones
        if (space) {
            space = false;
            lore.add(" ");
        }
        if (sbItem.getLorePlacement() == ISbItem.LorePlace.AboveAbility && sbItem.getLore() != Lore.EMPTY) {
            lore.addAll(sbItem.getLore().build(this, player));
            lore.add("  ");
        }
        for (Ability ability : getAbilities()) {
            if (ability.showInLore()) {
                lore.addAll(ability.buildLore(this, player));
                lore.add("§a  ");
            }
        }
        if (sbItem.getLorePlacement() == ISbItem.LorePlace.BelowAbility && sbItem.getLore() != Lore.EMPTY) {
            lore.addAll(sbItem.getLore().build(this, player));
            lore.add("  ");
        }
        if (sbItem instanceof Shortbow) {
            lore.add(STR."\{rarity.getPrefix()}Shortbow: Instatntly shoots!");
            lore.add("  ");
        }
        if (sbItem.getType().isReforgable()) /* Todo add reforge check*/ {
            lore.add("§8This item can be reforged!");
        }
        lore.add(STR."\{rarity.getPrefix()}§l\{rarity.getDisplay().toUpperCase()} \{sbItem.getType().getDisplay().toUpperCase()}");
        return lore;
    }

    public ItemRarity getRarity() {
        //TODO add custom rarity getter
        return sbItem.getRarity();
    }

    public double getStat(Stat stat) {
        GetItemStatEvent event = new GetItemStatEvent(this, stat, sbItem.getStat(stat));
        MinecraftServer.getGlobalEventHandler().call(event);
        return event.getValue() * event.getMultiplier();
    }

    public static Set<String> getIds() {
        return items.keySet();
    }

    public List<Ability> getAbilities() {
        //Later add stuff like hype ability
        return sbItem.getDefaultAbilities();
    }

    public SbItemStack withAmount(int i) {
        if (i <= 0) return null;
        return new SbItemStack(item.withAmount(i), sbItem);
    }
}
