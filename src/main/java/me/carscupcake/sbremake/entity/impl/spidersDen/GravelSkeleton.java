package me.carscupcake.sbremake.entity.impl.spidersDen;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public class GravelSkeleton extends SkyblockEntity {
    public GravelSkeleton() {
        super(EntityType.SKELETON);
        setEquipment(EquipmentSlot.MAIN_HAND, ItemStack.builder(Material.BOW).build());
        addAIGroup(skeletonAiGroup(this));
    }

    @Override
    public float getMaxHealth() {
        return 100;
    }

    @Override
    public String getName() {
        return "Gravel Skeleton";
    }

    @Override
    public double getDamage() {
        return 35;
    }

    @Override
    public int getLevel() {
        return 2;
    }
}
