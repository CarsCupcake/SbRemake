package me.carscupcake.sbremake.item.impl.armor.slayer.zombie;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.requirements.SlayerRequirement;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class CrystallizedHeart implements ISbItem, HeadWithValue, ISbItem.StatProvider {
    @Override
    public String value() {
        return "eyJ0aW1lc3RhbXAiOjE1NjgzNTg1OTU4OTEsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg3ZGZiN2MxZWU0ZGUzMWY1NDkzMWVhYzVjNjU3YzE0NWU0ZmE3ZmEwOWUzZjUyYjE3ODhhNjgyYjY1YWM3NSJ9fX0=";
    }

    @Override
    public String getId() {
        return "CRYSTALLIZED_HEART";
    }

    @Override
    public String getName() {
        return "Crystallized Heart";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Helmet;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public List<Requirement> requirements() {
        return List.of(new SlayerRequirement(Slayers.Zombie, 3));
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 50, Stat.Defense, 10, Stat.Intelligence, 50);
    }
}
