package me.carscupcake.sbremake.player;

import me.carscupcake.sbremake.util.TaskScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;

public class PlayerPet extends TaskScheduler {

    private final StoredPet petInfo;
    private final LivingEntity petHolder;
    private final LivingEntity nameTag;
    private final SkyblockPlayer player;
    int lastPetLevel;

    public PlayerPet(StoredPet petInfo, SkyblockPlayer player) {
        this.player = player;
        this.petInfo = petInfo;
        petHolder = new LivingEntity(EntityType.ARMOR_STAND);
        petHolder.editEntityMeta(ArmorStandMeta.class, armorStandMeta -> {
            armorStandMeta.setRightArmRotation(new Vec(Math.toDegrees(5.497787143782138), Math.toDegrees(4.066617157157146788), 0));
            armorStandMeta.setInvisible(true);
            armorStandMeta.setHasNoBasePlate(true);
            armorStandMeta.setHasNoGravity(true);
        });
        petHolder.setItemInMainHand(petInfo.toItem().update().item());
        nameTag = new LivingEntity(EntityType.ARMOR_STAND);
        nameTag.editEntityMeta(ArmorStandMeta.class, armorStandMeta -> {
            armorStandMeta.setInvisible(true);
            armorStandMeta.setHasNoBasePlate(true);
            armorStandMeta.setHasNoGravity(true);
        });
        nameTag.setCustomNameVisible(true);
        updateNameTag();
        petHolder.setInstance(player.getInstance());
        nameTag.setInstance(player.getInstance());
        setStandPosition(player.getPosition());
        basePos = petHolder.getPosition();
        repeatTask(1);
    }

    private Pos basePos;
    private double cycle = 0;

    @Override
    public void run() {
        if (basePos.distanceSquared(player.getPosition()) > 1.69) {
            Vec dir = player.getPosition().sub(basePos).asVec().normalize();
            basePos = basePos.add(dir.mul(0.4)).withDirection(dir);
        } else {
            cycle += 0.2;
            if (cycle == 20) cycle = 0;
        }
        setStandPosition(basePos);
        if (lastPetLevel != petInfo.getLevel()) {
            updateNameTag();
            player.sendMessage("§aYour pet leveled up " + (lastPetLevel) + ".");
        }
    }

    @Override
    public synchronized void cancel() {
        petHolder.remove();
        nameTag.remove();
        super.cancel();
    }

    public void updateNameTag() {
        lastPetLevel = petInfo.getLevel();
        nameTag.setCustomName(Component.text("§7[Lvl " + (petInfo.getLevel()) + "] " + (petInfo.getRarity().getPrefix()) + (((TextComponent) player.getName()).content()) + "'s " + (petInfo.getPet().getName())));
    }

    private double calculateYOffset() {
        return Math.sin(cycle * Math.PI * 0.1) * 0.5 + 0.5;
    }

    public void setStandPosition(Pos pos) {
        pos = pos.add(0, calculateYOffset(), 0);
        petHolder.teleport(pos);
        nameTag.teleport(pos.add(pos.withPitch(0).direction().mul(-0.3)).sub(0, 1, 0));
    }
}
