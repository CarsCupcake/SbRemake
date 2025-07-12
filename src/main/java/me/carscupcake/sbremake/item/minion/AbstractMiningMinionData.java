package me.carscupcake.sbremake.item.minion;

import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.Lazy;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public abstract class AbstractMiningMinionData implements IMinionData {
    public abstract Block representiveBlock();

    private final Lazy<Cost[][]> costs = new Lazy<>(() -> new Cost[][]{});

    public abstract Cost[][] costs();
    @Override
    public Cost[][] getCosts() {
        return costs.get();
    }

    @Override
    public ItemStack getItemInHand() {
        return ItemStack.of(Material.WOODEN_PICKAXE);
    }

}
