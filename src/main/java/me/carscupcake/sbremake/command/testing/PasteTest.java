package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.command.DebugCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Pos2d;
import me.carscupcake.sbremake.worlds.impl.dungeon.*;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;

@DebugCommand
public class PasteTest extends Command {
    public PasteTest() {
        super("pastetest");
        var enumRoomShape = new ArgumentEnum<>("type", RoomShape.class);
        var enumRoomType = new ArgumentEnum<>("roomType", RoomType.class);
        var enumRotation = new ArgumentEnum<>("rotation", Rotation.class);
        var xPos = new ArgumentInteger("x");
        var zPos = new ArgumentInteger("z");
        addSyntax((sender, context) -> {
            var paster = new Paster(new Room[0][0], ((SkyblockPlayer) sender).getInstance());
            var rotation = context.get(enumRotation);
            var roomShape = context.get(enumRoomShape);
            paster.paste(new Pos2d(0, 0), rotation, roomShape, switch (roomShape) {
                case ONE_BY_ONE -> "overgrown-3";
                case ONE_BY_TWO -> "pedestal-5";
                case ONE_BY_THREE -> "wizard-4";
                case ONE_BY_FOUR -> "mossy-4";
                case TWO_BY_TWO -> "mithril-cave-10";
                case L_SHAPE -> "dino-dig-site-4";
            }, RoomType.Room);
        }, enumRoomShape, enumRotation);
        addSyntax((sender, context) -> {
            var paster = new Paster(new Room[0][0], ((SkyblockPlayer) sender).getInstance());
            var rotation = context.get(enumRotation);
            var roomShape = context.get(enumRoomShape);
            paster.paste(new Pos2d(context.get(xPos), context.get(zPos)), rotation, roomShape, switch (roomShape) {
                case ONE_BY_ONE -> "overgrown-3";
                case ONE_BY_TWO -> "pedestal-5";
                case ONE_BY_THREE -> "wizard-4";
                case ONE_BY_FOUR -> "mossy-4";
                case TWO_BY_TWO -> "mithril-cave-10";
                case L_SHAPE -> "dino-dig-site-4";
            }, RoomType.Room);
        }, enumRoomShape, enumRotation, xPos, zPos);
        addSyntax((sender, context) -> {
            var paster = new Paster(new Room[0][0], ((SkyblockPlayer) sender).getInstance());
            var rotation = context.get(enumRotation);
            var roomType = context.get(enumRoomType);
            paster.paste(new Pos2d(0, 0), rotation, RoomShape.ONE_BY_ONE, switch (roomType) {
                case Trap -> "trap-hard-4";
                case Puzzle -> "boxes-room";
                default -> throw new IllegalArgumentException("Not a valid room type");
            }, roomType);
        }, enumRoomType, enumRotation);
        System.gc();

    }
}
