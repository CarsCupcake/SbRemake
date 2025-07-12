package me.carscupcake.sbremake.item.impl.reforgeStone;

import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.item.modifiers.reforges.Reforge;
import me.carscupcake.sbremake.item.smithing.SmithingItem;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.ArrayUtil;
import me.carscupcake.sbremake.util.Cost;

import java.util.List;
import java.util.Map;

public interface IReforgeStone extends ISbItem, SmithingItem {
    Reforge getReforge();
    List<Cost> getCosts(ItemRarity rarity);
    @Override
    default List<Requirement> requirements() {
        return getReforge().requirements();
    }

    @Override
    default ItemType getType() {
        return ItemType.ReforgeStone;
    }

    @Override
    default SbItemStack onApply(SkyblockPlayer player, SbItemStack left, SbItemStack right) {
        for (Cost cost : getCosts(left.getRarity())) {
            cost.pay(player);
        }
        return left.withModifier(Modifier.REFORGE, getReforge());
    }
    @Override
    default boolean canBeApplied(SkyblockPlayer player, SbItemStack other) {
        for (Cost cost : getCosts(other.getRarity())) {
            if (!cost.canPay(player)) return false;
         }
        return getReforge().canApply(player, other);
    }
    Lore reforgeLore = new Lore("""
                               §8Reforge Stone
                               Combinable in Reforge Anvil
                                 \s
                               §7Applies the §9%reforge%§7 reforge when combined with armor.
                                  \s
                               %breakdown%
                               """, Map.of(
            "%reforge%", (item, _) -> ((IReforgeStone) item.sbItem()).getReforge().getName(),
            "%breakdown%", (item, player) -> {
                var reforgeStone = (IReforgeStone) item.sbItem();
                StringBuilder builder = new StringBuilder("§9").append(reforgeStone.getReforge().getName()).append("§7(§6Legendary§7):");
                for (var stat : reforgeStone.getReforge().getStats().entrySet()) {
                    builder.append("§7").append(stat.getKey().getName()).append(": ").append(ArrayUtil.contains(SbItemStack.greenStats,
                                                                                                               stat.getKey()) ? "§a" :
                                                                                         "§c").append(stat.getValue().legendary() > 0 ?
                                                                                                                       "+" : "").append(stat.getValue().legendary()).append(
                            "\n");
                }
                return builder.toString();
            }
    ));
    @Override
    default Lore getLore() {
        return reforgeLore;
    }
}
