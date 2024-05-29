package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.Stat;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import org.jglrxavpok.hephaistos.nbt.NBT;

import java.util.Map;

public interface ISbItem {
    String getId();
    String getName();
    Material getMaterial();
    ItemType getType();
    ItemRarity getRarity();
    default boolean allowUpdates() {
        return true;
    }
    default double getStat(Stat stat) {
        return 0;
    }
    default Lore getLore() {
        return Lore.EMPTY;
    }
    default LorePlace getLorePlacement() {
        return LorePlace.AboveAbility;
    }
    default SbItemStack create() {
        ItemStack item = ItemStack.builder(getMaterial()).set(Tag.NBT("ExtraAttributes"), NBT.Compound(mutableNBTCompound -> mutableNBTCompound.put("id", NBT.String(getId())))).build();
        SbItemStack itemStack = SbItemStack.from(item);
        return itemStack.update();
    }
    enum LorePlace {
        Top,
        AboveAbility,
        BelowAbility
    }
}
