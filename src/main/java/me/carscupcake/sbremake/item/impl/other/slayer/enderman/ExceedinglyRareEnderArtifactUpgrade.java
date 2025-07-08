package me.carscupcake.sbremake.item.impl.other.slayer.enderman;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class ExceedinglyRareEnderArtifactUpgrade implements ISbItem, HeadWithValue {
    @Override
    public String getId() {
        return "EXCEEDINGLY_RARE_ENDER_ARTIFACT_UPGRADER";
    }

    @Override
    public String getName() {
        return "Exceedingly Rare Ender Artifact Upgrade";
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
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTYxNjQzNTAxODQ5MywKICAicHJvZmlsZUlkIiA6ICIwZjczMDA3NjEyNGU0NGM3YWYxMTE1NDY5YzQ5OTY3OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPcmVfTWluZXIxMjMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI1OTIzMWE5NDY5ODdlYTUzMTQxNzg5YTA5NDk2ZjA5OGQ2ZWNhYzQxMmEwMWUwYTI0YzkwNmE5OWZkYmQ5YSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
    }
}
