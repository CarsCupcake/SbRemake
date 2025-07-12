package me.carscupcake.sbremake.item.collections;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.item.Material;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CollectionTypes {
    Foraging("Foraging", Material.JUNGLE_SAPLING, "OAK_WOOD", "BIRCH_WOOD", "SPRUCE_WOOD", "DARK_OAK_WOOD", "ACACIA_WOOD", "JUNGLE_WOOD");
    private final String name;
    private final Material material;
    private final String[] collections;
    CollectionTypes(String name, Material showItem, String... ids) {
        collections = ids;
        this.material = showItem;
        this.name = name;
    }

    public List<Collection> getCollections(SkyblockPlayer player) {
        return Arrays.stream(collections).map(s -> player.getCollections().get(s)).collect(Collectors.toList());
    }

    public void openMenu(SkyblockPlayer player) {
        Collection.openCollectionSectionMenu(player, name, material, getCollections(player));
    }
}
