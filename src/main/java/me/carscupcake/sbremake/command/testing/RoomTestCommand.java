package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.command.DebugCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Pos2d;
import me.carscupcake.sbremake.worlds.impl.dungeon.Paster;
import me.carscupcake.sbremake.worlds.impl.dungeon.RoomShape;
import me.carscupcake.sbremake.worlds.impl.dungeon.RoomType;
import me.carscupcake.sbremake.worlds.impl.dungeon.Rotation;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentString;

@DebugCommand
public class RoomTestCommand extends Command {
    public RoomTestCommand() {
        super("room");
        var argumentEnum = new ArgumentEnum<>("shape", RoomShape.class);
        var argumentString = new ArgumentString("name");
        addSyntax((sender, args) -> {
            Paster.paste(new Pos2d(0, 0), Rotation.NW, args.get(argumentEnum), args.get(argumentString), RoomType.Room, ((SkyblockPlayer)sender).getInstance());
        }, argumentEnum, argumentString);
    }
}
