package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.item.SbItemStack;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.event.item.EntityEquipEvent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class SbEntityEquipEvent extends EntityEquipEvent {
    private SbItemStack sbItemStack;
    public SbEntityEquipEvent(@NotNull Entity entity, @NotNull SbItemStack equippedItem, @NotNull EquipmentSlot slot) {
        super(entity, equippedItem.item(), slot);
        sbItemStack = equippedItem;
    }

    @Override
    public void setEquippedItem(@NotNull ItemStack armorItem) {
        this.sbItemStack = SbItemStack.from(armorItem);
    }

    public @NotNull ItemStack getEquippedItem() {
        return sbItemStack.item();
    }
}
