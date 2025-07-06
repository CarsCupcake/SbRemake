package me.carscupcake.sbremake.item.minion;


import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public abstract class AbstractCombatMinionData implements IMinionData {
    protected abstract MinionEntity getEntity();

    @Override
    public ItemStack getItemInHand() {
        return ItemStack.of(Material.WOODEN_SWORD);
    }

}
