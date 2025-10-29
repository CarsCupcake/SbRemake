package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.potion.IPotion;
import me.carscupcake.sbremake.player.potion.Potion;
import me.carscupcake.sbremake.player.potion.PotionEffect;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.worlds.impl.hub.CentauriToyBox;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;

public class GodPotionCommand extends Command {
    public GodPotionCommand() {
        super("godpotion");
        ArgumentNumber<Integer> minutes = new ArgumentInteger("minutes").min(1).max(10080);
        addSyntax((commandSender, commandContext) -> {
            SkyblockPlayer player = (SkyblockPlayer) commandSender;
            long ms = commandContext.get(minutes) * 60 * 1000;
            startPotion(player, ms);
            player.playSound(SoundType.ENTITY_PLAYER_BURP, Sound.Source.PLAYER, 2f, 0.1f);
        }, minutes);
        addSyntax((commandSender, ignored) -> {
            SkyblockPlayer player = (SkyblockPlayer) commandSender;
            long ms = 604_800_000; //1 Week of God Potion
            startPotion(player, ms);
            player.playSound(SoundType.ENTITY_PLAYER_BURP, Sound.Source.PLAYER, 2f, 0.1f);
        });
    }

    public static void startPotion(SkyblockPlayer player, long ms) {
        ms += System.currentTimeMillis();
        CentauriToyBox.giveGodPotionEffect(player, ms);
    }
}
