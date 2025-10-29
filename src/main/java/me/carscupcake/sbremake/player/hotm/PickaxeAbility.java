package me.carscupcake.sbremake.player.hotm;

import lombok.Getter;
import me.carscupcake.sbremake.event.PlayerInteractEvent;
import me.carscupcake.sbremake.item.ability.AbilityType;
import me.carscupcake.sbremake.item.ability.CooldownRequirement;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;

@Getter
public abstract class PickaxeAbility extends HotmUpgrade {
    private final ItemAbility<PlayerInteractEvent> ability;

    @SafeVarargs
    public PickaxeAbility(SkyblockPlayer player, Class<? extends HotmUpgrade>... priorUpgrades) {
        super(player, priorUpgrades);
        ability = new ItemAbility<>(getName(), AbilityType.RIGHT_CLICK, ignored -> {
            player.sendMessage("§aYou used your §6" + (getName()) + " §aPickaxe Ability!");
            MinecraftServer.getSchedulerManager().buildTask(() -> player.sendMessage("§6" + (getName()) + " §ais now ready!")).delay(TaskSchedule.seconds(cooldown())).schedule();
            onInteract();
        }, lore(1), new CooldownRequirement<>(cooldown()));
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
                .setName((level == 0 ? "§c" : "§a") + (getName()))
                .addAllLore(" ", "§6Pickaxe Ability: " + (getName()))
                .addAllLore(lore(level == 0 ? 1 : level).build(null, getPlayer()))
                .addLoreRow("§8Cooldown: §a" + (cooldown()) + "s")
                .addLoreRow("  ")
                .addLoreIf(() -> level != 0 && getPlayer().getHotm().getActiveAbility() == this, "§a§lSELECTED")
                .addLoreIf(() -> level != 0 && getPlayer().getHotm().getActiveAbility() != this, "§eClick to select!")
                .addAllLore()
                .build();
    }

    public abstract int cooldown();

    public abstract void onInteract();
}
