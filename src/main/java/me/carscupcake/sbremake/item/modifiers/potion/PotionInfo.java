package me.carscupcake.sbremake.item.modifiers.potion;

import me.carscupcake.sbremake.player.potion.IPotion;
import me.carscupcake.sbremake.player.potion.PotionType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record PotionInfo(IPotion potion, boolean enhanced, boolean extended, byte potionLevel, PotionType potionType,
                         @Nullable String customPotionName, List<PotionEffect> effects) {
    public record PotionEffect(IPotion potion, byte level, long durationTicks) {

    }
}
