package me.carscupcake.sbremake.item.impl.other.dungeon.scrolls;

import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.item.smithing.SmithingItem;
import me.carscupcake.sbremake.player.Essence;
import net.minestom.server.item.Material;

public class Recombobulator3000 implements ISbItem, SmithingItem, HeadWithValue {

    @Override
    public String getId() {
        return "RECOMBOBULATOR_3000";
    }

    @Override
    public String getName() {
        return "Recombobulator 3000";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public boolean isUnstackable() {
        return true;
    }

    @Override
    public SbItemStack onApply(SbItemStack left, SbItemStack right) {
        return left.withModifier(Modifier.RarityUpgrades, 1);
    }

    @Override
    public boolean canBeApplied(SbItemStack other) {
        return other.getModifier(Modifier.RarityUpgrades) == 0;
    }

    @Override
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTcwOTY2NDE2NjkxMSwKICAicHJvZmlsZUlkIiA6ICI5ZWEwMjA0NDZhNGM0ZTQ2ODZiOGU1ZDBiZGU4Y2U1NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTYW01MTAyMDgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTdjY2QzNmRjOGY3MmFkY2IxZjhjOGU2MWVlODJjZDk2ZWFkMTQwY2YyYTE2YTEzNjZiZTliNWE4ZTNjYzNmYyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
    }
}
