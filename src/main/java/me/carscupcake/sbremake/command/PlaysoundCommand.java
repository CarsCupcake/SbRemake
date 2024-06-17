package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.SoundType;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.number.ArgumentFloat;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;

public class PlaysoundCommand extends Command {
    public PlaysoundCommand() {
        super("playsound");
        ArgumentEnum<SoundType> soundType = new ArgumentEnum<>("SoundType", SoundType.class);
        ArgumentNumber<Float> volume = new ArgumentFloat("volume").between(0f, 2f);
        ArgumentNumber<Float> pitch = new ArgumentFloat("pitch").between(0f, 2f);
        addSyntax((commandSender, commandContext) -> {
            ((SkyblockPlayer) commandSender).playSound(commandContext.get(soundType), Sound.Source.AMBIENT, 1f, 1f);
        }, soundType);
        addSyntax((commandSender, commandContext) -> {
            ((SkyblockPlayer) commandSender).playSound((SoundType) commandContext.get(soundType), Sound.Source.AMBIENT, commandContext.get(volume), commandContext.get(pitch));
        }, soundType, volume, pitch);
    }


}
