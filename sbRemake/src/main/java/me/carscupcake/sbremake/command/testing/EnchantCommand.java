package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantments;
import me.carscupcake.sbremake.item.modifiers.enchantment.SkyblockEnchantment;
import me.carscupcake.sbremake.item.modifiers.enchantment.UltimateEnchantments;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class EnchantCommand extends Command {
    public EnchantCommand() {
        super("enchant");
        ArgumentWord word = new ArgumentWord("enchantment").from(ArrayUtils.addAll(Arrays.stream(NormalEnchantments.values()).map(NormalEnchantments::getId).toArray(String[]::new), Arrays.stream(UltimateEnchantments.values()).map(UltimateEnchantments::getId).toArray(String[]::new)));
        ArgumentNumber<Integer> integerArgumentNumber = new ArgumentInteger("level").min(0);
        addSyntax((commandSender, commandContext) -> {
            var normals = NormalEnchantments.values();
            var w = commandContext.get(word);
            if (w == null) return;
            SkyblockEnchantment enchantment = null;
            for (var e : normals) {
                if (e.getId().equals(w)) {
                    enchantment = e;
                    break;
                }
            }
            if (enchantment == null) {
                var ults = UltimateEnchantments.values();
                for (var e : ults) {
                    if (e.getId().equals(w)) {
                        enchantment = e;
                        break;
                    }
                }
                if (enchantment == null) {
                    commandSender.sendMessage("§cNot a valid enchantment!");
                    return;
                }
            }
            int level = commandContext.get(integerArgumentNumber);
            SbItemStack item = ((SkyblockPlayer) commandSender).getSbItemInHand(PlayerHand.MAIN);
            if (item == null) {
                commandSender.sendMessage("§cNot a valid item!");
                return;
            }

            ((SkyblockPlayer) commandSender).setItemInHand(PlayerHand.MAIN, SbItemStack.from(enchantment.apply(item.item(), level)).update((SkyblockPlayer) commandSender));
        }, word, integerArgumentNumber);

        addSyntax((commandSender, commandContext) -> {
            var normals = NormalEnchantments.values();
            var w = commandContext.get(word);
            if (w == null) return;
            SkyblockEnchantment enchantment = null;
            for (var e : normals) {
                if (e.getId().equals(w)) {
                    enchantment = e;
                    break;
                }
            }
            if (enchantment == null) {
                var ults = UltimateEnchantments.values();
                for (var e : ults) {
                    if (e.getId().equals(w)) {
                        enchantment = e;
                        break;
                    }
                }
                if (enchantment == null) {
                    commandSender.sendMessage("§cNot a valid enchantment!");
                    return;
                }
            }
            int level = enchantment.getMaxLevel();
            SbItemStack item = ((SkyblockPlayer) commandSender).getSbItemInHand(PlayerHand.MAIN);
            if (item == null) {
                commandSender.sendMessage("§cNot a valid item!");
                return;
            }
            ((SkyblockPlayer) commandSender).setItemInHand(PlayerHand.MAIN, SbItemStack.from(enchantment.apply(item.item(), level)).update((SkyblockPlayer) commandSender));
        }, word);
    }
}
