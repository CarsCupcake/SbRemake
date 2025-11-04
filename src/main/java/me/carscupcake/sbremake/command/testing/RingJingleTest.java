package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.command.DebugCommand;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.number.ArgumentFloat;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;

@DebugCommand
public class RingJingleTest extends Command {
    public RingJingleTest() {
        super("ringjingletest");
        var number = new ArgumentFloat("ticks").min(0f).max(2f);
        addSyntax((sender, context) -> {
            var num = context.get(number);
            new TaskScheduler() {
                int ticks = 0;

                @Override
                public void run() {
                    if (ticks == 10) cancel();
                    sender.playSound(SoundType.BLOCK_NOTE_BLOCK_PLING.create(1, num));
                    ticks++;
                }
            }.repeatTaskAsync(1, 1);
        }, number);
    }
}
