package me.carscupcake.sbremake.item.impl.other.slayer.enderman;

import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.item.smithing.SmithingItem;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

import java.util.Objects;
import java.util.Set;

public class TransmitionTuner implements ISbItem, HeadWithValue, SmithingItem, NpcSellable {
    @Override
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTcyMDA0MjAxNzkwNSwKICAicHJvZmlsZUlkIiA6ICIxMWNkOTMzYmU1OGQ0MzYxOTdlYTc1MjU4MzM2Mzg0YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNb29ubGl0TUMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTI2MjYxNzhiODRkYmQzOTRjZjI0MDk0NjcyMTAyODJlNTIxMDMyMTc2ZGIyMWIyODFmZGM4YWU5Y2JlMmIyZiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
    }

    @Override
    public String getId() {
        return "TRANSMISSION_TUNER";
    }

    @Override
    public String getName() {
        return "Transmission Tuner";
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
        return ItemRarity.EPIC;
    }

    private static final Set<String> IDS = Set.of("ASPECT_OF_THE_END", "ASPECT_OF_THE_VOID", "SINSEEKER_SCYTHE");

    @Override
    public SbItemStack onApply(SkyblockPlayer player, SbItemStack left, SbItemStack right) {
        return left.withModifier(TRANSMITION_TUNERS, left.getModifier(TRANSMITION_TUNERS) + 1);
    }

    @Override
    public boolean canBeApplied(SkyblockPlayer player, SbItemStack other) {
        if (!IDS.contains(other.sbItem().getId()))
            return false;
        return other.getModifier(TRANSMITION_TUNERS) < 4;
    }

    @Override
    public int sellPrice() {
        return 45_000;
    }

    public static Modifier<Integer> TRANSMITION_TUNERS = new  Modifier<>() {

        @Override
        public Integer getFromNbt(SbItemStack item) {
            var data = item.item().get(DataComponents.CUSTOM_DATA);
            if (data == null) return 0;
            if (data.hasTag(Tag.Integer("tuned_transmition"))) return 0;
            return data.getTag(Tag.Integer("tuned_transmition"));
        }

        @Override
        public SbItemStack toNbt(Integer integer, SbItemStack itemStack) {
            return SbItemStack.from(itemStack.item().with(DataComponents.CUSTOM_DATA,
                                                          Objects.requireNonNull(itemStack.item().get(DataComponents.CUSTOM_DATA))
                                                                  .withTag(Tag.Integer("tuned_transmition")
                                                                  , integer)));
        }
    };
}
