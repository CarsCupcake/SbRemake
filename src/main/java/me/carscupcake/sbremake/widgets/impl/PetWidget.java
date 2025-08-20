package me.carscupcake.sbremake.widgets.impl;

import me.carscupcake.sbremake.item.impl.pets.Pet;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.StoredPet;
import me.carscupcake.sbremake.util.Span;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.widgets.IWidget;
import me.carscupcake.sbremake.widgets.TabListItem;
import me.carscupcake.sbremake.widgets.WidgetTypes;
import net.kyori.adventure.text.Component;

public class PetWidget implements IWidget {
    private final SkyblockPlayer player;
    private StoredPet pet;
    private int allocatedSpace;
    private Span<TabListItem> allocated;
    private double petXp = 0;
    private int level = 0;

    public PetWidget(SkyblockPlayer player) {
        this.player = player;
    }

    @Override
    public void update() {
        if (player.getPet() != pet || (pet != null && (pet.getXp() != petXp || level != pet.getLevel()))) {
            var pet = player.getPet();
            if (pet == null && allocatedSpace != 2) {
                allocated.get(2).updateName(Component.text(""));
                player.getWidgetContainer().sheduleRerender();
                return;
            }
            if (pet != null && allocatedSpace != 3) {
                player.getWidgetContainer().sheduleRerender();
                return;
            }
            if (pet == null) return;
            this.pet = pet;
            this.petXp = pet.getXp();
            this.level = pet.getLevel();
            double totalDone = 0;
            for (int i = 1; i < pet.getLevel(); i++) {
                totalDone += Pet.PetInfo.nextLevelXp(pet.getRarity(), i, pet.getPet()
                        .getLevelingType());
            }
            var xpForThis = Pet.PetInfo.nextLevelXp(pet.getRarity(), pet.getLevel(), pet.getPet()
                    .getLevelingType());
            double percentage = (pet.getXp() - totalDone) / xpForThis;
            allocated.get(1).updateName(Component.text(" §7[" + pet.getLevel() + "] " + pet.getRarity().getPrefix() + pet.getPet().getName()));
            allocated.get(2).updateName(Component.text(" §e" + StringUtils.toShortNumber(pet.getXp() - totalDone) + "§6/§e" + StringUtils.toShortNumber(xpForThis) + " §6(" + StringUtils.cleanDouble(100 * percentage, 1) + "%)"));
        }
    }

    @Override
    public void render(Span<TabListItem> allocated) {
        petXp = -1;
        level = -1;
        this.allocated = allocated;
        allocated.getFirst().updateName(Component.text("§e§lPet"));
        if (allocatedSpace == 2) {
            allocated.get(1).updateName(Component.text(" §7No pet selected!"));
        } else {
            update();
        }
    }

    @Override
    public WidgetTypes type() {
        return null;
    }

    @Override
    public int requiredSpace() {
        allocatedSpace = player.getPet() == null ? 2 : 3;
        return allocatedSpace;
    }
}
