package me.carscupcake.sbremake.item.impl.accessories.slayer.enderman;

import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.impl.AbstractAccessory;
import me.carscupcake.sbremake.item.requirements.SlayerRequirement;
import net.minestom.server.item.Material;

import java.util.List;

public class PocketEspressoMachine extends AbstractAccessory implements HeadWithValue {
    @Override
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTY5Mzc3MTY1NDc2MCwKICAicHJvZmlsZUlkIiA6ICJkN2Y4OTAyMWMxNmQ0ZjFhODg3ODQyNDQyZjc3NGI0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJQYW5kYXRvSG9wZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82NjYwNzBjZTAzYTU0NWVlNGQyNjNiY2YyN2YzNjMzOGQyNDlkN2NiN2EyMzc2ZjkyYzE2NzNhZTEzNGUwNGI2IgogICAgfQogIH0KfQ=";
    }

    @Override
    public AccessoryFamily getAccessoryFamily() {
        return AccessoryFamily.PocketEspressoMachine;
    }

    @Override
    public String getId() {
        return "POCKET_ESPRESSO_MACHINE";
    }

    @Override
    public String getName() {
        return "Pocket Espresso Machine";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.COMMON;
    }

    private final List<Requirement> requirements = List.of(new SlayerRequirement(Slayers.Enderman, 4));

    @Override
    public List<Requirement> requirements() {
        return requirements;
    }
}
