package me.carscupcake.sbremake.entity.slayer.zombie;

import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.slayer.zombie.ScytheBlade;
import me.carscupcake.sbremake.item.impl.rune.Rune;
import me.carscupcake.sbremake.item.impl.rune.impl.SnakeRune;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.item.modifiers.RuneModifier;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterEntry;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterItemLoot;
import me.carscupcake.sbremake.util.lootTable.rngMeter.SlayerLootTable;

import static me.carscupcake.sbremake.entity.slayer.zombie.RevenantHorrorI.REVENANT_FLESH;

public class RevenantHorrorIV extends RevenantHorrorIII {
    public static final RngMeterEntry SNAKE_RUNE = new RngMeterEntry("SNAKE_RUNE",
            new RngMeterItemLoot(SbItemStack.from(Rune.class).withModifier(Modifier.RUNE, new RuneModifier(new SnakeRune(), 1)), SlayerLootTable.LootTableType.Extra, Slayers.Zombie, 20));
    public static final RngMeterEntry SCYTHE_BLADE = new RngMeterEntry("SCYTHE_BLADE",
            new RngMeterItemLoot(SbItemStack.from(ScytheBlade.class), SlayerLootTable.LootTableType.Main, Slayers.Zombie, 7),
            new RngMeterItemLoot(SbItemStack.from(ScytheBlade.class), SlayerLootTable.LootTableType.Main, Slayers.Zombie, 15));

    public static final SlayerLootTable lootTable;

    static {
        lootTable = new SlayerLootTable();
        lootTable.addLoot(REVENANT_FLESH.loots()[3]);
        lootTable.addLoot(RevenantHorrorII.FOUL_FLESH.loots()[2]);
        lootTable.addLoot(RevenantHorrorII.PESTILENCE_RUNE.loots()[2]);
        lootTable.addLoot(RevenantHorrorII.UNDEAD_CATALYST.loots()[1]);
        lootTable.addLoot(RevenantHorrorII.REVENANT_CATALYST.loots()[0]);
        lootTable.addLoot(RevenantHorrorIII.BEHEADED_HORROR.loots()[1]);
        lootTable.addLoot(RevenantHorrorIII.SMITE_VI.loots()[1]);
        lootTable.addLoot(RevenantHorrorIV.SNAKE_RUNE.loots()[0]);
        lootTable.addLoot(RevenantHorrorIV.SCYTHE_BLADE.loots()[0]);
    }

    public RevenantHorrorIV(SkyblockPlayer owner) {
        super(owner, lootTable);
    }

    @Override
    public float getMaxHealth() {
        return 1_500_000;
    }

    @Override
    public double getDamage() {
        return 400;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 500;
    }

    @Override
    public int getTier() {
        return 4;
    }
}
