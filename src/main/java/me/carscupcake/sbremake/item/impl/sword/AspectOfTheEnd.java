package me.carscupcake.sbremake.item.impl.sword;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.AbilityType;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import me.carscupcake.sbremake.item.ability.ManaRequirement;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.util.Cost;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class AspectOfTheEnd implements ISbItem, ISbItem.StatProvider, GemstoneSlots {
    private final List<Ability> abilities = List.of(new ItemAbility<>("Transmission", AbilityType.RIGHT_CLICK, playerInteractEvent -> {
        Pos pos = playerInteractEvent.getPlayer().getPosition().add(0, playerInteractEvent.player().getEyeHeight(), 0);
        Vec dir = pos.direction().normalize().mul(0.5);
        playerInteractEvent.player().playSound(Sound.sound(Key.key("minecraft", "entity.enderman.teleport"), Sound.Source.PLAYER, 1, 1f));

        for (int i = 1; i <= 16; i++) {
            Pos newPos = pos.add(dir);
            Block b = playerInteractEvent.getPlayer().getInstance().getBlock(newPos);
            if (b.isSolid()) {
                playerInteractEvent.player().teleport(pos);
                return;
            }
            pos = newPos;
        }
        playerInteractEvent.player().getTemporaryModifiers().add(Stat.Speed, modifier, Duration.ofSeconds((3)));
        playerInteractEvent.player().teleport(pos);
    }, new Lore("§7Teleport §a8 blocks §7ahead of you and gain §a+50 " + (Stat.Speed.toString()) + " for §a3 seconds§7"), new ManaRequirement<>(50)));
    private static final PlayerStatEvent.PlayerStatModifier modifier = new PlayerStatEvent.BasicModifier("Transmission", 50, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability);
    @Override
    public String getId() {
        return "ASPECT_OF_THE_END";
    }

    @Override
    public String getName() {
        return "Aspect Of The End";
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Sword;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Damage, 100d, Stat.Strength, 100d);
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return abilities;
    }

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return new GemstoneSlotType[]{GemstoneSlotType.Sapphire};
    }

    @Override
    public boolean[] getUnlocked() {
        return new boolean[]{true};
    }

    @Override
    public Cost[][] getLockedSlotCost() {
        return new Cost[0][];
    }
}
