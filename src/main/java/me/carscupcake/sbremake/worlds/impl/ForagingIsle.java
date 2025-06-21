package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.blocks.Log;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.coordinate.BlockVec;

import java.util.HashMap;

public abstract class ForagingIsle extends SkyblockWorld.WorldProvider {
    public final HashMap<BlockVec, Log.LogInfo> brokenLogs = new HashMap<>();
    private final TaskScheduler foragingReset = new TaskScheduler() {
        @Override
        public void run() {
            brokenLogs.forEach((block, log) -> log.regen(container, block));
            brokenLogs.clear();
        }
    };
    @Override
    protected void register() {
        foragingReset.repeatTask(20 * 30);
    }

    @Override
    protected void unregister() {
        foragingReset.cancel();
    }
}
