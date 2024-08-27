package me.carscupcake.sbremake.item.impl.axe;

import me.carscupcake.sbremake.blocks.Log;
import me.carscupcake.sbremake.event.LogBreakEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.ItemCost;
import me.carscupcake.sbremake.worlds.impl.Hub;
import me.carscupcake.sbremake.worlds.impl.Park;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.util.*;

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

    private static final HashMap<SkyblockPlayer, Long> cooldown = new HashMap<>();

    @Override
    public EventNode<Event> node() {
        return EventNode.all("jungle_axe").addListener(LogBreakEvent.class, event -> {
            SbItemStack stack = SbItemStack.from(event.player().getItemInHand(Player.Hand.MAIN));
            if (stack == null) return;
            ISbItem item = stack.sbItem();
            int blocks = item instanceof JungleAxe ? 10 : (item instanceof Treecapitator ? 35 : 0);
            if (blocks == 0) return;
            long last = cooldown.getOrDefault(event.player(), 0L);
            long now = System.currentTimeMillis();
            if (now - last < 2000) return;
            cooldown.put(event.player(), now);
            int woods = 0;
            HashSet<BlockVec> candidatesOld = new HashSet<>();
            LinkedList<BlockVec> candidates = new LinkedList<>();
            LinkedList<BlockVec> candidatesNew = new LinkedList<>();
            candidatesNew.add(event.pos());
            while (woods < blocks) {
                if (candidatesNew.isEmpty()) break;
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
                                            candidatesNew.add(posNew);
                                            woods++;
                                            event.player().getInstance().setBlock(posNew, Block.AIR);
                                            SbItemStack i = log.drops(event.player());
                                            ItemEntity entity = new ItemEntity(i.item());
                                            entity.setInstance(event.player().getInstance(), Pos.fromPoint(posNew).add(0.5, 0, 0.5));
                                            entity.addViewer(event.player());
                                            event.player().getSkill(Skill.Foraging).addXp(log.xp());
                                            if (event.player().getWorldProvider() instanceof Park park)
                                                park.brokenLogs.put(posNew, new Log.LogInfo(log, blockNew.properties()));
                                            if (event.player().getWorldProvider() instanceof Hub hub)
                                                hub.brokenLogs.put(posNew, new Log.LogInfo(log, blockNew.properties()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (woods >= blocks) break;
                }
            }

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
