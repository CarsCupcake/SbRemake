package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.GetItemStatEvent;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.impl.bow.Shortbow;
import me.carscupcake.sbremake.item.impl.other.SkyblockMenu;
import me.carscupcake.sbremake.item.impl.pets.Pet;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.item.modifiers.RuneModifier;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantments;
import me.carscupcake.sbremake.item.modifiers.enchantment.SkyblockEnchantment;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlot;
import me.carscupcake.sbremake.item.modifiers.potion.PotionInfo;
import me.carscupcake.sbremake.item.modifiers.reforges.Reforge;
import me.carscupcake.sbremake.item.smithing.SmithingItem;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.PickaxeAbility;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.impl.Dungeoneering;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.component.HeadProfile;
import net.minestom.server.item.component.PotionContents;
import net.minestom.server.item.component.TooltipDisplay;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.potion.PotionType;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.*;

import static java.lang.Math.min;

/**
 * A Wrapper for an ItemStack to use some Sb features
 *
 * @param item   the item that gets wrapped
 * @param sbItem the Sb item
 */
public record SbItemStack(@NotNull ItemStack item, @NotNull ISbItem sbItem,
                          @NotNull HashMap<Modifier<?>, Object> modifierCache) {
    public SbItemStack(@NotNull ItemStack item, @NotNull ISbItem sbItem) {
        this(item, sbItem, new HashMap<>());
    }

    public static final SbItemStack AIR = new SbItemStack(ItemStack.AIR, new BaseSbItem(Material.AIR, "Air"));

    private static final Map<String, ISbItem> items = new HashMap<>();

    public static void initSbItem(ISbItem item) {
        items.put(item.getId(), item);
    }

    public static ISbItem raw(String id) {
        return items.get(id);
    }

    public static SbItemStack from(Class<? extends ISbItem> itemClass) {
        return from(ISbItem.get(itemClass));
    }
    public static SbItemStack from(ISbItem item) {
        return item.create();
    }

    public static @Nullable SbItemStack from(String id) {
        ISbItem item = items.get(id);
        if (item == null) return null;
        return item.create();
    }

    @NotNull
    public static SbItemStack from(ItemStack stack) {
        if (stack == null || stack.material() == Material.AIR) return AIR;
        if (!stack.hasTag(Tag.NBT("id")))
            return Objects.requireNonNull(base(stack.material()).withAmount(stack.amount()));
        ISbItem iSbItem = items.get(((StringBinaryTag) stack.getTag(Tag.NBT("id"))).value());
        if (iSbItem == null) return base(stack.material());
        return new SbItemStack(stack, iSbItem);
    }

    @NotNull
    public static SbItemStack base(Material material) {
        ISbItem item = items.get(material.key().value().toUpperCase());
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
            if (enchantments.containsKey(NormalEnchantments.Efficiency) && player != null && !player.getWorldProvider().useCustomMining()) {
                list = list.with(Enchantment.EFFICIENCY, min(255, enchantments.get(NormalEnchantments.Efficiency)));
            }
        } else {
            if (sbItem.modifierBuilder().isGlint()) list = list.with(Enchantment.PROTECTION, 1);

        }
        ItemStack itemStack = item;
        if (sbItem.getType() == ItemType.Pet) {
            Pet.PetInfo petInfo = getModifier(Modifier.PET_INFO);
            if (petInfo.pet() != null) {
                itemStack = itemStack.with(DataComponents.PROFILE, new HeadProfile(new PlayerSkin(petInfo.pet().skullValue(), "")));
            }
        }
        if (sbItem.getType() == ItemType.Rune) {
            RuneModifier modifier = getModifier(Modifier.RUNE);
            if (modifier != null) {
                itemStack = itemStack.with(DataComponents.PROFILE, new HeadProfile(new PlayerSkin(modifier.rune().headValue(), "")));
            }
        }
        if (sbItem.getType() == ItemType.Potion) {
            PotionInfo potionInfo = getModifier(Modifier.POTION);
            if (potionInfo != null) {
                itemStack = itemStack.with(DataComponents.POTION_CONTENTS, new PotionContents(PotionType.AWKWARD, potionInfo.potion().getPotionColor()));
                list = list.with(Enchantment.PROTECTION, 1);
            }
        }
        return new SbItemStack(itemStack.with(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(false, ISbItem.HIDDEN_COMPONENTS))
                .with(DataComponents.LORE, lore).with(DataComponents.ENCHANTMENTS, list).with(DataComponents.CUSTOM_NAME, Component.text(getRarity().getPrefix() + displayName())), sbItem);
    }

    public String displayName() {
        var suffix = new StringBuilder();

        if (sbItem instanceof StarUpgradable u) {
            var stars = getModifier(Modifier.STARS);
            if (stars > 0)
                suffix.append(" ").append(u.getStarDisplay(stars));
        }

        if (sbItem.getType() == ItemType.Potion) {
            PotionInfo potionInfo = getModifier(Modifier.POTION);
            if (potionInfo != null) {
                return potionInfo.customPotionName() != null ? potionInfo.customPotionName() : (potionInfo.potion().getName()) + " " + (StringUtils.toRoman(potionInfo.potionLevel())) + " Potion" + suffix;
            }
        }
        if (sbItem.getType() == ItemType.Rune) {
            RuneModifier runeModifier = getModifier(Modifier.RUNE);
            if (runeModifier != null)
                return (runeModifier.rune().getName()) + " Rune " + (StringUtils.toRoman(runeModifier.level())) + suffix;
        }
        Reforge reforge = getModifier(Modifier.REFORGE);
        Pet.PetInfo petInfo = getModifier(Modifier.PET_INFO);
        return (sbItem().getType() == ItemType.Pet ? "§7[Lvl " + (petInfo.level()) + "] " : "") + (reforge != null ? (reforge.getName()) + " " : "") + (petInfo.pet() == null ? sbItem.getName() : (petInfo.rarity().getPrefix()) + (petInfo.pet().getName())) + suffix;
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
    public static final Stat[] redStats = {Stat.Damage, Stat.Strength, Stat.CritChance, Stat.CritDamage, Stat.AttackSpeed};
    public static final Stat[] greenStats = {Stat.SwingRange, Stat.Health, Stat.Defense, Stat.Speed, Stat.Intelligence, Stat.MagicFind, Stat.PetLuck,
            Stat.TrueDefense, Stat.Ferocity, Stat.MiningSpeed, Stat.Pristine, Stat.MiningFortune, Stat.FarmingFortune, Stat.WheatFortune, Stat.CarrotFortune, Stat.PotatoFortune, Stat.PumpkinFortune, Stat.MelonFortune, Stat.MushroomFortune, Stat.CactusFortune, Stat.SugarCaneFortune, Stat.NetherWartFortune, Stat.CocoaBeansFortune, Stat.ForagingFortune, Stat.SeaCreatureChance, Stat.FishingSpeed, Stat.Vitality, Stat.Mending, Stat.Sweep};

    public List<String> buildLore(@Nullable SkyblockPlayer player) {
        GemstoneSlot[] gemstoneSlots = getModifier(Modifier.GEMSTONE_SLOTS);
        boolean space = false;
        ItemRarity rarity = getRarity();
        List<String> lore = new ArrayList<>();
        if (sbItem.getLorePlacement() == ISbItem.LorePlace.Top) {
            space = true;
            lore.addAll(sbItem.getLore().build(this, player));

        }
        if (sbItem instanceof SmithingItem) {
            lore.add("§8Combinable in Anvil");
            lore.add("§2 ");
        }
        if (sbItem.statsReplacement() != null) {
            lore.addAll(Objects.requireNonNull(sbItem.statsReplacement()).build(this, player));
            space = true;
        } else {
            double breakingPower = getStat(Stat.BreakingPower, player);
            if (breakingPower > 0) {
                lore.add("§8Breaking Power " + (StringUtils.cleanDouble(breakingPower, 1)));
                lore.add("§8  ");
            }

            for (Stat stat : redStats) {
                double value = getStat(stat, player);
                if (value == 0) continue;
                lore.add("§7" + (stat.getName()) + " §c" + (statLine(stat, value, player)));
                space = true;
            }
            for (Stat stat : greenStats) {
                double value = getStat(stat, player);
                if (value == 0) continue;
                lore.add("§7" + (stat.getName()) + " §a" + (statLine(stat, value, player)));
                space = true;
            }
            if (sbItem instanceof Shortbow shortbow) {
                lore.add("§7Shot Cooldown: §a" + (StringUtils.cleanDouble(((player == null) ? shortbow.getShortbowCooldown(getStat(Stat.AttackSpeed, null)) : shortbow.getShortbowCooldown(player.getStat(Stat.AttackSpeed, true))) / 1000d)) + "s");
            }
        }
        if (gemstoneSlots.length > 0) {
            space = true;
            StringBuilder builder = new StringBuilder();
            for (GemstoneSlot s : gemstoneSlots) {
                String bracketColor = s.unlocked() ? (s.gemstone() != null ? s.gemstone().quality().getRarity().getPrefix() : "§7") : "§8";
                builder.append(bracketColor).append("[").append(s.gemstone() != null ? s.gemstone().type().getPrefix() : "§8").append(s.type().getSymbol()).append(bracketColor).append("] ");
            }
            lore.add(builder.toString());
        }
        if (space) {
            lore.add(" ");
        }
        Map<SkyblockEnchantment, Integer> enchantmentIntegerMap = getEnchantments();
        if (!enchantmentIntegerMap.isEmpty()) {
            if (enchantmentIntegerMap.size() <= 6) {
                enchantmentIntegerMap.forEach((skyblockEnchantment, integer) -> {
                    lore.add("§9" + (skyblockEnchantment.getName()) + " " + (StringUtils.toRoman(integer)));
                    lore.addAll(skyblockEnchantment.description().build(this, player));
                });
            } else {
                StringBuilder builder = new StringBuilder("§9");
                int i = 0;
                for (Map.Entry<SkyblockEnchantment, Integer> entry : enchantmentIntegerMap.entrySet()) {
                    builder.append(entry.getKey().getName()).append(" ").append(StringUtils.toRoman(entry.getValue()));
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
        if (sbItem.getType() == ItemType.Potion) {
            PotionInfo info = getModifier(Modifier.POTION);
            if (info != null) {
                lore.add("§a ");
                for (PotionInfo.PotionEffect effect : info.effects()) {
                    lore.add((effect.potion().getPrefix()) + (effect.potion().getName()) + " " + (StringUtils.toRoman(effect.level())) + " " + (effect.potion().isInstant() ? "" : "§f(" + (StringUtils.ticksToString(effect.durationTicks())) + ")"));
                    lore.addAll(effect.potion().description().build(effect.level()));
                    lore.add("§b ");
                }
            }
        }
        if (sbItem.getLorePlacement() == ISbItem.LorePlace.BelowAbility && sbItem.getLore() != Lore.EMPTY) {
            lore.addAll(sbItem.getLore().build(this, player));
            lore.add("  ");
        }
        Reforge reforge = getModifier(Modifier.REFORGE);
        if (reforge != null && reforge.getLore() != Lore.EMPTY) {
            lore.add("§9" + (reforge.getName()) + " Bonus");
            lore.addAll(reforge.getLore().build(this, player));
            lore.add(" ");
        }
        if (sbItem.getType() == ItemType.Pet) {
            Pet.PetInfo petInfo = getModifier(Modifier.PET_INFO);
            if (petInfo.pet() != null) {
                if (petInfo.level() == petInfo.pet().getLevelingType().getMaxLevel()) {
                    lore.add("§b§lMAX LEVEL");
                    lore.add("§8Total Xp " + (StringUtils.cleanDouble(petInfo.exp())));
                } else {
                    double totalDone = 0;
                    for (int i = 1; i < petInfo.level(); i++) {
                        totalDone += petInfo.nextLevelXp(i);
                    }
                    var xpForThis = petInfo.nextLevelXp(petInfo.level());
                    double percentage = (petInfo.exp() - totalDone) / xpForThis;
                    lore.add("§7Progress to Level " + (petInfo.level() + 1) + ": §e" + (StringUtils.cleanDouble(percentage * 100, 1)) + "%");
                    lore.add((StringUtils.makeProgressBarAsString(25, percentage, 1, "§f", "§a", "§m ")) + "§r §e" + (StringUtils.cleanDouble(petInfo.exp() - totalDone, 1)) + "§6/§e" + (StringUtils.toShortNumber(xpForThis)));
                }
                lore.add("§9 ");
            }
        }
        if (sbItem instanceof Shortbow) {
            lore.add((rarity.getPrefix()) + "Shortbow: Instantly shoots!");
            lore.add("  ");
        }
        if (sbItem.getType().isReforgable() && reforge == null) {
            lore.add("§8This item can be reforged!");
        }
        for (Requirement requirement : sbItem.requirements())
            if (!requirement.canUse(player, item)) lore.add(requirement.display());
        lore.add((rarity.getPrefix()) + (getModifier(Modifier.RarityUpgrades) != 0 ? ("§ka" + rarity.getPrefix() + " ") : "") + "§l" + (rarity.getDisplay().toUpperCase()) + (isDungeonItem() ? " DUNGEON" : "") + " " + (sbItem.getType() == ItemType.None && sbItem.isDungeonItem() ? "ITEM" : sbItem.getType().getDisplay().toUpperCase()) + (getModifier(Modifier.RarityUpgrades) != 0 ? (" §ka") : ""));
        return lore;
    }

    public boolean isDungeonItem() {
        return sbItem.isDungeonItem();
    }

    private String statLine(Stat stat, double value, SkyblockPlayer player) {
        Reforge reforge = getModifier(Modifier.REFORGE);
        double reforgeValue = reforge == null ? 0 : reforge.getStat(stat, getRarity(), player);
        GemstoneSlot[] gemstoneSlots = getModifier(Modifier.GEMSTONE_SLOTS);
        double gemstoneValue = 0;
        gemstoneValue = getGemstoneStat(stat, gemstoneValue, gemstoneSlots);
        var dungeonValue = 0d;
        if (isDungeonItem() && (player == null || player.getWorldProvider().type() != SkyblockWorld.Dungeon)) {
            var stars = getModifier(Modifier.STARS);
            var mult = calculatePlayerDungeonBonus(player);
            dungeonValue = (value - value * ((StarUpgradable) sbItem).getBonus(player, stars)) * (1 + mult);
        }
        return ((value < 0) ? "" : "+") + (StringUtils.cleanDouble(value, 1)) + ((stat.isPercentValue()) ? "%" : "") + (reforgeValue != 0 ? " §9(" + (value < 0 ? "" : "+") + (StringUtils.cleanDouble(reforgeValue, 1)) + (stat.isPercentValue() ? "%" : "") + ")" : "") + (gemstoneValue != 0 ? " §d(+" + (StringUtils.cleanDouble(gemstoneValue, 1)) + ")" : "")
                + (dungeonValue != 0 ? " §8(+" + (StringUtils.cleanDouble(dungeonValue, 1)) + (stat.isPercentValue() ? "%" : "") + ")" : "");
    }

    public ItemRarity getRarity() {
        if (sbItem.getType() == ItemType.Rune) {
            RuneModifier modifier = getModifier(Modifier.RUNE);
            if (modifier != null)
                return modifier.rune().getRarity();
        }
        if (sbItem.getType() == ItemType.Potion) {
            PotionInfo info = getModifier(Modifier.POTION);
            if (info != null) return switch (info.potionLevel()) {
                case 1, 2 -> ItemRarity.COMMON;
                case 3, 4 -> ItemRarity.UNCOMMON;
                case 5, 6 -> ItemRarity.RARE;
                case 7, 8 -> ItemRarity.EPIC;
                default -> ItemRarity.LEGENDARY;
            };
        }
        if (sbItem.getType() == ItemType.Pet) return getModifier(Modifier.PET_INFO).rarity();
        var rarity = sbItem.getRarity();
        for (int i = 0; i < getModifier(Modifier.RarityUpgrades); i++)
            if (rarity == ItemRarity.VERY_SPECIAL) break;
            else rarity = rarity.next();

        return rarity;
    }

    public double getStat(Stat stat, SkyblockPlayer player) {
        double baseStat = sbItem.getStat(stat);
        Reforge reforge = getModifier(Modifier.REFORGE);
        if (reforge != null) baseStat += reforge.getStat(stat, getRarity(), player);
        GemstoneSlot[] gemstoneSlots = getModifier(Modifier.GEMSTONE_SLOTS);
        baseStat = getGemstoneStat(stat, baseStat, gemstoneSlots);
        if (sbItem().getType() == ItemType.Pet) {
            Pet.PetInfo petInfo = getModifier(Modifier.PET_INFO);
            if (petInfo.pet() != null) baseStat += petInfo.pet().getStat(stat, petInfo);
        }
        GetItemStatEvent event = new GetItemStatEvent(this, stat, baseStat, player);
        if (sbItem instanceof StarUpgradable starUpgradable) {
            if ((isDungeonItem() || getModifier(Modifier.DUNGEON_ITEM)) && player != null && player.getWorldProvider().type() == SkyblockWorld.Dungeon)
                event.setMultiplier(event.getMultiplier() + calculatePlayerDungeonBonus(player));
            else
                event.setMultiplier(event.getMultiplier() + starUpgradable.getBonus(player, getModifier(Modifier.STARS)));
        }
        MinecraftServer.getGlobalEventHandler().call(event);
        return event.getValue() * event.getMultiplier();
    }

    private double calculatePlayerDungeonBonus(SkyblockPlayer player) {
        var stars = getModifier(Modifier.STARS);
        var playerDungeon = player != null ? player.getSkill(Skill.Dungeneering).getLevel() : 1;
        return stars * 0.1d + ((double) Dungeoneering.dungeonItemStatBonus(playerDungeon)) / 100d;
    }

    private double getGemstoneStat(Stat stat, double baseStat, GemstoneSlot[] gemstoneSlots) {
        if (gemstoneSlots != null) {
            for (GemstoneSlot slot : gemstoneSlots) {
                if (!slot.unlocked()) continue;
                if (slot.gemstone() == null) continue;
                if (slot.gemstone().type().getStat() == stat) {
                    baseStat += slot.gemstone().value(this);
                }
            }
        }
        return baseStat;
    }

    public static Set<String> getIds() {
        return items.keySet();
    }
    public static Collection<ISbItem> getRegisteredItems() {
        return items.values();
    }

    public List<Ability> getAbilities(SkyblockPlayer player) {
        //Later add stuff like hype ability
        List<Ability> abilities = sbItem.getDefaultAbilities();
        if (sbItem.getType() == ItemType.Pickaxe || sbItem.getType() == ItemType.Drill) if (player != null) {
            PickaxeAbility ability = player.getHotm().getActiveAbility();
            if (ability != null) abilities.add(ability.getAbility());
        }
        if (sbItem().getType() == ItemType.Pet) {
            Pet.PetInfo petInfo = getModifier(Modifier.PET_INFO);
            if (petInfo.pet() != null) abilities.addAll(List.of(petInfo.pet().getAbility(getRarity())));
        }
        if (sbItem instanceof IContextAbilities contextAbilities)
            abilities.addAll(contextAbilities.getAbilities(player, this));
        return abilities;
    }


    public SbItemStack withAmount(int i) {
        if (i <= 0) return AIR;
        if (i == item.amount()) return this;
        return new SbItemStack(item.withAmount(i), sbItem);
    }

    private static final Random rand = new Random();

    public SbItemStack calculateFortuneAmount(int base, double fortune) {
        var floored = Math.floor(fortune / 100);
        var extraChance = Math.floor(((fortune / 100) - floored));
        if (rand.nextDouble() < extraChance)
            floored++;
        return withAmount((int) (base * (1 + floored)));
    }

    public int getEnchantmentLevel(SkyblockEnchantment enchantment) {
        return getEnchantments().getOrDefault(enchantment, 0);
    }

    public Map<SkyblockEnchantment, Integer> getEnchantments() {
        return getModifier(Modifier.ENCHANTMENTS);
    }

    public void drop(Instance instance, Point pos) {
        ItemEntity entity = new ItemEntity(item());
        entity.scheduleRemove(Duration.ofSeconds(30));
        entity.setVelocity(new Vec(new Random().nextDouble(), 2, new Random().nextDouble()));
        entity.setInstance(instance, pos);
    }
    public void drop(SkyblockPlayer player, Instance instance, Point pos) {
        if (player.getSkyblockLevel() >= 6) {
            if (player.addItem(this)) return;
        }
        drop(instance, pos);
    }

    public <T> T getModifier(Modifier<T> modifier) {
        return modifier.get(this);
    }

    public <T> SbItemStack withModifier(Modifier<T> modifier, T value) {
        return modifier.toNbt(value, this);
    }
}
