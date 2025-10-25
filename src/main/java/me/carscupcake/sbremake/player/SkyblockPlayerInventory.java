package me.carscupcake.sbremake.player;

import me.carscupcake.sbremake.config.ConfigField;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.config.DefaultConfigItem;
import me.carscupcake.sbremake.event.SbEntityEquipEvent;
import me.carscupcake.sbremake.item.SbItemStack;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.inventory.InventoryItemChangeEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.MathUtils;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Arrays;
import java.util.Iterator;

public class SkyblockPlayerInventory extends PlayerInventory implements Iterable<SbItemStack>, DefaultConfigItem {
    private static final VarHandle ITEM_UPDATER = MethodHandles.arrayElementVarHandle(SbItemStack[].class);
    @ConfigField
    private SbItemStack[] itemStacks;
    private final SkyblockPlayer player;

    public SkyblockPlayerInventory(@NotNull SkyblockPlayer player) {
        super();
        this.player = player;
        itemStacks = new SbItemStack[46];
        Arrays.fill(itemStacks, SbItemStack.AIR);
    }

    @Override
    public void load(ConfigSection section) {
        DefaultConfigItem.super.load(section);
    }

    public void updateSbItemStacks() {
        for (int i = 0; i < itemStacks.length; i++) {
            var item = itemStacks[i].update(player);
            itemStacks[i] = item;
            super.itemStacks[i] = item.item();
        }
    }

    @Override
    protected void UNSAFE_itemInsert(int slot, @NotNull ItemStack item, @NotNull ItemStack previous, boolean sendPacket) {
        UNSAFE_itemInsert(slot, SbItemStack.from(item), sendPacket);
    }

    protected void UNSAFE_itemInsert(int slot, @NotNull SbItemStack itemStack, boolean sendPacket) {
        EquipmentSlot var10000;
        switch (slot) {
            case 41 -> var10000 = EquipmentSlot.HELMET;
            case 42 -> var10000 = EquipmentSlot.CHESTPLATE;
            case 43 -> var10000 = EquipmentSlot.LEGGINGS;
            case 44 -> var10000 = EquipmentSlot.BOOTS;
            case 45 -> var10000 = EquipmentSlot.OFF_HAND;
            default -> var10000 = slot == this.player.getHeldSlot() ? EquipmentSlot.MAIN_HAND : null;
        }

        EquipmentSlot equipmentSlot = var10000;
        if (equipmentSlot != null) {
            SbEntityEquipEvent entityEquipEvent = new SbEntityEquipEvent(this.player, itemStack, equipmentSlot);
            EventDispatcher.call(entityEquipEvent);
            itemStack = entityEquipEvent.getSbItemStack();
            this.player.updateEquipmentAttributes(this.itemStacks[slot].item(), itemStack.item(), equipmentSlot);
        }

        this.itemStacks[slot] = itemStack;
        super.itemStacks[slot] = itemStack.item();
        if (sendPacket) {
            if (equipmentSlot != null) {
                this.player.syncEquipment(equipmentSlot);
            }

            this.sendSlotRefresh(slot, itemStack.item());
        }

    }

    /**
     * @param slot the slot for the item
     * @return the item at the slot
     * @deprecated Try using getSbItemStack instead
     */
    @Deprecated
    public @NotNull ItemStack getItemStack(int slot) {
        return getSbItemStack(slot).item();
    }

    public SbItemStack getSbItemStack(int slot) {
        return (SbItemStack) ITEM_UPDATER.getVolatile(itemStacks, slot);
    }

    /**
     * @param slot the slot for the item
     * @return the item at the slot
     * @deprecated Try using getSbItemStack instead
     */
    @Deprecated
    public @NotNull ItemStack getEquipment(@NotNull EquipmentSlot slot) {
        return getSbEquipment(slot).item();
    }

    @Deprecated
    public void setEquipment(@NotNull EquipmentSlot slot, @NotNull ItemStack itemStack) {
        setEquipment(slot, SbItemStack.from(itemStack));
    }

    public void setEquipment(@NotNull EquipmentSlot slot, @NotNull SbItemStack itemStack) {
        if (slot == EquipmentSlot.BODY) {
            Check.fail("PlayerInventory does not support body equipment");
        }

        this.safeItemInsert(this.getSlotId(slot), itemStack, true);
    }

    public SbItemStack getSbEquipment(EquipmentSlot slot) {
        return slot == EquipmentSlot.BODY ? SbItemStack.from(ItemStack.AIR) : this.getSbItemStack(this.getSlotId(slot));
    }

    private int getSlotId(@NotNull EquipmentSlot slot) {
        int var10000;
        switch (slot) {
            case MAIN_HAND -> var10000 = this.player.getHeldSlot();
            case OFF_HAND -> var10000 = 45;
            default -> var10000 = slot.armorSlot();
        }

        return var10000;
    }

    @Deprecated
    public void setItemStack(int slot, @NotNull ItemStack itemStack) {
        setItemStack(slot, SbItemStack.from(itemStack));
    }

    public void setItemStack(int slot, @NotNull SbItemStack itemStack) {
        Check.argCondition(!MathUtils.isBetween(slot, 0, this.getSize()), "Inventory does not have the slot " + slot);
        this.safeItemInsert(slot, itemStack, true);
    }

    protected final void safeItemInsert(int slot, @NotNull SbItemStack itemStack, boolean sendPacket) {
        SbItemStack previous;
        synchronized (this) {
            Check.argCondition(!MathUtils.isBetween(slot, 0, this.getSize()), "The slot {0} does not exist in this inventory", new Object[]{slot});
            previous = this.itemStacks[slot];
            if (itemStack.equals(previous)) {
                return;
            }

            this.UNSAFE_itemInsert(slot, itemStack, sendPacket);
        }

        EventDispatcher.call(new InventoryItemChangeEvent(this, slot, previous.item(), itemStack.item()));

    }

    @Override
    public @NotNull Iterator<SbItemStack> iterator() {
        return new  Iterator<>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return itemStacks.length > i;
            }

            @Override
            public SbItemStack next() {
                return itemStacks[i++];
            }
        };
    }
}
