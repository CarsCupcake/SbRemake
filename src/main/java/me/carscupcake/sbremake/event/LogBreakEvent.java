package me.carscupcake.sbremake.event;

import me.carscupcake.sbremake.blocks.Log;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.event.Event;

import java.util.List;

public record LogBreakEvent(SkyblockPlayer player, BlockVec pos, Log log, List<SbItemStack> drops) implements Event {
}
