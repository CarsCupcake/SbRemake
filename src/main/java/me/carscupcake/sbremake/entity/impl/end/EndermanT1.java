package me.carscupcake.sbremake.entity.impl.end;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.armor.enderArmor.EnderBoots;
import me.carscupcake.sbremake.item.impl.armor.enderArmor.EnderChestplate;
import me.carscupcake.sbremake.item.impl.armor.enderArmor.EnderHelmet;
import me.carscupcake.sbremake.item.impl.armor.enderArmor.EnderLeggings;
import me.carscupcake.sbremake.util.lootTable.ItemLoot;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;

public class EndermanT1 extends SkyblockEntity {
    public EndermanT1() {
        super(EntityType.ENDERMAN, new LootTable<SbItemStack>().addLoot(new ItemLoot(ISbItem.get(Material.ENDER_PEARL), 1, 3, 1))
                .addLootTable(new LootTable<SbItemStack>().addLoot(new ItemLoot(EnderHelmet.class))
                        .addLoot(new ItemLoot(EnderChestplate.class))
                        .addLoot(new ItemLoot(EnderLeggings.class))
                        .addLoot(new ItemLoot(EnderBoots.class)).setSingleLoot(), 0.1));
    }

    @Override
    public void spawn() {
        super.spawn();
        addAIGroup(zombieAiGroup(this, true));
    }

    @Override
    public float getMaxHealth() {
        return 4_500;
    }

    @Override
    public double getDamage() {
        return 500;
    }

    @Override
    public String getName() {
        return "Enderman";
    }

    @Override
    public int getLevel() {
        return 42;
    }
}
