package me.carscupcake.sbremake.item.minion;

import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public abstract class AbstractMiningMinionData implements IMinionData {
    public abstract Block representiveBlock();

    @Override
    public ItemStack getItemInHand() {
        return ItemStack.of(Material.WOODEN_PICKAXE);
    }
}
