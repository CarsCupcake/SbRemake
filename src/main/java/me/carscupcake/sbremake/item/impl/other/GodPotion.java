package me.carscupcake.sbremake.item.impl.other;

import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.event.PlayerInteractEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.AbilityType;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import me.carscupcake.sbremake.player.potion.IPotion;
import me.carscupcake.sbremake.player.potion.Potion;
import me.carscupcake.sbremake.player.potion.PotionEffect;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

public class GodPotion implements ISbItem, HeadWithValue {
    private static final ItemAbility<PlayerInteractEvent> ability = new ItemAbility<>("", AbilityType.RIGHT_CLICK, event -> {
        double hourDuration = 12 * (1 + (event.player().getSkill(Skill.Alchemy).getLevel() / 50d));
        long ms = (long) (hourDuration * 60 * 60 * 1000) + System.currentTimeMillis();
        synchronized (event.player().getPotionEffects()) {
            for (IPotion potion : Potion.values()) {
                if (potion.isBuff() && !potion.isInstant()) {
                    event.player().startPotionEffect(new PotionEffect(potion, ms++, potion.getMaxLevel()));
                }
            }
            event.player().startPotionEffect(new PotionEffect(Potion.JumpBoost, ms, (byte) 6));
        }
        event.player().setItemInHand(Player.Hand.MAIN, ItemStack.AIR);
        new TaskScheduler() {
            int i = 0;
            @Override
            public void run() {
                event.player().playSound(SoundType.ENTITY_GENERIC_DRINK, Sound.Source.PLAYER, 1, (float) (1 + i / 10d));
                i++;
                if (i == 10) cancel();
            }
        }.repeatTask(2);

    });
    @Override
    public String getId() {
        return "GOD_POTION_2";
    }

    @Override
    public String getName() {
        return "God Potion";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.SPECIAL;
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return List.of(ability);
    }

    @Override
    public boolean isUnstackable() {
        return true;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjAyMjZkNGMxZDMwZmJlYmVjYWU5MzlkYTkwMDYwM2U0Y2QwZmVkODU5MmExYmIzZTExZjlhYzkyMzkxYTQ1YSJ9fX0=";
    }

    @Override
    public Lore getLore() {
        return new Lore("§7Consume this potion to receive an assortment of positive §bpotion effects\n \n§7Duration: §a%d%\n \n§eRight-click to consume!");
    }
}
