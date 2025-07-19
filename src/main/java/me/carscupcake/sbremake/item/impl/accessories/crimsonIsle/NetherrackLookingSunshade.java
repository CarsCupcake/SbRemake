package me.carscupcake.sbremake.item.impl.accessories.crimsonIsle;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.impl.AbstractAccessory;
import net.minestom.server.item.Material;

import java.util.Map;

public class NetherrackLookingSunshade extends AbstractAccessory implements HeadWithValue {

    public NetherrackLookingSunshade() {
        super(new Lore("§7Makes you invisible to Dive Ghasts."));
    }

    @Override
    public AccessoryFamily getAccessoryFamily() {
        return AccessoryFamily.NetherrackLookingSunshade;
    }

    @Override
    public String getId() {
        return "NETHERRACK_LOOKING_SUNSHADE";
    }

    @Override
    public String getName() {
        return "Netherrack-Looking Sunshade";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.COMMON;
    }

    @Override
    public String value() {
        return "eyJ0aW1lc3RhbXAiOjE1NTczMzMyNjkxMDIsInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJ4RmFpaUxlUiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmM5MzBiMGEyZDg3ZjJkZGRiNDA5YTMwMmY5NTkxODk3MTg0ZjI5ZWNjOTdiNWNhNWNkZWMxY2Q2OTgzOTU5NyJ9fX0=";
    }

    @Override
    public boolean isUnstackable() {
        return true;
    }
}
