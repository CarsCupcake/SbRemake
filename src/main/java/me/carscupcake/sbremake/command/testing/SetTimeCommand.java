package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.Time;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;

public class SetTimeCommand extends Command {
    public SetTimeCommand() {
        super("settime");
        ArgumentNumber<Integer> hour = new  ArgumentInteger("hour").min(0).max(23);
        ArgumentNumber<Integer> minute = new  ArgumentInteger("minute").min(0).max(5);
        addSyntax((_, context) -> {
            Time.hour = context.get(hour);
            Time.minute = context.get(minute) * 10;
            if (Time.hour < 6) {
                Time.tick = 24000 - ((6 - Time.hour) * 1000L + (long) (Time.minute * (1000d / 60)));
            } else {
                Time.tick = ((Time.hour - 6) * 1000L + (long) (Time.minute * (1000d / 60)));
            }
            System.out.println(Time.tick);
            SkyblockWorld.getAllWorlds().forEach(worldProvider -> worldProvider.container.setTime(Time.tick));
        }, hour, minute);
    }
}
