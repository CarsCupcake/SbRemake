package me.carscupcake.sbremake.item.impl.sword.dungeons;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.event.EntityMeleeDamagePlayerEvent;
import me.carscupcake.sbremake.event.GetItemStatEvent;
import me.carscupcake.sbremake.event.PlayerInteractEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.ability.*;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.player.Essence;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.particle.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NecronBlade implements ISbItem, ISbItem.StatProvider, Dungeonizable, IContextAbilities, Listener {

    public static final ItemAbility<PlayerInteractEvent> IMPLOSION = new ItemAbility<>("Implosion", AbilityType.RIGHT_CLICK, event -> {
        ParticleUtils.spawnParticle(event.player().getInstance(), event.player().getPosition().add(0, event.player().getEyeHeight() / 2, 0), Particle.EXPLOSION, 6, Vec.ZERO, 10);
        event.player().getInstance().playSound(SoundType.ENTITY_GENERIC_EXPLODE.create(1f, 1.2f), event.player().getPosition());
        var intelligence = event.player().getStat(Stat.Intelligence);
        AtomicInteger totalEntities = new AtomicInteger(0);
        var total = event.player().getInstance().getNearbyEntities(event.player().getPosition(), 6).stream().filter(entity -> entity instanceof SkyblockEntity).map(entity -> {
            totalEntities.set(totalEntities.get() + 1);
            return (SkyblockEntity) entity;
        }).map(entity -> entity.mageDamage(event.player(), intelligence, 10_000, 0.3)).reduce(0d, Double::sum);
        if (totalEntities.get() != 0)
            event.player().sendMessage("§7Your Implosion hit §c" + totalEntities.get() + "§7 enemy" + (totalEntities.get() > 1 ? "s" : "") + " for §c" + StringUtils.toFormatedNumber(total) + "§7 damage.");
    }, new Lore("§7Deals §c%d% §7to nearby enemies.", "%d%", new Lore.AbilityDamagePlaceholder(10_000, 0.3)), new ManaRequirement<>(300), new CooldownRequirement<>(10));
    public static final ItemAbility<PlayerInteractEvent> SHADOW_WARP = new ItemAbility<>("Shadow Warp", AbilityType.RIGHT_CLICK, _ -> {

    }, new Lore("§7Created a spacial distortion §e10§7 blocks ahead of you that sucks all enemies around it. Using this ability again with §e5§7 seconds to detonate the warp and deal §c%d%§7 damage to enemies near it.", "%d%", new Lore.AbilityDamagePlaceholder(10_000, 0.3)), new ManaRequirement<>(300), new CooldownRequirement<>(10));
    private static final Set<SkyblockPlayer> witherShields = new HashSet<>();
    public static final ItemAbility<PlayerInteractEvent> WITHER_SHIELD = new ItemAbility<>("Wither Shield", AbilityType.RIGHT_CLICK, event -> {
        event.player().playSound(SoundType.ENTITY_ZOMBIE_VILLAGER_CURE, 2f, 1.8f);
        witherShields.add(event.player());
        var critDamage = event.player().getStat(Stat.CritDamage);
        event.player().addAbsorption(critDamage * 1.5);
        new TaskScheduler() {
            @Override
            public void run() {
                if (!event.player().isOnline()) return;
                witherShields.remove(event.player());
                event.player().addSbHealth(event.player().getAbsorption() * 0.5);
                event.player().setAbsorption(0);
                event.player().playSound(SoundType.ENTITY_PLAYER_LEVELUP, 1, 2);
                ParticleUtils.spawnParticle(event.player().getInstance(), event.player().getPosition().add(0, event.player().getEyeHeight() / 2, 0), Particle.HEART, 5, new Vec(0.5, 0.5, 0.5), 1);
            }
        }.delayTask(20 * 5);
    }, new Lore("§7Reduces damage taken by §c10%§7 for §e5§7 seconds. Also grants an §6❤ Absorption§7 shield that gives §9150%§7 of your " + Stat.CritDamage + "§7 as SkyBlock " + Stat.Health + "§7, after §e5§7 seconds §a50%§7 of the shield is converted into healing."), new ManaRequirement<>(150), new CooldownRequirement<>(10));
    public static final ItemAbility<PlayerInteractEvent> WITHER_IMPACT = new ItemAbility<>("Wither Impact", AbilityType.RIGHT_CLICK, event -> {
        if (!witherShields.contains(event.player())) WITHER_SHIELD.executor().accept(event);
        if (!event.player().getInstance().getBlock(event.getPlayer().getPosition().add(0, 2, 0)).isSolid()) {
            var pos = event.player().getPosition().add(0, event.getPlayer().getEyeHeight(), 0);
            var dir = event.player().getPosition().direction().normalize().mul(0.5);
            for (int i = 1; i <= 20; i++) {
                Pos newPos = pos.add(dir);
                Block b = event.getPlayer().getInstance().getBlock(newPos);
                if (b.isSolid()) {
                    break;
                }
                b = event.getPlayer().getInstance().getBlock(newPos.add(0, 1, 0));
                if (b.isSolid()) {
                    break;
                }
                pos = newPos;
            }
            event.player().teleport(pos);
        }
        IMPLOSION.executor().accept(event);
        event.player().setVelocity(event.getPlayer().getVelocity().withY(0));
    }, new Lore("§7Teleport §a10 blocks§7 ahead of you. Then implode dealing §c%d% " + Stat.Damage + "§7 to nearby enemies. Also applies the wither shield scroll ability reducing damage taken and granting an §6❤ Absorption§7 shield for §e5§7 seconds.", "%d%", new Lore.AbilityDamagePlaceholder(10_000, 0.3)), new ManaRequirement<>(300));
    public static final Modifier<List<Ability>> WITHER_SCROLLS = new Modifier<>() {
        @Override
        public @Nullable List<Ability> getFromNbt(SbItemStack item) {
            if (!(item.sbItem() instanceof NecronBlade)) return null;
            var scrolls = Objects.requireNonNull(item.item().get(DataComponents.CUSTOM_DATA)).nbt().getList("ability_scrolls", ListBinaryTag.empty());
            if (scrolls.isEmpty()) return List.of();
            if (scrolls.size() == 3) return List.of(WITHER_IMPACT);
            var list = new ArrayList<Ability>(scrolls.size());
            for (var scroll : scrolls) {
                var string = ((StringBinaryTag) scroll).value();
                switch (string) {
                    case "IMPLOSION_SCROLL" -> list.add(IMPLOSION);
                    case "SHADOW_WARP_SCROLL" -> list.add(SHADOW_WARP);
                    case "WITHER_SHIELD_SCROLL" -> list.add(WITHER_SHIELD);
                    default -> throw new IllegalStateException("Unexpected value: " + string);
                }
            }
            return list;
        }

        @Override
        public SbItemStack toNbt(List<Ability> abilities, SbItemStack itemStack) {
            var scrolls = ListBinaryTag.builder();
            if (abilities.size() == 3) {
                scrolls.add(StringBinaryTag.stringBinaryTag("IMPLOSION_SCROLL"));
                scrolls.add(StringBinaryTag.stringBinaryTag("SHADOW_WARP_SCROLL"));
                scrolls.add(StringBinaryTag.stringBinaryTag("WITHER_SHIELD_SCROLL"));
                return SbItemStack.from(itemStack.item().with(DataComponents.CUSTOM_DATA, new CustomData(Objects.requireNonNull(itemStack.item().get(DataComponents.CUSTOM_DATA)).nbt().put("ability_scrolls", scrolls.build()))));
            }
            for (var ability : abilities) {
                if (ability == WITHER_IMPACT) {
                    scrolls.add(StringBinaryTag.stringBinaryTag("IMPLOSION_SCROLL"));
                    scrolls.add(StringBinaryTag.stringBinaryTag("SHADOW_WARP_SCROLL"));
                    scrolls.add(StringBinaryTag.stringBinaryTag("WITHER_SHIELD_SCROLL"));
                    break;
                }
                if (ability == SHADOW_WARP) scrolls.add(StringBinaryTag.stringBinaryTag("SHADOW_WARP_SCROLL"));
                if (ability == WITHER_SHIELD) scrolls.add(StringBinaryTag.stringBinaryTag("WITHER_SHIELD_SCROLL"));
                if (ability == IMPLOSION) scrolls.add(StringBinaryTag.stringBinaryTag("IMPLOSION_SCROLL"));
            }
            return SbItemStack.from(itemStack.item().with(DataComponents.CUSTOM_DATA, new CustomData(Objects.requireNonNull(itemStack.item().get(DataComponents.CUSTOM_DATA)).nbt().put("ability_scrolls", scrolls.build()))));
        }
    };

    @Override
    public boolean isDungeonItem() {
        return true;
    }

    @Override
    public Essence getEssence() {
        return Essence.Wither;
    }

    @Override
    public String getId() {
        return "NECRON_BLADE";
    }

    @Override
    public String getName() {
        return "Necron's Blade (Unrefined)";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Sword;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Damage, 260, Stat.Strength, 110, Stat.Intelligence, 50, Stat.Ferocity, 30);
    }

    @Override
    public int getMaxStars() {
        return 5;
    }

    @Override
    public Collection<Ability> getAbilities(@Nullable SkyblockPlayer player, @NotNull SbItemStack item) {
        return item.getModifier(WITHER_SCROLLS);
    }

    @Override
    public Lore getLore() {
        return new Lore("§7Deals §a+50% §7damage to Withers.");
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("wither_scrolls.ability").addListener(EntityMeleeDamagePlayerEvent.class, event -> {
            if (witherShields.contains(event.getPlayer())) event.setNormalDamage(event.getNormalDamage() * 0.9);
        }).addListener(GetItemStatEvent.class, event -> {
            if (event.getPlayer() == null) return;
            if (!(event.getItemStack().sbItem() instanceof NecronBlade)) return;
            var catalevel = event.getPlayer().getSkill(Skill.Dungeneering).getLevel();
            if (event.getStat() == Stat.Damage) {
                event.addValue(catalevel);
                return;
            }
            switch (event.getItemStack().sbItem()) {
                case Hyperion _ -> {
                    if (event.getStat() == Stat.Intelligence) event.addValue(catalevel * 2);
                }
                case Valkyrie _ -> {
                    if (event.getStat() == Stat.Strength) event.addValue(catalevel);
                }
                case Scylla _ -> {
                    if (event.getStat() == Stat.CritDamage) event.addValue(catalevel);
                }
                case Astraea _ -> {
                    if (event.getStat() == Stat.Defense) event.addValue(catalevel * 2);
                }
                default -> {
                }
            }
        });
    }
}
