package me.carscupcake.sbremake.item.minion;

import me.carscupcake.sbremake.event.PlayerInteractEvent;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.impl.PrivateIsle;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Basic methods that a minion has to provide
 */

public interface Minion {
    /**
     * Retrives the owner
     * @return the owner
     */
    UUID getOwner();

    /**
     * Starts the work progress
     */
    void startWorking();

    /**
     * removes the minion
     *
     * @param removeReason is the remove reason
     */
    void remove(@Nullable("can be null if removeReson is not PICKUP_MINION") SkyblockPlayer player, MinionRemoveReason removeReason);

    /**
     * is a check if the inventory is full
     *
     * @return true if the inventory is full
     */
    boolean isInventoryFull();

    /**
     * is for sending the message
     *
     * @param message  is the message
     * @param duration is the time in ticks how long the message is visable
     */
    void setMinionMessage(String message, long duration);

    /**
     * Show the inventory of the minion
     */
    void showInventory(SkyblockPlayer player);

    /**
     * Gets the representive stand
     *
     * @return the armor stand
     */
    MinionArmorStand getArmorStand();

    UUID getId();

    static Minion getMinion(IMinionData base, int level, Instance instance, Pos location, String minionid, UUID player) {
        if (base instanceof AbstractMiningMinionData miningMinion)
            return new MiningMinion(level, miningMinion, instance, location, minionid, player);
        if (base instanceof AbstractCombatMinionData combatMinion)
            return new CombatMinion(level, combatMinion, instance, location, minionid, player);
        return null;
    }

    EventNode<@NotNull Event> LISTENER = EventNode.all("minions")
            .addListener(PlayerBlockBreakEvent.class, event -> {
                if (!(((SkyblockPlayer) event.getPlayer()).getWorldProvider() instanceof PrivateIsle privateIsle)) return;
                for (Minion minion : privateIsle.minions.values()){
                    if(minion instanceof MiningMinion miningMinion){
                        if(miningMinion.getBlocks().contains(event.getBlock()))
                            miningMinion.checkHasSpace();
                    }
                }
            })
            .addListener(PlayerBlockPlaceEvent.class, event -> {
                if (!(((SkyblockPlayer) event.getPlayer()).getWorldProvider() instanceof PrivateIsle privateIsle)) return;
                for (Minion minion : privateIsle.minions.values()){
                    if(minion instanceof MiningMinion miningMinion){
                        if(miningMinion.getBlocks().contains(event.getBlock()))
                            miningMinion.checkHasSpace();
                    }
                }
            })
            .addListener(PlayerInteractEvent.class,  event -> {
                if (event.interaction() != PlayerInteractEvent.Interaction.Right || event.entity() == null) return;
                if (!(event.player().getWorldProvider() instanceof PrivateIsle)) return;
                if(event.entity() instanceof MinionArmorStand minionArmorStand) {
                    if (event.player().getUuid().equals(minionArmorStand.getMinion().getOwner()))
                        minionArmorStand.getMinion().showInventory(event.player());
                }
            });
}
