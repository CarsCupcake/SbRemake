package me.carscupcake.sbremake.player.hotm;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

public abstract class PickaxeAbility extends HotmUpgrade {
    public PickaxeAbility(SkyblockPlayer player, Class<? extends HotmUpgrade>... priorUpgrades) {
        super(player, priorUpgrades);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.MithrilPowder;
    }

    @Override
    public int nextLevelCost(int current) {
        return 0;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(level == 0 ? Material.COAL_BLOCK : Material.EMERALD_BLOCK)
                .setGlint(getPlayer().getHotm().getActiveAbility() == this)
                .setName(STR."\{level == 0 ? "§c" : "§a"}\{getName()}")
                .addAllLore(" ", STR."§6Pickaxe Ability: \{getName()}")
                .addAllLore(lore(level == 0 ? 1 : level).build(null, getPlayer()))
                .addLoreRow(STR."§8Cooldown: §a\{cooldown()}s")
                .addLoreRow("  ")
                .addLoreIf(() -> level != 0 && getPlayer().getHotm().getActiveAbility() == this, "§a§lSELECTED")
                .addLoreIf(() -> level != 0 && getPlayer().getHotm().getActiveAbility() != this, "§eClick to select!")
                .addAllLore()
                .build();
    }

    public abstract int cooldown();

    public abstract void onInteract();
}
