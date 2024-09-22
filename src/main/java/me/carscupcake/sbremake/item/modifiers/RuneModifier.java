package me.carscupcake.sbremake.item.modifiers;

import me.carscupcake.sbremake.item.impl.rune.IRune;
import net.minestom.server.entity.Entity;

public record RuneModifier(IRune<? extends Entity> rune, int level) {
}
