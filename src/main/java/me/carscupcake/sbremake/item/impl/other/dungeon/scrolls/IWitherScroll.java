package me.carscupcake.sbremake.item.impl.other.dungeon.scrolls;

import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import me.carscupcake.sbremake.item.impl.sword.dungeons.NecronBlade;
import me.carscupcake.sbremake.item.smithing.SmithingItem;
import net.minestom.server.item.Material;

import java.util.ArrayList;

public interface IWitherScroll extends ISbItem, SmithingItem, NpcSellable {

    @Override
    default SbItemStack onApply(SbItemStack left, SbItemStack right) {
        var abilities = new ArrayList<>(left.getModifier(NecronBlade.WITHER_SCROLLS));
        abilities.add(getBladeAbility());
        return left.withModifier(NecronBlade.WITHER_SCROLLS, abilities);
    }

    @Override
    default boolean canBeApplied(SbItemStack other) {
        var scrolls = other.getModifier(NecronBlade.WITHER_SCROLLS);
        return other.sbItem() instanceof NecronBlade && (scrolls == null || !scrolls.contains(getBladeAbility()));
    }

    @Override
    default Material getMaterial() {
        return Material.WRITABLE_BOOK;
    }

    default ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    default int sellPrice() {
        return 1;
    }

    @Override
    default ItemType getType() {
        return ItemType.None;
    }

    @Override
    default ItemStackModifiers modifierBuilder() {
        return ItemStackModifiers.ENCHANT_GLINT_MODIFIERS;
    }

    @Override
    default boolean isUnstackable() {
        return true;
    }

    @Override
    default Lore getLore() {
        return new Lore("""
               §7Applies the §%s Ability§7 when combined with a §6Necron's Blade (Unrefined)§7.
               \s
               !a!""".formatted(getBladeAbility().name()), "!a!", (item, player) -> String.join("\n", getBladeAbility().buildLore(item, player)));
    }

    ItemAbility<?> getBladeAbility();

    @Override
    default boolean isDungeonItem() {
        return true;
    }
}
