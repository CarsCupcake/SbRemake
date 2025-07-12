package me.carscupcake.sbremake.item.impl.accessories.slayer.enderman;

import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.impl.AbstractAccessory;
import me.carscupcake.sbremake.item.requirements.SlayerRequirement;
import net.minestom.server.item.Material;

import java.util.List;

public class HandyBloodChalice extends AbstractAccessory implements HeadWithValue {
    @Override
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTY1MjMzNTQwMTA2OCwKICAicHJvZmlsZUlkIiA6ICIzOTg5OGFiODFmMjU0NmQxOGIyY2ExMTE1MDRkZGU1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICI4YjJjYTExMTUwNGRkZTUwIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzQzMWNkN2VkNGU0YmYwN2MzZGZkOWJhNDk4NzA4ZTczMGU2OWQ4MDczMzVhZmZhYmMxMmQ4N2ZmNTQyZjZhODgiCiAgICB9CiAgfQp9";
    }

    @Override
    public AccessoryFamily getAccessoryFamily() {
        return AccessoryFamily.PocketEspressoMachine;
    }

    @Override
    public String getId() {
        return "HANDY_BLOOD_CHALICE";
    }

    @Override
    public String getName() {
        return "Handy Blood Chalice";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.COMMON;
    }

    private final List<Requirement> requirements = List.of(new SlayerRequirement(Slayers.Enderman, 5));

    @Override
    public List<Requirement> requirements() {
        return requirements;
    }
}
