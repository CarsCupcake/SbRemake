package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.instance.block.Block;

public class BlockData extends Command {
    public BlockData() {
        super("blockdata");
        addSyntax((commandSender, _) -> {
            BlockVec block = new BlockVec(((SkyblockPlayer) commandSender).getLineOfSight(5).getFirst());
            Block b = ((SkyblockPlayer) commandSender).getInstance().getBlock(block);
            commandSender.sendMessage("Properties: " + (b.properties().toString()) );
            if (b.nbt() != null)
                commandSender.sendMessage("Nbt: " + (b.nbt().toString()) );
        });
    }
}
