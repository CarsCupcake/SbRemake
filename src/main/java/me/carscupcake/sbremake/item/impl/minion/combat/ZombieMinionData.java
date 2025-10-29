package me.carscupcake.sbremake.item.impl.minion.combat;

import me.carscupcake.sbremake.entity.impl.privateIsle.Zombie;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.mining.resources.EnchantedCobblestone;
import me.carscupcake.sbremake.item.minion.AbstractCombatMinionData;
import me.carscupcake.sbremake.item.minion.IMinionData;
import me.carscupcake.sbremake.item.minion.MinionEntity;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.ILootTable;
import net.minestom.server.color.Color;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.HashMap;

public class ZombieMinionData extends AbstractCombatMinionData {
    @Override
    protected MinionEntity getEntity() {
        return new Zombie();
    }

    @Override
    public int getLevels() {
        return 11;
    }

    @Override
    public String[] getHeadStrings() {
        return new String[]{
                "eyJ0aW1lc3RhbXAiOjE1NTc5MjA1OTkxMzMsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk2MDYzYTg4NGQzOTAxYzQxZjM1YjY5YThjOWY0MDFjNjFhYzlmNjMzMGY5NjRmODBjMzUzNTJjM2U4YmZiMCJ9fX0=",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzYwNDgzOTMsInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJ4RmFpaUxlUiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzAxNjEzYmEyZTk5ZWU4MzI2YjVjZWFlNzdlZmIxZTlhZmE2YWU1NDFmMzhiNGVkNjNlNzllY2IwMWU3MjVmMCJ9fX0=",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzYwNzI1MTIsInByb2ZpbGVJZCI6IjNmYzdmZGY5Mzk2MzRjNDE5MTE5OWJhM2Y3Y2MzZmVkIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Q2ZmRkOGQ1NGJjM2ExMDliN2UwNmJhYWYxYjBhYzk3ZmIyMjk4OWFhOTMwNjliNjNjY2E4MTdmZjdmZDc0NjMifX19",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzYxMjg1OTYsInByb2ZpbGVJZCI6ImIwZDczMmZlMDBmNzQwN2U5ZTdmNzQ2MzAxY2Q5OGNhIiwicHJvZmlsZU5hbWUiOiJPUHBscyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmZiZWMxYmQwZmUzYjcxYjlkYTlkNzY2NmZkNmJiZGUzNDFiNGM0ODFlODU2M2ZkZGY2MWY0ZWU1MmY3Y2QxYiJ9fX0=",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzYyOTA0NjMsInByb2ZpbGVJZCI6IjJkYzc3YWU3OTQ2MzQ4MDI5NDI4MGM4NDIyNzRiNTY3IiwicHJvZmlsZU5hbWUiOiJzYWR5MDYxMCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjdhMTk0NWI1Mjc2MTQ0M2QxYTdkZTIzM2E0ZTRhZWE0MGM5YWJhZDkyYWU5YWMzNWUzODU0Nzg5NzE5NTZhZSJ9fX0=",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzYzMDM0NDIsInByb2ZpbGVJZCI6ImZkNjBmMzZmNTg2MTRmMTJiM2NkNDdjMmQ4NTUyOTlhIiwicHJvZmlsZU5hbWUiOiJSZWFkIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hOGMzYWI0MmQzMjdmYTAxMjcxZjlmMTk5NThjNzdlMGRlZTlmZGU1NzQxNWY4NzM3ODM3MzdhMWU4M2Y0ZTg2In19fQ==",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzYzMjkwNzAsInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJ4RmFpaUxlUiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTA1OGYwODkxMGIzOWMzMDY0NGYzM2ZkNzFmODFhNDEyZjZlMDVmZTdjNzAzYTg3ZmQ0ZjNkNWU0YjJiNjUwOSJ9fX0=",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzYzNDM2MDIsInByb2ZpbGVJZCI6Ijc1MTQ0NDgxOTFlNjQ1NDY4Yzk3MzlhNmUzOTU3YmViIiwicHJvZmlsZU5hbWUiOiJUaGFua3NNb2phbmciLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U0MGIyMGFiYTViM2MyNzlkZWU0MmIzOWQ4ZTAzZGUyNWNiZWFkMzQyMTY1NWYwY2YxYmVhNDNlZDBiNDI3MmUifX19",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzYzNTgyOTIsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNiZjE3NjgxZTU3OWYwMGQ2NWY5NzhjMGI1MDkxNWFhZjJkNWY2MDlkYTdkOWFiMTU2Y2I2ZjA5MmI4ODg0MCJ9fX0=",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzYzNzE0OTAsInByb2ZpbGVJZCI6IjgyYzYwNmM1YzY1MjRiNzk4YjkxYTEyZDNhNjE2OTc3IiwicHJvZmlsZU5hbWUiOiJOb3ROb3RvcmlvdXNOZW1vIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82YjRhOWRjNmQwZmRiZDFiYWQzNjEzZGNiM2FiNWM1NGM1ZWE1ZTBiNDk4ZWUzNWIyZmQzMDk1MWNjMmU5ZmNkIn19fQ==",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzYzOTE2MjksInByb2ZpbGVJZCI6IjNmYzdmZGY5Mzk2MzRjNDE5MTE5OWJhM2Y3Y2MzZmVkIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzY2OTlmZjVjZTlhMGY1MDMyMzQwNTk2ZjZiMmRkNmFjNzAyOGZjN2NjNWI5NDNkNGMxZmMyZDM3NDlmZWRjZDYifX19"
        };
    }
    @Override
    public Cost[][] costs() {
        return IMinionData.generateDefaultCost(ISbItem.get(Material.ROTTEN_FLESH), ISbItem.get(EnchantedCobblestone.class));
    }

    @Override
    public String name() {
        return "Zombie Minion";
    }

    @Override
    public ILootTable<SbItemStack> drops() {
        return Zombie.lootTable;
    }

    @Override
    public int[] timeBetweenActions() {
        return new int[]{
                26*20,
                24*20,
                24*20,
                22*20,
                22*20,
                20*20,
                20*20,
                17*20,
                17*20,
                13*20,
                13*20
        };
    }

    @Override
    public HashMap<EquipmentSlot, ItemStack> getEquipment() {
        HashMap<EquipmentSlot, ItemStack> equipment = new HashMap<>();
        equipment.put(EquipmentSlot.CHESTPLATE, new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherColor(new Color(0x136948)).build());
        equipment.put(EquipmentSlot.LEGGINGS, new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherColor(new Color(0x136948)).build());
        equipment.put(EquipmentSlot.BOOTS, new ItemBuilder(Material.LEATHER_BOOTS).setLeatherColor(new Color(0x136948)).build());
        return equipment;
    }

    @Override
    public String id() {
        return "ZOMBIE";
    }
}
