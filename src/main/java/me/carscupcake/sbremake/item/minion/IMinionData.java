package me.carscupcake.sbremake.item.minion;

import me.carscupcake.sbremake.event.PlayerInteractEvent;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.util.lootTable.ILootTable;
import me.carscupcake.sbremake.worlds.impl.PrivateIsle;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.item.ItemStack;

import java.util.HashMap;
import java.util.function.Consumer;

public interface IMinionData {
    int getLevels();
    String[] getHeadStrings();
    String name();

    /**
     * Get the drops for 1 generate
     * @return Hasmap with a bundle Item - Count and a double for the chance
     */
    ILootTable<SbItemStack> drops();

    /**
     * Is for the time between the working actions
     * @return Time in ticks
     */
    int[] timeBetweenActions();
    HashMap<EquipmentSlot, ItemStack> getEquipment();
    ItemStack getItemInHand();
    String id();

    HashMap<String, IMinionData> minions = new HashMap<>();

    class MinionPlace implements Consumer<PlayerInteractEvent> {
        private final IMinionData minion;
        private final int level;
        public MinionPlace(IMinionData minion, int level){
            this.minion = minion;
            this.level = level;
        }

        @Override
        public void accept(PlayerInteractEvent event) {
            if (event.block() == null) return;
            if (!(event.player().getWorldProvider() instanceof PrivateIsle privateIsle)) return;
            if(!privateIsle.addMinion(minion, level, new Pos(event.block())))
                event.player().sendMessage("§cYou already placed the maximum amount of minions!");
            else
                event.player().setItemInHand(PlayerHand.MAIN, SbItemStack.AIR);
        }
    }
}
