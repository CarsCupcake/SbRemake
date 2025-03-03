package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantment;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;

public class EnchantCommand extends Command {
    public EnchantCommand() {
        super("enchant");
        ArgumentEnum<NormalEnchantment> normalEnchantmentArgumentEnum = new ArgumentEnum<>("enchantment", NormalEnchantment.class);
        ArgumentNumber<Integer> integerArgumentNumber = new ArgumentInteger("level").min(0);
        addSyntax((commandSender, commandContext) -> {
            NormalEnchantment enchantment = commandContext.get(normalEnchantmentArgumentEnum);
            int level = commandContext.get(integerArgumentNumber);
            SbItemStack item = SbItemStack.from(((SkyblockPlayer) commandSender).getItemInHand(PlayerHand.MAIN));
            if (item == null) {
                commandSender.sendMessage("§cNot a valid item!");
                return;
            }

            ((SkyblockPlayer) commandSender).setItemInHand(PlayerHand.MAIN, SbItemStack.from(enchantment.apply(item.item(), level)).update((SkyblockPlayer) commandSender).item());
        }, normalEnchantmentArgumentEnum, integerArgumentNumber);

        addSyntax((commandSender, commandContext) -> {
            NormalEnchantment enchantment = commandContext.get(normalEnchantmentArgumentEnum);
            int level = enchantment.getMaxLevel();
            SbItemStack item = SbItemStack.from(((SkyblockPlayer) commandSender).getItemInHand(PlayerHand.MAIN));
            if (item == null) {
                commandSender.sendMessage("§cNot a valid item!");
                return;
            }
            ((SkyblockPlayer) commandSender).setItemInHand(PlayerHand.MAIN, SbItemStack.from(enchantment.apply(item.item(), level)).update((SkyblockPlayer) commandSender).item());
        }, normalEnchantmentArgumentEnum);
    }
}
