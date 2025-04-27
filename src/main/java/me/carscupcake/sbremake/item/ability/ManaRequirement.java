package me.carscupcake.sbremake.item.ability;

import me.carscupcake.sbremake.command.testing.ToggleCommand;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.enchantment.UltimateEnchantments;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.EnchantmentUtils;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;

public record ManaRequirement<T extends PlayerEvent>(long manaCost) implements Requirement<T> {
    @Override
    public boolean requirement(T t) {
        if (ToggleCommand.Toggles.IgnoreMana.toggled) return true;
        SkyblockPlayer player = (SkyblockPlayer) t.getPlayer();
        return player.getMana() >= getManaCost(player.getSbItemInMainHand());
    }

    @Override
    public void execute(T t) {
        SkyblockPlayer player = (SkyblockPlayer) t.getPlayer();
        player.setMana(player.getMana() - getManaCost(player.getSbItemInHand(Player.Hand.MAIN)));
    }

    public long getManaCost(SbItemStack item) {
        return (long) (manaCost * (1 - EnchantmentUtils.getUltimateWiseBonus(item.getEnchantmentLevel(UltimateEnchantments.UltimateWise))));
    }
}
