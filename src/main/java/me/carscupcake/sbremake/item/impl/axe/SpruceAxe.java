package me.carscupcake.sbremake.item.impl.axe;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.blocks.Log;
import me.carscupcake.sbremake.event.LogBreakEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.ItemCost;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.worlds.impl.ForagingIsle;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SpruceAxe implements ISbItem, Listener, GemstoneSlots {

    @Override
    public String getId() {
        return "JUNGLE_AXE";
    }

    @Override
    public String getName() {
        return "Spruce Axe";
    }

    @Override
    public Material getMaterial() {
        return Material.WOODEN_AXE;
    }

    @Override
    public ItemType getType() {
        return ItemType.Axe;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public Lore getLore() {
        return new Lore(List.of("§7A powerful Wooden Axe which", "§7break multiple logs in a single hit!", "§8Cooldown: §a2s"));
    }

    @Override
    public LorePlace getLorePlacement() {
        return LorePlace.BelowAbility;
    }

    @Override
    public double getStat(Stat stat) {
        return stat == Stat.Sweep ? 10 : 0;
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("sweep").addListener(LogBreakEvent.class, event -> {
            final int blocks = (int) Math.max(event.player().getStat(Stat.Sweep), 35);
            if (blocks == 0) return;
            var instance = event.player().getInstance();
            ForagingIsle foragingIsle = (ForagingIsle) event.player().getWorldProvider();
            LinkedList<BlockVec> candidates = new LinkedList<>();
            for (int x = -1; x <= 1; x++)
                for (int y = -1; y <= 1; y++)
                    for (int z = -1; z <= 1; z++)
                        if (x != 0 || y != 0 || z != 0) {
                            var pos = event.pos().add(x, y, z);
                            var b = instance.getBlock(event.pos());
                            if (b == Block.AIR) continue;
                            candidates.push(pos);
                        }

            new TaskScheduler() {
                int woods = 0;

                @Override
                public void run() {
                    while (!candidates.isEmpty()) {
                        var candidate =  candidates.poll();
                        var block = instance.getBlock(candidate);
                        var log = Log.getLog(block);
                        if (log != null) {
                            woods++;
                            instance.setBlock(candidate, Block.AIR);
                            foragingIsle.brokenLogs.put(candidate, new Log.LogInfo(log, block.properties()));
                            log.drop().create().calculateFortuneAmount(1, event.player().getStat(Stat.ForagingFortune)).drop(instance,
                                            candidate.middle());

                            for (int x = -1; x <= 1; x++)
                                for (int y = -1; y <= 1; y++)
                                    for (int z = -1; z <= 1; z++)
                                        if (x != 0 || y != 0 || z != 0) {
                                            var pos = candidate.add(x, y, z);
                                            var b = instance.getBlock(pos);
                                            if (b == Block.AIR) continue;
                                            candidates.add(pos);
                                        }
                            break;
                        }
                    }
                    if (candidates.isEmpty() || woods >= blocks) {
                        cancel();
                    }
                }
            }.repeatTask(1, 1);

        });
    }

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return new GemstoneSlotType[]{GemstoneSlotType.Citrine};
    }

    @Override
    public boolean[] getUnlocked() {
        return new boolean[]{false};
    }

    @Override
    public Cost[][] getLockedSlotCost() {
        return new Cost[][]{{new CoinsCost(50_000), new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Citrine).get(Gemstone.Quality.Fine).asItem(), 20)}};
    }
}
