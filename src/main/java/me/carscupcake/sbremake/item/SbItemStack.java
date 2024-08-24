package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.GetItemStatEvent;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.impl.bow.Shortbow;
import me.carscupcake.sbremake.item.impl.other.SkyblockMenu;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantment;
import me.carscupcake.sbremake.item.modifiers.enchantment.SkyblockEnchantment;
import me.carscupcake.sbremake.item.modifiers.reforges.Reforge;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.PickaxeAbility;
import me.carscupcake.sbremake.util.StringUtils;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static java.lang.Math.min;

/**
 * A Wrapper for an ItemStack to use some Sb features
 *
 * @param item   the item that gets wrapped
 * @param sbItem the Sb item
 */
@SuppressWarnings("preview")
public record SbItemStack(@NotNull ItemStack item, @NotNull ISbItem sbItem, @NotNull HashMap<Modifier<?>, Object> modifierCache) {
    public SbItemStack(@NotNull ItemStack item, @NotNull ISbItem sbItem) {
        this(item, sbItem, new HashMap<>());
    }

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
        if (compound == null || !compound.keySet().contains("id")) return base(stack.material()).withAmount(stack.amount());
        ISbItem iSbItem = items.get(compound.getString("id"));
        if (iSbItem == null) return base(stack.material());
        return new SbItemStack(stack, iSbItem);
    }

    @NotNull
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
        EnchantmentList list = new EnchantmentList(new HashMap<>());
        Map<SkyblockEnchantment, Integer> enchantments = getEnchantments();
        if (!enchantments.isEmpty()) {
            list = list.with(Enchantment.PROTECTION, 1);
            if (enchantments.containsKey(NormalEnchantment.Efficiency) && player != null && !player.getWorldProvider().useCustomMining()) {
                list = list.with(Enchantment.EFFICIENCY, min(255, enchantments.get(NormalEnchantment.Efficiency)));
            }
        } else {
            if (sbItem.modifierBuilder().isGlint())
                list = list.with(Enchantment.PROTECTION, 1);

        }
        return new SbItemStack(item.with(ItemComponent.LORE, lore).with(ItemComponent.ENCHANTMENTS, list.withTooltip(false)).with(ItemComponent.CUSTOM_NAME, Component.text(getRarity().getPrefix() + displayName())), sbItem);
    }

    public String displayName() {
        Reforge reforge = getModifier(Modifier.REFORGE);
        return STR."\{reforge != null ? STR."\{reforge.getName()} " : ""}\{sbItem.getName()}";
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
            double breakingPower = getStat(Stat.BreakingPower, player);
            if (breakingPower > 0) {
                lore.add(STR."§8Breaking Power \{StringUtils.cleanDouble(breakingPower, 1)}");
                lore.add("§8  ");
            }

            for (Stat stat : redStats) {
                double value = getStat(stat, player);
                if (value == 0) continue;
                lore.add(STR."§7\{stat.getName()} §c\{statLine(stat, value, player)}");
                space = true;
            }
            for (Stat stat : greenStats) {
                double value = getStat(stat, player);
                if (value == 0) continue;
                lore.add(STR."§7\{stat.getName()} §a\{statLine(stat, value, player)}");
                space = true;
            }
            if (sbItem instanceof Shortbow shortbow) {
                lore.add(STR."§7Shot Cooldown: §a\{StringUtils.cleanDouble(((player == null) ? shortbow.getShortbowCooldown(getStat(Stat.AttackSpeed, player)) : shortbow.getShortbowCooldown(player.getStat(Stat.AttackSpeed, true))) / 1000d)}s");
            }
        }
        //Todo Gemstones
        if (space) {
            lore.add(" ");
        }
        Map<SkyblockEnchantment, Integer> enchantmentIntegerMap = getEnchantments();
        if (!enchantmentIntegerMap.isEmpty()) {
            if (enchantmentIntegerMap.size() <= 6) {
                enchantmentIntegerMap.forEach((skyblockEnchantment, integer) -> {
                    lore.add(STR."§9\{skyblockEnchantment.getName()} \{StringUtils.toRoman(integer)}");
                    lore.addAll(skyblockEnchantment.description().build(this, player));
                });
            } else {
                StringBuilder builder = new StringBuilder("§9");
                int i = 0;
                for (Map.Entry<SkyblockEnchantment, Integer> entry : enchantmentIntegerMap.entrySet()) {
                    builder.append(STR."\{entry.getKey().getName()} \{StringUtils.toRoman(entry.getValue())}");
                    i++;
                    if (i == 3) {
                        i = 0;
                        lore.add(builder.toString());
                        builder = new StringBuilder("§9");
                    } else builder.append(" ");
                }
                if (i != 0) lore.add(builder.toString());
            }
            lore.add(" ");
        }
        if (sbItem.getLorePlacement() == ISbItem.LorePlace.AboveAbility && sbItem.getLore() != Lore.EMPTY) {
            lore.addAll(sbItem.getLore().build(this, player));
            if (sbItem instanceof SkyblockMenu) return lore;
            lore.add("  ");
        }
        for (Ability ability : getAbilities(player)) {
            if (ability.showInLore()) {
                lore.addAll(ability.buildLore(this, player));
                lore.add("§a  ");
            }
        }
        if (sbItem.getLorePlacement() == ISbItem.LorePlace.BelowAbility && sbItem.getLore() != Lore.EMPTY) {
            lore.addAll(sbItem.getLore().build(this, player));
            lore.add("  ");
        }
        Reforge reforge = getModifier(Modifier.REFORGE);
        if (reforge != null && reforge.getLore() != Lore.EMPTY) {
            lore.add(STR."§9\{reforge.getName()} Bonus");
            lore.addAll(reforge.getLore().build(this, player));
            lore.add(" ");
        }
        if (sbItem instanceof Shortbow) {
            lore.add(STR."\{rarity.getPrefix()}Shortbow: Instatntly shoots!");
            lore.add("  ");
        }
        if (sbItem.getType().isReforgable()) /* Todo add reforge check*/ {
            lore.add("§8This item can be reforged!");
        }
        for (Requirement requirement : sbItem.requirements())
            if (!requirement.canUse(player, item))
                lore.add(requirement.display());
        lore.add(STR."\{rarity.getPrefix()}§l\{rarity.getDisplay().toUpperCase()} \{sbItem.getType().getDisplay().toUpperCase()}");
        return lore;
    }

    private String statLine(Stat stat, double value, SkyblockPlayer player) {
        Reforge reforge = getModifier(Modifier.REFORGE);
        double reforgeValue = reforge == null ? 0 : reforge.getStat(stat, getRarity(),player);
        return STR."\{(value < 0) ? "" : "+"}\{StringUtils.cleanDouble(value, 1)}\{(stat.isPercentValue()) ? "%" : ""}\{reforgeValue != 0 ? STR." §9(\{value < 0 ? "" : "+"}\{StringUtils.cleanDouble(reforgeValue, 1)}\{stat.isPercentValue() ? "%" : ""})" : ""}";
    }

    public ItemRarity getRarity() {
        //TODO add custom rarity getter
        return sbItem.getRarity();
    }

    public double getStat(Stat stat, SkyblockPlayer player) {
        Reforge reforge = getModifier(Modifier.REFORGE);
        GetItemStatEvent event = new GetItemStatEvent(this, stat, sbItem.getStat(stat) + (reforge != null ? reforge.getStat(stat, getRarity(), player) : 0));
        MinecraftServer.getGlobalEventHandler().call(event);
        return event.getValue() * event.getMultiplier();
    }

    public static Set<String> getIds() {
        return items.keySet();
    }

    public List<Ability> getAbilities(SkyblockPlayer player) {
        //Later add stuff like hype ability
        List<Ability> abilities = sbItem.getDefaultAbilities();
        if (sbItem.getType() == ItemType.Pickaxe || sbItem.getType() == ItemType.Drill)
            if (player != null) {
                PickaxeAbility ability = player.getHotm().getActiveAbility();
                if (ability != null) abilities.add(ability.getAbility());
            }

        return abilities;
    }


    public SbItemStack withAmount(int i) {
        if (i <= 0) return null;
        if (i == item.amount()) return this;
        return new SbItemStack(item.withAmount(i), sbItem);
    }

    public int getEnchantmentLevel(SkyblockEnchantment enchantment) {
        return getEnchantments().getOrDefault(enchantment, 0);
    }

    public Map<SkyblockEnchantment, Integer> getEnchantments() {
        return getModifier(Modifier.ENCHANTMENTS);
    }

    public void drop(Instance instance, Point pos) {
        ItemEntity entity = new ItemEntity(item());
        entity.setInstance(instance, pos);
    }

    public <T> T getModifier(Modifier<T> modifier) {
        return modifier.get(this);
    }

    public <T> SbItemStack applyModifier(Modifier<T> modifier, T value) {
        return modifier.toNbt(value, this);
    }
}
