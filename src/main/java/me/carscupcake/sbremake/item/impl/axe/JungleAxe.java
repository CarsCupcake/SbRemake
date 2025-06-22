package me.carscupcake.sbremake.item.impl.axe;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.blocks.Log;
import me.carscupcake.sbremake.event.LogBreakEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.ItemCost;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.worlds.impl.Hub;
import me.carscupcake.sbremake.worlds.impl.LegacyPark;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;

public class JungleAxe implements ISbItem, Listener, GemstoneSlots {

    @Override
    public String getId() {
        return "JUNGLE_AXE";
    }

    @Override
    public String getName() {
        return "Jungle Axe";
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

            new TaskScheduler() {
                int woods = 0;
                final HashSet<BlockVec> candidatesOld = new HashSet<>();
                final LinkedList<BlockVec> candidates = new LinkedList<>();
                final LinkedList<BlockVec> candidatesNew = new LinkedList<>(List.of(event.pos()));
                final LinkedList<BlockVec> toBreak = new LinkedList<>();

                @Override
                public void run() {
                    if (!toBreak.isEmpty()) {
                        var block = toBreak.poll();
                        var blockType = instance.getBlock(block);
                        Log log = null;
                        for (Log l : Log.logs)
                            if (Objects.requireNonNull(l.block().registry().material()).equals(blockType.registry().material())) {
                                log = l;
                                break;
                            }
                        if (log != null) {
                            event.player().getInstance().setBlock(block, Block.AIR);
                            SbItemStack i = log.drops(event.player());
                            ItemEntity entity = new ItemEntity(i.item());
                            entity.setInstance(event.player().getInstance(), block.add(0.5, 0, 0.5));
                            entity.addViewer(event.player());
                            event.player().getSkill(Skill.Foraging).addXp(log.xp());
                            if (event.player().getWorldProvider() instanceof LegacyPark park)
                                park.brokenLogs.put(block, new Log.LogInfo(log, blockType.properties()));
                            if (event.player().getWorldProvider() instanceof Hub hub)
                                hub.brokenLogs.put(block, new Log.LogInfo(log, blockType.properties()));
                        }
                    }
                    if (woods >= blocks) {
                        cancel();
                        return;
                    }
                    if (candidatesNew.isEmpty()) {
                        cancel();
                        return;
                    }
                    candidates.addAll(candidatesNew);
                    candidatesNew.clear();
                    while (!candidates.isEmpty()) {
                        BlockVec candidate = candidates.pop();
                        candidatesOld.add(candidate);
                        for (int x = -1; x <= 1; x++) {
                            for (int y = -1; y <= 1; y++) {
                                for (int z = -1; z <= 1; z++) {
                                    if (x != 0 || y != 0 || z != 0) {
                                        if (woods >= blocks) break;
                                        BlockVec posNew = candidate.add(x, y, z);
                                        if (!candidatesOld.contains(posNew) && !candidates.contains(posNew) && !candidatesNew.contains(posNew)) {
                                            Block blockNew = event.player().getInstance().getBlock(posNew);
                                            Log log = null;
                                            for (Log l : Log.logs)
                                                if (Objects.requireNonNull(l.block().registry().material()).equals(blockNew.registry().material())) {
                                                    log = l;
                                                    break;
                                                }
                                            if (log != null) {
                                                toBreak.add(posNew);
                                                candidatesNew.add(posNew);
                                                woods++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (woods >= blocks) break;
                    }
                }
            }.repeatTask(2);

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
