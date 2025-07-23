package me.carscupcake.sbremake.util;

import net.kyori.adventure.internal.properties.AdventureProperties;
import net.minestom.server.instance.block.Block;

import java.util.Map;

public record PalletItem(Block block, Map<String, String> propertyMap, String headValue) {
}
