package me.carscupcake.sbremake.item.impl.minion.mining;


import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.mining.resources.EnchantedCobblestone;
import me.carscupcake.sbremake.item.minion.AbstractMiningMinionData;
import me.carscupcake.sbremake.item.minion.IMinionData;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.ILootTable;
import me.carscupcake.sbremake.util.lootTable.ItemLoot;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import net.minestom.server.color.Color;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.HashMap;

public class CobblestoneMinionData extends AbstractMiningMinionData {
    @Override
    public Block representiveBlock() {
        return Block.COBBLESTONE;
    }

    @Override
    public Cost[][] costs() {
        return IMinionData.generateDefaultCost(ISbItem.get(Material.COBBLESTONE), ISbItem.get(EnchantedCobblestone.class));
    }

    @Override
    public int getLevels() {
        return 12;
    }

    @Override
    public String[] getHeadStrings() {
        return new String[]{
                "eyJ0aW1lc3RhbXAiOjE1NTc5MTkyNzc1NzMsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmY5MzI4OWE4MmJkMmEwNmNiYmU2MWI3MzNjZmRjMWYxYmQ5M2M0MzQwZjdhOTBhYmQ5YmRkYTc3NDEwOTA3MSJ9fX0=",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzQ0ODI4MjYsInByb2ZpbGVJZCI6IjNmYzdmZGY5Mzk2MzRjNDE5MTE5OWJhM2Y3Y2MzZmVkIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzNmZDg3NDg2ZGM5NGNiOGNkMDRhM2Q3ZDA2ZjE5MWYwMjdmMzhkYWQ3YjRlZDM0YzY2ODFmYjRkMDg4MzRjMDYifX19",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzQ0OTM1ODgsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MwODhlZDZiYjg3NjNhZjRlYjdkMDA2ZTAwZmRhN2RjMTFkNzY4MWU5N2M5ODNiNzAxMWMzZTg3MmY2YWFiOSJ9fX0=",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzQ1MDY5NTgsInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJ4RmFpaUxlUiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzk1MTRmZWU5NWQ3MDI2MjViOTc0ZjE3MzBmZDYyZTU2N2M1OTM0OTk3ZjczYmFlN2UwN2FiNTJkZGY5MDY2ZSJ9fX0=",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzQ1MTc2MTQsInByb2ZpbGVJZCI6IjU3MGIwNWJhMjZmMzRhOGViZmRiODBlY2JjZDdlNjIwIiwicHJvZmlsZU5hbWUiOiJMb3JkU29ubnkiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzNlMjQ2N2I4Y2NhZjAwN2QwM2E5YmI3YzIyZDZhNjEzOTdjYTFiYjI4NGYxMjhkNWNjZDEzOGFkMDkxMjRlNjgifX19",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzQ1NTE5MTYsInByb2ZpbGVJZCI6ImRkZWQ1NmUxZWY4YjQwZmU4YWQxNjI5MjBmN2FlY2RhIiwicHJvZmlsZU5hbWUiOiJEaXNjb3JkQXBwIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mNGUwMWY1NTI1NDkwMzdhZTg4ODc1NzA3MDBlNzRkYjIwYzZmMDI2YTY1MGFlZWM1ZDljOGVjNTFiYTNmNTE1In19fQ==",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzQ1NzU2NTUsInByb2ZpbGVJZCI6IjNmYzdmZGY5Mzk2MzRjNDE5MTE5OWJhM2Y3Y2MzZmVkIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUxNjE2ZTYzYmUwZmYzNDFmNzA4NjJlMDA0OTgxMmZhMGMyN2IzOWEyZTc3MDU4ZGQ4YmZjMzg2Mzc1ZTFkMTYifX19",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzQ1ODY1ODAsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE1M2UzYzlmNDQ2YTc3ZThjNTlkZjMwNWE0MTBhOGFjY2I3NTFjMDAyYTQxZTU1YTEwMThjZTFiMzExNDY5MCJ9fX0=",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzQ1OTY5NTEsInByb2ZpbGVJZCI6ImNiZGViZGRjODNhNTQ0OWFiZDFiOThhNzBjY2E0ZDhlIiwicHJvZmlsZU5hbWUiOiJDaGVja2lkb2lzIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jY2Y1NDY1ODQ0MjhiNTM4NWJjMGMxYTAwMzFhYTg3ZTk4ZTg1ODc1ZTRkNjEwNGUxYmUwNmNlZjhiZDc0ZmU0In19fQ==",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzQ2MTczMjksInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJ4RmFpaUxlUiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg5ZGIwYTljOTdmMGUwYjViYjllYzdiM2UzMmY4ZDYzYzY0OGQ0NjA4Y2ZkNWJlOWFkYmU4ODI1ZDRlNmE5NCJ9fX0=",
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzQ2Mjk2MTIsInByb2ZpbGVJZCI6IjNmYzdmZGY5Mzk2MzRjNDE5MTE5OWJhM2Y3Y2MzZmVkIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ViY2MwOTlmM2EwMGVjZTBlNWM0YjMxZDMxYzgyOGU1MmIwNjM0OGQwYTRlYWMxMWYzZmNiZWYzYzA1Y2I0MDcifX19",
                "ewogICJ0aW1lc3RhbXAiIDogMTYxMDY1NjI0ODE3NCwKICAicHJvZmlsZUlkIiA6ICJlZGQyZDgyZmY3Zjg0ZGE5OTc0Yjk1YWNhMmYzM2RjMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJtaW5lZ2xvemUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg4ZDVhN2RiNTNkOTA0ODgyNTc4NTllZTY2NDUzOWUxM2EwZmYzYTI5ZTg1N2RlN2RiODhkZjA3NzQ3OWRhZSIKICAgIH0KICB9Cn0="
        };
    }

    @Override
    public String name() {
        return "Cobblestone Minion";
    }

    private final LootTable<SbItemStack> lootTable = new LootTable<SbItemStack>().addLoot(new ItemLoot(Material.COBBLESTONE));
    @Override
    public ILootTable<SbItemStack> drops() {
        return lootTable;
    }

    @Override
    public int[] timeBetweenActions() {
        return new int[]{
                14*20,
                14*20,
                12*20,
                12*20,
                10*20,
                10*20,
                9*20,
                9*20,
                8*20,
                8*20,
                7*20,
                6*20
        };
    }

    @Override
    public HashMap<EquipmentSlot, ItemStack> getEquipment() {
        HashMap<EquipmentSlot, ItemStack> equipment = new HashMap<>();
        equipment.put(EquipmentSlot.CHESTPLATE, new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherColor(new Color(8026468)).build());
        equipment.put(EquipmentSlot.LEGGINGS, new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherColor(new Color(8026468)).build());
        equipment.put(EquipmentSlot.BOOTS, new ItemBuilder(Material.LEATHER_BOOTS).setLeatherColor(new Color(8026468)).build());
        return equipment;
    }

    @Override
    public String id() {
        return "COBBLESTONE";
    }
}
