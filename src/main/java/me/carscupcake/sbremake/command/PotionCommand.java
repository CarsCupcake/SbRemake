package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.item.modifiers.potion.PotionInfo;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.potion.IPotion;
import me.carscupcake.sbremake.player.potion.PotionType;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentLong;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.item.Material;

import java.util.List;

public class PotionCommand extends Command {
    public PotionCommand() {
        super("potion");
        ArgumentWord potion = new ArgumentWord("potion").from(IPotion.potions.keySet().toArray(new String[0]));
        ArgumentNumber<Integer> amplifier = new ArgumentInteger("amplifier").min(1).max(127);
        ArgumentNumber<Long> tickDuration = new ArgumentLong("tickDuration").min(1L);
        addSyntax((commandSender, commandContext) -> {
            IPotion p = IPotion.potions.get(commandContext.get(potion));
            byte ampl = (byte) ((int) commandContext.get(amplifier));
            long time = commandContext.get(tickDuration);
            PotionInfo effect = new PotionInfo(p, false, false, ampl, PotionType.POTION, null, List.of(new PotionInfo.PotionEffect(p, ampl, time)));
            ((SkyblockPlayer) commandSender).addItem(SbItemStack.base(Material.POTION).withModifier(Modifier.POTION, effect).update((SkyblockPlayer) commandSender).item());
        }, potion, amplifier, tickDuration);
    }
}
