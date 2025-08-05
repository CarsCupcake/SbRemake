package me.carscupcake.sbremake.entity.impl.privateIsle;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.minion.MinionEntity;
import me.carscupcake.sbremake.util.lootTable.CoinLoot;
import me.carscupcake.sbremake.util.lootTable.ILootTable;
import me.carscupcake.sbremake.util.lootTable.ItemLoot;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;

public class Zombie extends SkyblockEntity implements MinionEntity {
    public static final ILootTable<SbItemStack> lootTable = new  LootTable<SbItemStack>()
            .addLoot(new CoinLoot(1d))
            .addLoot(new ItemLoot(Material.ROTTEN_FLESH))
            .addLoot(new ItemLoot(Material.POISONOUS_POTATO, 1, 1, 0.2))
            .addLoot(new ItemLoot(Material.POTATO, 1, 1, 0.1))
            .addLoot(new ItemLoot(Material.CARROT, 1, 1, 0.1));
    public Zombie() {
        super(EntityType.ZOMBIE, lootTable, MobType.Undead);
    }

    @Override
    public void spawn() {
        addAIGroup(zombieAiGroup(this, false));
    }

    @Override
    public float getMaxHealth() {
        return 100;
    }

    @Override
    public String getName() {
        return "Zombie";
    }

    @Override
    public String getId() {
        return "ZOMBIE";
    }

    private String minionId = null;

    @Override
    public String minionId() {
        return minionId;
    }

    @Override
    public void setMinionId(String minionId) {
        this.minionId = minionId;
    }

    @Override
    public SkyblockEntity makeNew() {
        return new Zombie();
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public double getDamage() {
        return 20;
    }
}
